package com.agri40.sensoring.web.rest;

import com.agri40.sensoring.domain.Sante;
import com.agri40.sensoring.repository.SanteRepository;
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
 * REST controller for managing {@link com.agri40.sensoring.domain.Sante}.
 */
@RestController
@RequestMapping("/api")
public class SanteResource {

    private final Logger log = LoggerFactory.getLogger(SanteResource.class);

    private static final String ENTITY_NAME = "sensoringSante";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SanteRepository santeRepository;

    public SanteResource(SanteRepository santeRepository) {
        this.santeRepository = santeRepository;
    }

    /**
     * {@code POST  /santes} : Create a new sante.
     *
     * @param sante the sante to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sante, or with status {@code 400 (Bad Request)} if the sante has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/santes")
    public ResponseEntity<Sante> createSante(@RequestBody Sante sante) throws URISyntaxException {
        log.debug("REST request to save Sante : {}", sante);
        if (sante.getId() != null) {
            throw new BadRequestAlertException("A new sante cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sante result = santeRepository.save(sante);
        return ResponseEntity
            .created(new URI("/api/santes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    // add many santes
    @PostMapping("/santes/all")
    public ResponseEntity<List<Sante>> createSantes(@RequestBody List<Sante> santes) throws URISyntaxException {
        log.debug("REST request to save Sante : {}", santes);
        List<Sante> result = santeRepository.saveAll(santes);
        return ResponseEntity
            .created(new URI("/api/santes/all"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.get(0).getId()))
            .body(result);
    }

    /**
     * {@code PUT  /santes/:id} : Updates an existing sante.
     *
     * @param id the id of the sante to save.
     * @param sante the sante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sante,
     * or with status {@code 400 (Bad Request)} if the sante is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/santes/{id}")
    public ResponseEntity<Sante> updateSante(@PathVariable(value = "id", required = false) final String id, @RequestBody Sante sante)
        throws URISyntaxException {
        log.debug("REST request to update Sante : {}, {}", id, sante);
        if (sante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!santeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Sante result = santeRepository.save(sante);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sante.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /santes/:id} : Partial updates given fields of an existing sante, field will ignore if it is null
     *
     * @param id the id of the sante to save.
     * @param sante the sante to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sante,
     * or with status {@code 400 (Bad Request)} if the sante is not valid,
     * or with status {@code 404 (Not Found)} if the sante is not found,
     * or with status {@code 500 (Internal Server Error)} if the sante couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/santes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Sante> partialUpdateSante(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Sante sante
    ) throws URISyntaxException {
        log.debug("REST request to partial update Sante partially : {}, {}", id, sante);
        if (sante.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sante.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!santeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Sante> result = santeRepository
            .findById(sante.getId())
            .map(existingSante -> {
                if (sante.getDate() != null) {
                    existingSante.setDate(sante.getDate());
                }
                if (sante.getDureePositionCouchee() != null) {
                    existingSante.setDureePositionCouchee(sante.getDureePositionCouchee());
                }
                if (sante.getLeve() != null) {
                    existingSante.setLeve(sante.getLeve());
                }
                if (sante.getPas() != null) {
                    existingSante.setPas(sante.getPas());
                }
                if (sante.getCowId() != null) {
                    existingSante.setCowId(sante.getCowId());
                }

                return existingSante;
            })
            .map(santeRepository::save);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sante.getId()));
    }

    /**
     * {@code GET  /santes} : get all the santes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of santes in body.
     */
    @GetMapping("/santes")
    public ResponseEntity<List<Sante>> getAllSantes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Santes");
        Page<Sante> page = santeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /santes/:id} : get the "id" sante.
     *
     * @param id the id of the sante to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sante, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/santes/{id}")
    public ResponseEntity<Sante> getSante(@PathVariable String id) {
        log.debug("REST request to get Sante : {}", id);
        Optional<Sante> sante = santeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sante);
    }

    /**
     * {@code DELETE  /santes/:id} : delete the "id" sante.
     *
     * @param id the id of the sante to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/santes/{id}")
    public ResponseEntity<Void> deleteSante(@PathVariable String id) {
        log.debug("REST request to delete Sante : {}", id);
        santeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
