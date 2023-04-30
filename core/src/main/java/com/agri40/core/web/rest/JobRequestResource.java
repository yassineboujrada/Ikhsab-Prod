package com.agri40.core.web.rest;

import com.agri40.core.domain.JobRequest;
import com.agri40.core.repository.JobRequestRepository;
import com.agri40.core.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.agri40.core.domain.JobRequest}.
 */
@RestController
@RequestMapping("/api")
public class JobRequestResource {

    private final Logger log = LoggerFactory.getLogger(JobRequestResource.class);

    private static final String ENTITY_NAME = "jobRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobRequestRepository jobRequestRepository;

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
    public Mono<ResponseEntity<JobRequest>> createJobRequest(@RequestBody JobRequest jobRequest) throws URISyntaxException {
        log.debug("REST request to save JobRequest : {}", jobRequest);
        if (jobRequest.getId() != null) {
            throw new BadRequestAlertException("A new jobRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return jobRequestRepository
            .save(jobRequest)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/job-requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<JobRequest>> updateJobRequest(
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

        return jobRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return jobRequestRepository
                    .save(jobRequest)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<JobRequest>> partialUpdateJobRequest(
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

        return jobRequestRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<JobRequest> result = jobRequestRepository
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

                        return existingJobRequest;
                    })
                    .flatMap(jobRequestRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /job-requests} : get all the jobRequests.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobRequests in body.
     */
    @GetMapping("/job-requests")
    public Mono<ResponseEntity<List<JobRequest>>> getAllJobRequests(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of JobRequests");
        return jobRequestRepository
            .count()
            .zipWith(jobRequestRepository.findAllBy(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /job-requests/:id} : get the "id" jobRequest.
     *
     * @param id the id of the jobRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-requests/{id}")
    public Mono<ResponseEntity<JobRequest>> getJobRequest(@PathVariable String id) {
        log.debug("REST request to get JobRequest : {}", id);
        Mono<JobRequest> jobRequest = jobRequestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobRequest);
    }

    /**
     * {@code DELETE  /job-requests/:id} : delete the "id" jobRequest.
     *
     * @param id the id of the jobRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-requests/{id}")
    public Mono<ResponseEntity<Void>> deleteJobRequest(@PathVariable String id) {
        log.debug("REST request to delete JobRequest : {}", id);
        return jobRequestRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
