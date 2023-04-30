package com.agri40.notification.web.rest;

import com.agri40.notification.domain.JobRequest;
import com.agri40.notification.repository.JobRequestRepository;
import com.agri40.notification.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agri40.notification.domain.JobRequest}.
 */
@RestController
@RequestMapping("/api")
public class JobRequestResource {

    private final Logger log = LoggerFactory.getLogger(JobRequestResource.class);

    private static final String ENTITY_NAME = "notificationJobRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobRequestRepository jobRequestRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public JobRequestResource(JobRequestRepository jobRequestRepository) {
        this.jobRequestRepository = jobRequestRepository;
    }

    /**
     * {@code POST  /job-requests} : Create a new jobRequest.
     *
     * @param jobRequest the jobRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobRequest, or with status {@code 400 (Bad Request)} if the jobRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-requests")
    public ResponseEntity<JobRequest> createJobRequest(@RequestBody JobRequest jobRequest) throws URISyntaxException {
        log.debug("REST request to save JobRequest : {}", jobRequest);
        if (jobRequest.getId() != null) {
            throw new BadRequestAlertException("A new jobRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobRequest result = jobRequestRepository.save(jobRequest);
        return ResponseEntity
            .created(new URI("/api/job-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /job-requests/:id} : Updates an existing jobRequest.
     *
     * @param id the id of the jobRequest to save.
     * @param jobRequest the jobRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobRequest,
     * or with status {@code 400 (Bad Request)} if the jobRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-requests/{id}")
    public ResponseEntity<JobRequest> updateJobRequest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody JobRequest jobRequest
    ) throws URISyntaxException {
        log.debug("REST request to update JobRequest : {}, {}", id, jobRequest);
        if (jobRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobRequest result = jobRequestRepository.save(jobRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobRequest.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-requests/:id} : Partial updates given fields of an existing jobRequest, field will ignore if it is null
     *
     * @param id the id of the jobRequest to save.
     * @param jobRequest the jobRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobRequest,
     * or with status {@code 400 (Bad Request)} if the jobRequest is not valid,
     * or with status {@code 404 (Not Found)} if the jobRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobRequest> partialUpdateJobRequest(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody JobRequest jobRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobRequest partially : {}, {}", id, jobRequest);
        if (jobRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobRequest> result = jobRequestRepository
            .findById(jobRequest.getId())
            .map(existingJobRequest -> {
                if (jobRequest.getConsumer() != null) {
                    existingJobRequest.setConsumer(jobRequest.getConsumer());
                }
                if (jobRequest.getProvider() != null) {
                    existingJobRequest.setProvider(jobRequest.getProvider());
                }
                if (jobRequest.getServiceStatus() != null) {
                    existingJobRequest.setServiceStatus(jobRequest.getServiceStatus());
                }
                if (jobRequest.getRoomId() != null) {
                    existingJobRequest.setRoomId(jobRequest.getRoomId());
                }
                if (jobRequest.getCowId() != null) {
                    existingJobRequest.setCowId(jobRequest.getCowId());
                }

                return existingJobRequest;
            })
            .map(jobRequestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobRequest.getId())
        );
    }

    /**
     * {@code GET  /job-requests} : get all the jobRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobRequests in body.
     */
    @GetMapping("/job-requests")
    public ResponseEntity<List<JobRequest>> getAllJobRequests(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of JobRequests");
        Page<JobRequest> page = jobRequestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/job-requests/user-requests/{id}")
    // if id is Provider or Consumer
    public ResponseEntity<List<JobRequest>> getAllJobRequestsByProviderOrConsumer(@PathVariable String id) {
        log.debug("REST request to get a page of JobRequests");
        List<JobRequest> jobRequests = jobRequestRepository.findAllByProviderOrConsumer(id, id);
        return ResponseEntity.ok().body(jobRequests);
    }

    // set job as done
    @GetMapping("/job-requests/set-as-done/{id}")
    public ResponseEntity<JobRequest> setJobAsDone(@PathVariable String id) {
        log.debug("REST request to get a page of JobRequests");
        Optional<JobRequest> jobRequest = jobRequestRepository.findById(id);
        if (jobRequest.isPresent()) {
            rabbitTemplate.convertAndSend("icow.addInsemineEvent", jobRequest.get().getCowId());
            jobRequest.get().setServiceStatus("DONE");
            jobRequestRepository.save(jobRequest.get());
            return ResponseEntity.ok().body(jobRequest.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // get confirmed job requests
    @GetMapping("/job-requests/confirmed-requests/{id}")
    public ResponseEntity<List<JobRequest>> getConfirmedJobRequests(@PathVariable String id) {
        log.debug("REST request to get a page of JobRequests");
        List<JobRequest> jobRequests = jobRequestRepository.findAllByProviderOrConsumerAndServiceStatus(id, id, "CONFIRMED");
        return ResponseEntity.ok().body(jobRequests);
    }

    // get all completed job requests
    @GetMapping("/job-requests/completed-requests/{id}")
    public ResponseEntity<List<JobRequest>> getCompletedJobRequests(@PathVariable String id) {
        log.debug("REST request to get a page of JobRequests");
        List<JobRequest> jobRequests = jobRequestRepository.findAllByProviderOrConsumerAndServiceStatus(id, id, "DONE");
        return ResponseEntity.ok().body(jobRequests);
    }

    /**
     * {@code GET  /job-requests/:id} : get the "id" jobRequest.
     *
     * @param id the id of the jobRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-requests/{id}")
    public ResponseEntity<JobRequest> getJobRequest(@PathVariable String id) {
        log.debug("REST request to get JobRequest : {}", id);
        Optional<JobRequest> jobRequest = jobRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobRequest);
    }

    /**
     * {@code DELETE  /job-requests/:id} : delete the "id" jobRequest.
     *
     * @param id the id of the jobRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-requests/{id}")
    public ResponseEntity<Void> deleteJobRequest(@PathVariable String id) {
        log.debug("REST request to delete JobRequest : {}", id);
        jobRequestRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
