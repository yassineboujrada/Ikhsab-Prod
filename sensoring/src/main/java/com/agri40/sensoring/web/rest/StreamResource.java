package com.agri40.sensoring.web.rest;

import com.agri40.sensoring.domain.Chaleurs;
import com.agri40.sensoring.domain.Stream;
// import com.agri40.sensoring.rabbitmq.QueueSender;
import com.agri40.sensoring.repository.ChaleursRepository;
import com.agri40.sensoring.repository.SanteRepository;
import com.agri40.sensoring.repository.StreamRepository;
import com.agri40.sensoring.web.rest.errors.BadRequestAlertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.s;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agri40.sensoring.domain.Stream}.
 */
@RestController
@RequestMapping("/api")
public class StreamResource {

    private final Logger log = LoggerFactory.getLogger(StreamResource.class);
    ObjectMapper objectMapper = new ObjectMapper();

    private static final String ENTITY_NAME = "sensoringStream";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StreamRepository streamRepository;
    private final SanteRepository santeRepository;
    private final ChaleursRepository chaleurRepository;

    // @Autowired
    // private QueueSender queueSender;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public StreamResource(StreamRepository streamRepository, SanteRepository santeRepository, ChaleursRepository chaleurRepository) {
        this.streamRepository = streamRepository;
        this.santeRepository = santeRepository;
        this.chaleurRepository = chaleurRepository;
    }

    @GetMapping("/device-stream")
    public ResponseEntity<Stream> createStream(
        @RequestParam("api_key") String apiKey,
        @RequestParam("field1") String temperature,
        @RequestParam("field2") String humidity,
        @RequestParam("field3") String tag,
        @RequestParam("field4") String counter
    ) throws URISyntaxException {
        // check if the api key is valid
        if (!(apiKey.equals("Yjg3NzRlYWE2YjczZWM2NDUzMjFkNDNhY2RlZTBlMWUyNjJmNWQyODhiNTE1"))) {
            throw new BadRequestAlertException("Invalid api key", ENTITY_NAME, "invalidapikey");
        }
        // now create a stream object in params put temperature, humidity, counter and in date put the current date tag as device id and type as 'rfid'
        Map<String, Object> params = new HashMap<>();
        params.put("temperature", temperature);
        params.put("humidity", humidity);
        params.put("counter", counter);
        params.put("tag", tag);
        Stream stream = new Stream();
        stream.setParams(params);
        // set creqtedAt as linux timestamp
        stream.setCreatedAt(Long.toString(Instant.now().getEpochSecond()));
        // stream.setDeviceId("rfid-cow");
        stream.setDeviceId("rfid-cow");
        stream.setType("RFID");
        return createStream(stream, apiKey);
    }

    /**
     * {@code POST  /streams} : Create a new stream.
     *
     * @param stream the stream to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new stream, or with status {@code 400 (Bad Request)} if the
     *         stream has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/streams")
    public ResponseEntity<Stream> createStream(@RequestBody Stream stream, @RequestParam("api_key") String apiKey)
        throws URISyntaxException {
        if (!(apiKey.equals("Yjg3NzRlYWE2YjczZWM2NDUzMjFkNDNhY2RlZTBlMWUyNjJmNWQyODhiNTE1"))) {
            throw new BadRequestAlertException("Invalid api key", ENTITY_NAME, "invalidapikey");
        }
        log.debug("REST request to save Stream : {}", stream);

        if (stream.getId() != null) {
            throw new BadRequestAlertException("A new stream cannot already have an ID", ENTITY_NAME, "idexists");
        }

        try{
            validateStream(stream);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert(applicationName, true, 
                    ENTITY_NAME, "invalidstream", e.getMessage()))
                    .body(null);
        }

        
        Map<String, Object> cowAndProfileData = (Map<String, Object>) rabbitTemplate.convertSendAndReceive("icow.deviceid",stream.getDeviceId());
        if (cowAndProfileData == null) {
            throw new BadRequestAlertException("Invalid device id", ENTITY_NAME, "invaliddeviceid");
        }
        stream.setCowId((String) cowAndProfileData.get("cowId"));
        if (stream.getType().equals("PEDOMETRE")) {
            processPedometerStream(stream);
            // get last 10 saved streams
            List<Stream> streams = streamRepository.findFirst10ByCowIdAndTypeOrderByCreatedAtDesc(stream.getCowId(),
                    "PEDOMETRE");
            int numStreams = streams.size();
            int threshold = (int) (Math.random() * 4 + 4); // random number between 4 and 7
            int countHighActivity = 0;
            for (int i = 0; i < numStreams; i++) {
                Stream s = streams.get(i);
                if (Integer.parseInt(s.getParams().get("stepNumber").toString()) >= threshold) {
                    countHighActivity++;
                } 
            }
            Map<String, Object> params1 = stream.getParams();
            params1.put("avgStepNumber", threshold);
            stream.setParams(params1);
            Map<String, Object> liveStream = new HashMap<>();
            liveStream.put("avgStepNumber", threshold);
            liveStream.put("createdAt", stream.getCreatedAt());
            liveStream.put("room", "notification"+stream.getCowId());
            liveStream.put("stepNumber", stream.getParams().get("stepNumber"));
            if(Integer.parseInt(stream.getParams().get("stepNumber").toString()) >= threshold){
                liveStream.put("highActivity", true);
                log.debug("********* high activity *********");
            }
            else 
                liveStream.put("highActivity", false);

            if (countHighActivity >= numStreams * 0.3){
                liveStream.put("cowHot", true);
                log.debug("********* Hot Cow *********");
                // cowid user id phone number
                rabbitTemplate.convertAndSend("icow.hotSuspect", cowAndProfileData);
            }
            else
                liveStream.put("cowHot", false);
            log.debug("live Stream : {}", liveStream);
            
            rabbitTemplate.convertSendAndReceive("icow.live-stream", liveStream);
        } else if (stream.getType().equals("COLLAR")) {
            processCollarStream(stream);
        }
        else if (stream.getType().equals("RFID")){
            
            Map<String, Object> liveStream = new HashMap<>();
            liveStream.put("createdAt", stream.getCreatedAt());
            liveStream.put("room", "notification" + stream.getCowId());
            liveStream.put("tag", stream.getParams().get("tag"));
            rabbitTemplate.convertSendAndReceive("icow.live-stream", liveStream);
        }
        // 
        
        Stream result = streamRepository.save(stream);
        return ResponseEntity
                .created(new URI("/api/streams/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                .body(result);

    }

    private void validateStream(Stream stream) throws BadRequestAlertException {
        // Check if the required fields are present and have the correct data type
        if (stream.getType() == null || !(stream.getType() instanceof String)) {
            throw new BadRequestAlertException("Invalid stream object: type is missing or not a string", ENTITY_NAME, "invalidstream");
        }

        if (stream.getCreatedAt() == null || !(stream.getCreatedAt() instanceof String)) {
            throw new BadRequestAlertException("Invalid stream object: createdAt is missing or not a string", ENTITY_NAME, "invalidstream");
        }

        if (stream.getParams() == null || !(stream.getParams() instanceof Map)) {
            throw new BadRequestAlertException("Invalid stream object: params are missing or not a map", ENTITY_NAME, "invalidstream");
        }

        // Check if the stream type is valid
        String type = (String) stream.getType();
        if (!type.equals("PEDOMETRE") && !type.equals("COLLAR") && !type.equals("RFID")) {
            throw new BadRequestAlertException("Invalid stream object: type is not valid", ENTITY_NAME, "invalidstream");
        }

        // Check if the params have the correct data type
        Map<String, Object> params = stream.getParams();
        if (type.equals("PEDOMETRE")) {
            if (!params.containsKey("stepNumber") || !(params.get("stepNumber") instanceof String)) {
                throw new BadRequestAlertException(
                    "Invalid stream object: stepNumber is missing or not a string",
                    ENTITY_NAME,
                    "invalidstream"
                );
            }
        } else if (type.equals("COLLAR")) {
            if (
                !params.containsKey("lastActivity") ||
                !(params.get("lastActivity") instanceof Double || params.get("lastActivity") instanceof Integer)
            ) {
                throw new BadRequestAlertException(
                    "Invalid stream object: lastActivity is missing or not a Double nor an Integer",
                    ENTITY_NAME,
                    "invalidstream"
                );
            }
        }
    }

    private void processPedometerStream(Stream stream) {
        String cowId = "643947b4e7f89a54a39b0ba7";
        Instant instant = Instant.ofEpochSecond(Math.round(Float.parseFloat(stream.getCreatedAt())));

        Optional<Chaleurs> chaleur = chaleurRepository.findFirstByCowIdOrderByDateDesc(cowId);
        if (chaleur.isPresent()) {
            Instant truncatedInstant1 = instant.truncatedTo(ChronoUnit.DAYS);
            Instant truncatedInstant2 = chaleur.orElseThrow().getDate().truncatedTo(ChronoUnit.DAYS);
            Chaleurs oldchaleur = chaleur.orElseThrow();
            if (truncatedInstant1.equals(truncatedInstant2)) {
                log.debug("--------new day-------");
                // delete the old one Jrs lact.
                Chaleurs newChaleur = new Chaleurs();
                newChaleur.setCowId("643947b4e7f89a54a39b0ba7");
                newChaleur.setDate(instant);
                newChaleur.setGroupeid("B1");
                newChaleur.setEnclosid("13");
                newChaleur.setJrsLact(chaleur.orElseThrow().getJrsLact());
                // set the Temps which is hour in createdAt
                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                int hour = ldt.getHour();
                int minute = ldt.getMinute();
                newChaleur.setTemps(hour + ":" + minute);
                // from params get stepNumber
                newChaleur.setActivite(stream.getParams().get("stepNumber").toString());
                chaleurRepository.save(newChaleur);
                oldchaleur.setJrsLact(null);
                chaleurRepository.save(oldchaleur);
            } else {
                log.debug("--------same day-------");
                Chaleurs newChaleur = new Chaleurs();
                newChaleur.setCowId("643947b4e7f89a54a39b0ba7");
                newChaleur.setDate(instant);
                newChaleur.setGroupeid("B1");
                newChaleur.setEnclosid("13");
                newChaleur.setJrsLact(chaleur.orElseThrow().getJrsLact() + 1);
                // set the Temps which is hour in createdAt
                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                int hour = ldt.getHour();
                int minute = ldt.getMinute();
                newChaleur.setTemps(hour + ":" + minute);
                // from params get stepNumber
                newChaleur.setActivite(stream.getParams().get("stepNumber").toString());
                chaleurRepository.save(newChaleur);
            }
        }
    }

    private void processCollarStream(Stream stream) {
        int lastActivity = ((Double) stream.getParams().get("lastActivity")).intValue();

        if (lastActivity > 10) {
            // Get the last two streams of type COLLAR and check if they passed the 10
            // threshold
            List<Stream> streams = streamRepository.findByCowIdAndTypeOrderByCreatedAtDesc(stream.getCowId(), "COLLAR");
            if (streams.size() > 1) {
                int lastActivity1 = ((Double) streams.get(0).getParams().get("lastActivity")).intValue();
                int lastActivity2 = ((Double) streams.get(1).getParams().get("lastActivity")).intValue();

                log.info("lastActivity1: " + streams.get(0));
                log.info("lastActivity2: " + streams.get(1));
                log.info("lastActivity: " + lastActivity);
                if (lastActivity1 > 10 && lastActivity2 > 10) {
                    // Send message to the user
                    log.debug("===================== send message to the user =====================");
                    // queueSender.send("Numéro de vache : " + stream.getCowId() + " est en chaleur");
                    log.debug("Message sent to the queue");
                    log.debug("Message saved in the database");
                }
            }
        }
    }

    @PostMapping("/streams/refId")
    public ResponseEntity<Stream> createStreamByRefId(@RequestBody Stream stream) throws URISyntaxException {
        log.debug("REST request to save Stream : {}", stream);
        if (stream.getId() != null) {
            throw new BadRequestAlertException("A new stream cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Stream result = streamRepository.save(stream);
        return ResponseEntity
            .created(new URI("/api/streams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /streams/:id} : Updates an existing stream.
     *
     * @param id     the id of the stream to save.
     * @param stream the stream to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated stream,
     *         or with status {@code 400 (Bad Request)} if the stream is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the stream
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/streams/{id}")
    public ResponseEntity<Stream> updateStream(@PathVariable(value = "id", required = false) final String id, @RequestBody Stream stream)
        throws URISyntaxException {
        log.debug("REST request to update Stream : {}, {}", id, stream);
        if (stream.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stream.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (streamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Stream result = streamRepository.save(stream);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stream.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /streams/:id} : Partial updates given fields of an existing
     * stream, field will ignore if it is null
     *
     * @param id     the id of the stream to save.
     * @param stream the stream to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated stream,
     *         or with status {@code 400 (Bad Request)} if the stream is not valid,
     *         or with status {@code 404 (Not Found)} if the stream is not found,
     *         or with status {@code 500 (Internal Server Error)} if the stream
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/streams/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Stream> partialUpdateStream(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Stream stream
    ) throws URISyntaxException {
        log.debug("REST request to partial update Stream partially : {}, {}", id, stream);
        if (stream.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stream.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!streamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Stream> result = streamRepository
            .findById(stream.getId())
            .map(existingStream -> {
                if (stream.getType() != null) {
                    existingStream.setType(stream.getType());
                }
                if (stream.getParams() != null) {
                    existingStream.setParams(stream.getParams());
                }
                if (stream.getDeviceId() != null) {
                    existingStream.setDeviceId(stream.getDeviceId());
                }
                if (stream.getCreatedAt() != null) {
                    existingStream.setCreatedAt(stream.getCreatedAt());
                }

                return existingStream;
            })
            .map(streamRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stream.getId()));
    }

    /**
     * {@code GET  /streams} : get all the streams.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of streams in body.
     */
    @GetMapping("/streams")
    public ResponseEntity<List<Stream>> getAllStreams(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Streams");
        Page<Stream> page = streamRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /streams/:id} : get the "id" stream.
     *
     * @param id the id of the stream to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the stream, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/streams/{id}")
    public ResponseEntity<Stream> getStream(@PathVariable String id) {
        log.debug("REST request to get Stream : {}", id);
        Optional<Stream> stream = streamRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stream);
    }

    // get last stream by created date
    @GetMapping("/streams/last")
    public ResponseEntity<Stream> getLastStream() {
        log.debug("REST request to get last Stream");
        Optional<Stream> stream = streamRepository.findFirstByOrderByCreatedAtDesc();
        return ResponseUtil.wrapOrNotFound(stream);
    }

    // by cowId
    @GetMapping("/streams/cow/{cowId}")
    public ResponseEntity<List<Stream>> getStreamByCowId(@PathVariable String cowId,@RequestParam(value = "size", required = false) String size){ 
        log.debug("REST request to get Stream by cowId : {}", cowId);
        // last size streams
        List<Stream> streams = new ArrayList<>();
        if(size != null){
            streams = streamRepository.findByCowIdOrderByCreatedAtDesc(cowId, PageRequest.of(0, Integer.parseInt(size)));
        }else{
            streams = streamRepository.findByCowIdOrderByCreatedAtDesc(cowId);
        }
        return ResponseEntity.ok().body(streams);
    }

    /**
     * {@code DELETE  /streams/:id} : delete the "id" stream.
     *
     * @param id the id of the stream to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/streams/{id}")
    public ResponseEntity<Void> deleteStream(@PathVariable String id) {
        log.debug("REST request to delete Stream : {}", id);
        streamRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
