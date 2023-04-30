package com.agri40.core.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.agri40.core.IntegrationTest;
import com.agri40.core.domain.JobRequest;
import com.agri40.core.repository.JobRequestRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link JobRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class JobRequestResourceIT {

    private static final String DEFAULT_CONSUMER = "AAAAAAAAAA";
    private static final String UPDATED_CONSUMER = "BBBBBBBBBB";

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_ID = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private WebTestClient webTestClient;

    private JobRequest jobRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobRequest createEntity() {
        JobRequest jobRequest = new JobRequest()
            .consumer(DEFAULT_CONSUMER)
            .provider(DEFAULT_PROVIDER)
            .serviceStatus(DEFAULT_SERVICE_STATUS)
            .roomId(DEFAULT_ROOM_ID);
        return jobRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobRequest createUpdatedEntity() {
        JobRequest jobRequest = new JobRequest()
            .consumer(UPDATED_CONSUMER)
            .provider(UPDATED_PROVIDER)
            .serviceStatus(UPDATED_SERVICE_STATUS)
            .roomId(UPDATED_ROOM_ID);
        return jobRequest;
    }

    @BeforeEach
    public void initTest() {
        jobRequestRepository.deleteAll().block();
        jobRequest = createEntity();
    }

    @Test
    void createJobRequest() throws Exception {
        int databaseSizeBeforeCreate = jobRequestRepository.findAll().collectList().block().size();
        // Create the JobRequest
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeCreate + 1);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(DEFAULT_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(DEFAULT_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(DEFAULT_ROOM_ID);
    }

    @Test
    void createJobRequestWithExistingId() throws Exception {
        // Create the JobRequest with an existing ID
        jobRequest.setId("existing_id");

        int databaseSizeBeforeCreate = jobRequestRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllJobRequests() {
        // Initialize the database
        jobRequestRepository.save(jobRequest).block();

        // Get all the jobRequestList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(jobRequest.getId()))
            .jsonPath("$.[*].consumer")
            .value(hasItem(DEFAULT_CONSUMER))
            .jsonPath("$.[*].provider")
            .value(hasItem(DEFAULT_PROVIDER))
            .jsonPath("$.[*].serviceStatus")
            .value(hasItem(DEFAULT_SERVICE_STATUS))
            .jsonPath("$.[*].roomId")
            .value(hasItem(DEFAULT_ROOM_ID));
    }

    @Test
    void getJobRequest() {
        // Initialize the database
        jobRequestRepository.save(jobRequest).block();

        // Get the jobRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, jobRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(jobRequest.getId()))
            .jsonPath("$.consumer")
            .value(is(DEFAULT_CONSUMER))
            .jsonPath("$.provider")
            .value(is(DEFAULT_PROVIDER))
            .jsonPath("$.serviceStatus")
            .value(is(DEFAULT_SERVICE_STATUS))
            .jsonPath("$.roomId")
            .value(is(DEFAULT_ROOM_ID));
    }

    @Test
    void getNonExistingJobRequest() {
        // Get the jobRequest
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingJobRequest() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest).block();

        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();

        // Update the jobRequest
        JobRequest updatedJobRequest = jobRequestRepository.findById(jobRequest.getId()).block();
        updatedJobRequest
            .consumer(UPDATED_CONSUMER)
            .provider(UPDATED_PROVIDER)
            .serviceStatus(UPDATED_SERVICE_STATUS)
            .roomId(UPDATED_ROOM_ID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedJobRequest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedJobRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(UPDATED_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(UPDATED_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(UPDATED_ROOM_ID);
    }

    @Test
    void putNonExistingJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, jobRequest.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJobRequestWithPatch() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest).block();

        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();

        // Update the jobRequest using partial update
        JobRequest partialUpdatedJobRequest = new JobRequest();
        partialUpdatedJobRequest.setId(jobRequest.getId());

        partialUpdatedJobRequest.provider(UPDATED_PROVIDER);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedJobRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(DEFAULT_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(DEFAULT_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(DEFAULT_ROOM_ID);
    }

    @Test
    void fullUpdateJobRequestWithPatch() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest).block();

        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();

        // Update the jobRequest using partial update
        JobRequest partialUpdatedJobRequest = new JobRequest();
        partialUpdatedJobRequest.setId(jobRequest.getId());

        partialUpdatedJobRequest
            .consumer(UPDATED_CONSUMER)
            .provider(UPDATED_PROVIDER)
            .serviceStatus(UPDATED_SERVICE_STATUS)
            .roomId(UPDATED_ROOM_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedJobRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedJobRequest))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(UPDATED_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(UPDATED_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(UPDATED_ROOM_ID);
    }

    @Test
    void patchNonExistingJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, jobRequest.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().collectList().block().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(jobRequest))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJobRequest() {
        // Initialize the database
        jobRequestRepository.save(jobRequest).block();

        int databaseSizeBeforeDelete = jobRequestRepository.findAll().collectList().block().size();

        // Delete the jobRequest
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, jobRequest.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<JobRequest> jobRequestList = jobRequestRepository.findAll().collectList().block();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
