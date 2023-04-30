package com.agri40.management.web.rest;

import com.agri40.management.domain.Enclos;
import com.agri40.management.repository.EnclosRepository;
import com.agri40.management.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.agri40.management.domain.Enclos}.
 */
@RestController
@RequestMapping("/api")
public class EnclosResource {

    private final Logger log = LoggerFactory.getLogger(EnclosResource.class);

    private static final String ENTITY_NAME = "managementEnclos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnclosRepository enclosRepository;

    public EnclosResource(EnclosRepository enclosRepository) {
        this.enclosRepository = enclosRepository;
    }

    /**
     * {@code POST  /enclos} : Create a new enclos.
     *
     * @param enclos the enclos to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enclos, or with status {@code 400 (Bad Request)} if the enclos has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enclos")
    public ResponseEntity<Enclos> createEnclos(@RequestBody Enclos enclos) throws URISyntaxException {
        log.debug("REST request to save Enclos : {}", enclos);
        if (enclos.getId() != null) {
            throw new BadRequestAlertException("A new enclos cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Enclos result = enclosRepository.save(enclos);
        return ResponseEntity
            .created(new URI("/api/enclos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /enclos/:id} : Updates an existing enclos.
     *
     * @param id the id of the enclos to save.
     * @param enclos the enclos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enclos,
     * or with status {@code 400 (Bad Request)} if the enclos is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enclos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enclos/{id}")
    public ResponseEntity<Enclos> updateEnclos(@PathVariable(value = "id", required = false) final String id, @RequestBody Enclos enclos)
        throws URISyntaxException {
        log.debug("REST request to update Enclos : {}, {}", id, enclos);
        if (enclos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enclos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enclosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Enclos result = enclosRepository.save(enclos);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enclos.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /enclos/:id} : Partial updates given fields of an existing enclos, field will ignore if it is null
     *
     * @param id the id of the enclos to save.
     * @param enclos the enclos to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enclos,
     * or with status {@code 400 (Bad Request)} if the enclos is not valid,
     * or with status {@code 404 (Not Found)} if the enclos is not found,
     * or with status {@code 500 (Internal Server Error)} if the enclos couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/enclos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Enclos> partialUpdateEnclos(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Enclos enclos
    ) throws URISyntaxException {
        log.debug("REST request to partial update Enclos partially : {}, {}", id, enclos);
        if (enclos.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enclos.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enclosRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Enclos> result = enclosRepository
            .findById(enclos.getId())
            .map(existingEnclos -> {
                if (enclos.getName() != null) {
                    existingEnclos.setName(enclos.getName());
                }
                if (enclos.getUserId() != null) {
                    existingEnclos.setUserId(enclos.getUserId());
                }
                if (enclos.getGroupId() != null) {
                    existingEnclos.setGroupId(enclos.getGroupId());
                }

                return existingEnclos;
            })
            .map(enclosRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enclos.getId()));
    }

    /**
     * {@code GET  /enclos} : get all the enclos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enclos in body.
     */
    @GetMapping("/enclos")
    public ResponseEntity<List<Enclos>> getAllEnclos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Enclos");
        Page<Enclos> page = enclosRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enclos/:id} : get the "id" enclos.
     *
     * @param id the id of the enclos to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enclos, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enclos/{id}")
    public ResponseEntity<Enclos> getEnclos(@PathVariable String id) {
        log.debug("REST request to get Enclos : {}", id);
        Optional<Enclos> enclos = enclosRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enclos);
    }

    // get all by group id
    @GetMapping("/enclos/group/{groupId}")
    public ResponseEntity<List<Enclos>> getAllEnclosByGroupId(@PathVariable String groupId) {
        log.debug("REST request to get all Enclos by group id : {}", groupId);
        List<Enclos> enclos = enclosRepository.findAllByGroupId(groupId);
        return ResponseEntity.ok().body(enclos);
    }

    /**
     * {@code DELETE  /enclos/:id} : delete the "id" enclos.
     *
     * @param id the id of the enclos to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enclos/{id}")
    public ResponseEntity<Void> deleteEnclos(@PathVariable String id) {
        log.debug("REST request to delete Enclos : {}", id);
        enclosRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
