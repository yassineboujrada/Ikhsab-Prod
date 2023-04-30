package com.agri40.management.web.rest;

import com.agri40.management.domain.Calendar;
import com.agri40.management.repository.CalendarRepository;
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
 * REST controller for managing {@link com.agri40.management.domain.Calendar}.
 */
@RestController
@RequestMapping("/api")
public class CalendarResource {

    private final Logger log = LoggerFactory.getLogger(CalendarResource.class);

    private static final String ENTITY_NAME = "managementCalendar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarRepository calendarRepository;

    public CalendarResource(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    /**
     * {@code POST  /calendars} : Create a new calendar.
     *
     * @param calendar the calendar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendar, or with status {@code 400 (Bad Request)} if the calendar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendars")
    public ResponseEntity<Calendar> createCalendar(@RequestBody Calendar calendar) throws URISyntaxException {
        log.debug("REST request to save Calendar : {}", calendar);
        if (calendar.getId() != null) {
            throw new BadRequestAlertException("A new calendar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Calendar result = calendarRepository.save(calendar);
        return ResponseEntity
            .created(new URI("/api/calendars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /calendars/:id} : Updates an existing calendar.
     *
     * @param id the id of the calendar to save.
     * @param calendar the calendar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendar,
     * or with status {@code 400 (Bad Request)} if the calendar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendars/{id}")
    public ResponseEntity<Calendar> updateCalendar(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Calendar calendar
    ) throws URISyntaxException {
        log.debug("REST request to update Calendar : {}, {}", id, calendar);
        if (calendar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Calendar result = calendarRepository.save(calendar);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendar.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /calendars/:id} : Partial updates given fields of an existing calendar, field will ignore if it is null
     *
     * @param id the id of the calendar to save.
     * @param calendar the calendar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendar,
     * or with status {@code 400 (Bad Request)} if the calendar is not valid,
     * or with status {@code 404 (Not Found)} if the calendar is not found,
     * or with status {@code 500 (Internal Server Error)} if the calendar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/calendars/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Calendar> partialUpdateCalendar(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Calendar calendar
    ) throws URISyntaxException {
        log.debug("REST request to partial update Calendar partially : {}, {}", id, calendar);
        if (calendar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendarRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Calendar> result = calendarRepository
            .findById(calendar.getId())
            .map(existingCalendar -> {
                if (calendar.getLactation() != null) {
                    existingCalendar.setLactation(calendar.getLactation());
                }
                if (calendar.getJrsLact() != null) {
                    existingCalendar.setJrsLact(calendar.getJrsLact());
                }
                if (calendar.getStatutReproduction() != null) {
                    existingCalendar.setStatutReproduction(calendar.getStatutReproduction());
                }
                if (calendar.getEtatProd() != null) {
                    existingCalendar.setEtatProd(calendar.getEtatProd());
                }
                if (calendar.getDateNaissance() != null) {
                    existingCalendar.setDateNaissance(calendar.getDateNaissance());
                }
                if (calendar.getVelage() != null) {
                    existingCalendar.setVelage(calendar.getVelage());
                }
                if (calendar.getChaleur() != null) {
                    existingCalendar.setChaleur(calendar.getChaleur());
                }
                if (calendar.getInsemination() != null) {
                    existingCalendar.setInsemination(calendar.getInsemination());
                }
                if (calendar.getCowId() != null) {
                    existingCalendar.setCowId(calendar.getCowId());
                }

                return existingCalendar;
            })
            .map(calendarRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendar.getId())
        );
    }

    /**
     * {@code GET  /calendars} : get all the calendars.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendars in body.
     */
    @GetMapping("/calendars")
    public ResponseEntity<List<Calendar>> getAllCalendars(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Calendars");
        Page<Calendar> page = calendarRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /calendars/:id} : get the "id" calendar.
     *
     * @param id the id of the calendar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendars/{id}")
    public ResponseEntity<Calendar> getCalendar(@PathVariable String id) {
        log.debug("REST request to get Calendar : {}", id);
        Optional<Calendar> calendar = calendarRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(calendar);
    }

    // all by cow id
    @GetMapping("/calendars/cow/{id}")
    public ResponseEntity<List<Calendar>> getAllCalendarsByCowId(@PathVariable String id) {
        log.debug("REST request to get a page of Calendars");
        List<Calendar> page = calendarRepository.findAllByCowId(id);
        return ResponseEntity.ok().body(page);
    }

    /**
     * {@code DELETE  /calendars/:id} : delete the "id" calendar.
     *
     * @param id the id of the calendar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendars/{id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable String id) {
        log.debug("REST request to delete Calendar : {}", id);
        calendarRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
