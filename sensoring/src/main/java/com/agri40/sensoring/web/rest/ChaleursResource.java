package com.agri40.sensoring.web.rest;

import com.agri40.sensoring.domain.Chaleurs;
import com.agri40.sensoring.repository.ChaleursRepository;
import com.agri40.sensoring.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.agri40.sensoring.domain.Chaleurs}.
 */
@RestController
@RequestMapping("/api")
public class ChaleursResource {

    private final Logger log = LoggerFactory.getLogger(ChaleursResource.class);

    private static final String ENTITY_NAME = "sensoringChaleurs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChaleursRepository chaleursRepository;

    public ChaleursResource(ChaleursRepository chaleursRepository) {
        this.chaleursRepository = chaleursRepository;
    }

    /**
     * {@code POST  /chaleurs} : Create a new chaleurs.
     *
     * @param chaleurs the chaleurs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chaleurs, or with status {@code 400 (Bad Request)} if the chaleurs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chaleurs")
    public ResponseEntity<Chaleurs> createChaleurs(@RequestBody Chaleurs chaleurs) throws URISyntaxException {
        log.debug("REST request to save Chaleurs : {}", chaleurs);
        if (chaleurs.getId() != null) {
            throw new BadRequestAlertException("A new chaleurs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Chaleurs result = chaleursRepository.save(chaleurs);
        return ResponseEntity
            .created(new URI("/api/chaleurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /chaleurs/:id} : Updates an existing chaleurs.
     *
     * @param id the id of the chaleurs to save.
     * @param chaleurs the chaleurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chaleurs,
     * or with status {@code 400 (Bad Request)} if the chaleurs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chaleurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chaleurs/{id}")
    public ResponseEntity<Chaleurs> updateChaleurs(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Chaleurs chaleurs
    ) throws URISyntaxException {
        log.debug("REST request to update Chaleurs : {}, {}", id, chaleurs);
        if (chaleurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chaleurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chaleursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Chaleurs result = chaleursRepository.save(chaleurs);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chaleurs.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /chaleurs/:id} : Partial updates given fields of an existing chaleurs, field will ignore if it is null
     *
     * @param id the id of the chaleurs to save.
     * @param chaleurs the chaleurs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chaleurs,
     * or with status {@code 400 (Bad Request)} if the chaleurs is not valid,
     * or with status {@code 404 (Not Found)} if the chaleurs is not found,
     * or with status {@code 500 (Internal Server Error)} if the chaleurs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chaleurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Chaleurs> partialUpdateChaleurs(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Chaleurs chaleurs
    ) throws URISyntaxException {
        log.debug("REST request to partial update Chaleurs partially : {}, {}", id, chaleurs);
        if (chaleurs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chaleurs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chaleursRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Chaleurs> result = chaleursRepository
            .findById(chaleurs.getId())
            .map(existingChaleurs -> {
                if (chaleurs.getDate() != null) {
                    existingChaleurs.setDate(chaleurs.getDate());
                }
                if (chaleurs.getJrsLact() != null) {
                    existingChaleurs.setJrsLact(chaleurs.getJrsLact());
                }
                if (chaleurs.getTemps() != null) {
                    existingChaleurs.setTemps(chaleurs.getTemps());
                }
                if (chaleurs.getGroupeid() != null) {
                    existingChaleurs.setGroupeid(chaleurs.getGroupeid());
                }
                if (chaleurs.getEnclosid() != null) {
                    existingChaleurs.setEnclosid(chaleurs.getEnclosid());
                }
                if (chaleurs.getActivite() != null) {
                    existingChaleurs.setActivite(chaleurs.getActivite());
                }
                if (chaleurs.getFacteurEleve() != null) {
                    existingChaleurs.setFacteurEleve(chaleurs.getFacteurEleve());
                }
                if (chaleurs.getSuspect() != null) {
                    existingChaleurs.setSuspect(chaleurs.getSuspect());
                }
                if (chaleurs.getActAugmentee() != null) {
                    existingChaleurs.setActAugmentee(chaleurs.getActAugmentee());
                }
                if (chaleurs.getAlarmeChaleur() != null) {
                    existingChaleurs.setAlarmeChaleur(chaleurs.getAlarmeChaleur());
                }
                if (chaleurs.getPasDeChaleur() != null) {
                    existingChaleurs.setPasDeChaleur(chaleurs.getPasDeChaleur());
                }
                if (chaleurs.getCowId() != null) {
                    existingChaleurs.setCowId(chaleurs.getCowId());
                }

                return existingChaleurs;
            })
            .map(chaleursRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chaleurs.getId())
        );
    }

    /**
     * {@code GET  /chaleurs} : get all the chaleurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chaleurs in body.
     */
    @GetMapping("/chaleurs")
    public ResponseEntity<List<Chaleurs>> getAllChaleurs(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Chaleurs");
        Page<Chaleurs> page = chaleursRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chaleurs/:id} : get the "id" chaleurs.
     *
     * @param id the id of the chaleurs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chaleurs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chaleurs/{id}")
    public ResponseEntity<Chaleurs> getChaleurs(@PathVariable String id) {
        log.debug("REST request to get Chaleurs : {}", id);
        Optional<Chaleurs> chaleurs = chaleursRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(chaleurs);
    }

    // by cowId
    @GetMapping("/chaleurs/cow/{cowId}")
    public ResponseEntity<List<Chaleurs>> getChaleursByCowId(@PathVariable String cowId) {
        log.debug("REST request to get Chaleurs : {}", cowId);
        List<Chaleurs> chaleurs = chaleursRepository.findByCowId(cowId);
        return ResponseEntity.ok().body(chaleurs);
    }

    /**
     * {@code DELETE  /chaleurs/:id} : delete the "id" chaleurs.
     *
     * @param id the id of the chaleurs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chaleurs/{id}")
    public ResponseEntity<Void> deleteChaleurs(@PathVariable String id) {
        log.debug("REST request to delete Chaleurs : {}", id);
        chaleursRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
