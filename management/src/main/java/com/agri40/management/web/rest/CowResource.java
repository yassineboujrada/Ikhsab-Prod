package com.agri40.management.web.rest;

import com.agri40.management.domain.Cow;
import com.agri40.management.repository.CowRepository;
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
 * REST controller for managing {@link com.agri40.management.domain.Cow}.
 */
@RestController
@RequestMapping("/api")
public class CowResource {

    private final Logger log = LoggerFactory.getLogger(CowResource.class);

    private static final String ENTITY_NAME = "managementCow";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CowRepository cowRepository;

    public CowResource(CowRepository cowRepository) {
        this.cowRepository = cowRepository;
    }

    /**
     * {@code POST  /cows} : Create a new cow.
     *
     * @param cow the cow to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cow, or with status {@code 400 (Bad Request)} if the cow has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cows")
    public ResponseEntity<Cow> createCow(@RequestBody Cow cow) throws URISyntaxException {
        log.debug("REST request to save Cow : {}", cow);
        if (cow.getId() != null) {
            throw new BadRequestAlertException("A new cow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cow result = cowRepository.save(cow);
        return ResponseEntity
            .created(new URI("/api/cows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /cows/:id} : Updates an existing cow.
     *
     * @param id the id of the cow to save.
     * @param cow the cow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cow,
     * or with status {@code 400 (Bad Request)} if the cow is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cows/{id}")
    public ResponseEntity<Cow> updateCow(@PathVariable(value = "id", required = false) final String id, @RequestBody Cow cow)
        throws URISyntaxException {
        log.debug("REST request to update Cow : {}, {}", id, cow);
        if (cow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cow result = cowRepository.save(cow);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cow.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /cows/:id} : Partial updates given fields of an existing cow, field will ignore if it is null
     *
     * @param id the id of the cow to save.
     * @param cow the cow to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cow,
     * or with status {@code 400 (Bad Request)} if the cow is not valid,
     * or with status {@code 404 (Not Found)} if the cow is not found,
     * or with status {@code 500 (Internal Server Error)} if the cow couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cows/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cow> partialUpdateCow(@PathVariable(value = "id", required = false) final String id, @RequestBody Cow cow)
        throws URISyntaxException {
        log.debug("REST request to partial update Cow partially : {}, {}", id, cow);
        if (cow.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cow.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cowRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cow> result = cowRepository
            .findById(cow.getId())
            .map(existingCow -> {
                if (cow.getNumero() != null) {
                    existingCow.setNumero(cow.getNumero());
                }
                if (cow.getGroupeId() != null) {
                    existingCow.setGroupeId(cow.getGroupeId());
                }
                if (cow.getEnclosId() != null) {
                    existingCow.setEnclosId(cow.getEnclosId());
                }
                if (cow.getRepondeur() != null) {
                    existingCow.setRepondeur(cow.getRepondeur());
                }
                if (cow.getNom() != null) {
                    existingCow.setNom(cow.getNom());
                }
                if (cow.getDeviceId() != null) {
                    existingCow.setDeviceId(cow.getDeviceId());
                }
                if (cow.getUserId() != null) {
                    existingCow.setUserId(cow.getUserId());
                }
                if (cow.getWaitingForInseminator() != null) {
                    existingCow.setWaitingForInseminator(cow.getWaitingForInseminator());
                }

                return existingCow;
            })
            .map(cowRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cow.getId()));
    }

    /**
     * {@code GET  /cows} : get all the cows.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cows in body.
     */
    @GetMapping("/cows")
    public ResponseEntity<List<Cow>> getAllCows(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cows");
        Page<Cow> page = cowRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cows/:id} : get the "id" cow.
     *
     * @param id the id of the cow to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cow, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cows/{id}")
    public ResponseEntity<Cow> getCow(@PathVariable String id) {
        log.debug("REST request to get Cow : {}", id);
        Optional<Cow> cow = cowRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cow);
    }

    @GetMapping("/cows/enclos/{id}")
    public ResponseEntity<List<Cow>> getCowByEnclos(@PathVariable String id) {
        log.debug("REST request to get Cow : {}", id);
        List<Cow> cows = cowRepository.findByEnclosId(id);
        return ResponseEntity.ok().body(cows);
    }

    // set waiting for inseminator to true
    @GetMapping("/cows/{id}/set-as-waiting")
    public ResponseEntity<Cow> setWaitingForInseminator(@PathVariable String id) {
        log.debug("REST request to get Cow : {}", id);
        Optional<Cow> cow = cowRepository.findById(id);
        if (cow.isPresent()) {
            Cow c = cow.get();
            c.setWaitingForInseminator(true);
            cowRepository.save(c);
            return ResponseEntity.ok().body(c);
        }
        return ResponseEntity.notFound().build();
    }

    // get waiting for inseminator
    @GetMapping("/cows/waiting-for-inseminator")
    public ResponseEntity<List<Cow>> getWaitingForInseminator() {
        log.debug("REST request to get Cow : {}");
        List<Cow> cows = cowRepository.findByWaitingForInseminator(true);
        return ResponseEntity.ok().body(cows);
    }

    /**
     * {@code DELETE  /cows/:id} : delete the "id" cow.
     *
     * @param id the id of the cow to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cows/{id}")
    public ResponseEntity<Void> deleteCow(@PathVariable String id) {
        log.debug("REST request to delete Cow : {}", id);
        cowRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
