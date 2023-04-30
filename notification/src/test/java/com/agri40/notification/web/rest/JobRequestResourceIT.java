package com.agri40.notification.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agri40.notification.IntegrationTest;
import com.agri40.notification.domain.JobRequest;
import com.agri40.notification.repository.JobRequestRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link JobRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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

    private static final String DEFAULT_COW_ID = "AAAAAAAAAA";
    private static final String UPDATED_COW_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private MockMvc restJobRequestMockMvc;

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
            .roomId(DEFAULT_ROOM_ID)
            .cowId(DEFAULT_COW_ID);
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
            .roomId(UPDATED_ROOM_ID)
            .cowId(UPDATED_COW_ID);
        return jobRequest;
    }

    @BeforeEach
    public void initTest() {
        jobRequestRepository.deleteAll();
        jobRequest = createEntity();
    }

    @Test
    void createJobRequest() throws Exception {
        int databaseSizeBeforeCreate = jobRequestRepository.findAll().size();
        // Create the JobRequest
        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRequest)))
            .andExpect(status().isCreated());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeCreate + 1);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(DEFAULT_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(DEFAULT_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(DEFAULT_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(DEFAULT_ROOM_ID);
        assertThat(testJobRequest.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void createJobRequestWithExistingId() throws Exception {
        // Create the JobRequest with an existing ID
        jobRequest.setId("existing_id");

        int databaseSizeBeforeCreate = jobRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRequest)))
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllJobRequests() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest);

        // Get all the jobRequestList
        restJobRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobRequest.getId())))
            .andExpect(jsonPath("$.[*].consumer").value(hasItem(DEFAULT_CONSUMER)))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].serviceStatus").value(hasItem(DEFAULT_SERVICE_STATUS)))
            .andExpect(jsonPath("$.[*].roomId").value(hasItem(DEFAULT_ROOM_ID)))
            .andExpect(jsonPath("$.[*].cowId").value(hasItem(DEFAULT_COW_ID)));
    }

    @Test
    void getJobRequest() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest);

        // Get the jobRequest
        restJobRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, jobRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobRequest.getId()))
            .andExpect(jsonPath("$.consumer").value(DEFAULT_CONSUMER))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER))
            .andExpect(jsonPath("$.serviceStatus").value(DEFAULT_SERVICE_STATUS))
            .andExpect(jsonPath("$.roomId").value(DEFAULT_ROOM_ID))
            .andExpect(jsonPath("$.cowId").value(DEFAULT_COW_ID));
    }

    @Test
    void getNonExistingJobRequest() throws Exception {
        // Get the jobRequest
        restJobRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingJobRequest() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest);

        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();

        // Update the jobRequest
        JobRequest updatedJobRequest = jobRequestRepository.findById(jobRequest.getId()).get();
        updatedJobRequest
            .consumer(UPDATED_CONSUMER)
            .provider(UPDATED_PROVIDER)
            .serviceStatus(UPDATED_SERVICE_STATUS)
            .roomId(UPDATED_ROOM_ID)
            .cowId(UPDATED_COW_ID);

        restJobRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobRequest))
            )
            .andExpect(status().isOk());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(UPDATED_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(UPDATED_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(UPDATED_ROOM_ID);
        assertThat(testJobRequest.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void putNonExistingJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateJobRequestWithPatch() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest);

        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();

        // Update the jobRequest using partial update
        JobRequest partialUpdatedJobRequest = new JobRequest();
        partialUpdatedJobRequest.setId(jobRequest.getId());

        partialUpdatedJobRequest.provider(UPDATED_PROVIDER);

        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobRequest))
            )
            .andExpect(status().isOk());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(DEFAULT_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(DEFAULT_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(DEFAULT_ROOM_ID);
        assertThat(testJobRequest.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void fullUpdateJobRequestWithPatch() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest);

        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();

        // Update the jobRequest using partial update
        JobRequest partialUpdatedJobRequest = new JobRequest();
        partialUpdatedJobRequest.setId(jobRequest.getId());

        partialUpdatedJobRequest
            .consumer(UPDATED_CONSUMER)
            .provider(UPDATED_PROVIDER)
            .serviceStatus(UPDATED_SERVICE_STATUS)
            .roomId(UPDATED_ROOM_ID)
            .cowId(UPDATED_COW_ID);

        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobRequest))
            )
            .andExpect(status().isOk());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
        JobRequest testJobRequest = jobRequestList.get(jobRequestList.size() - 1);
        assertThat(testJobRequest.getConsumer()).isEqualTo(UPDATED_CONSUMER);
        assertThat(testJobRequest.getProvider()).isEqualTo(UPDATED_PROVIDER);
        assertThat(testJobRequest.getServiceStatus()).isEqualTo(UPDATED_SERVICE_STATUS);
        assertThat(testJobRequest.getRoomId()).isEqualTo(UPDATED_ROOM_ID);
        assertThat(testJobRequest.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void patchNonExistingJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamJobRequest() throws Exception {
        int databaseSizeBeforeUpdate = jobRequestRepository.findAll().size();
        jobRequest.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobRequestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobRequest in the database
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteJobRequest() throws Exception {
        // Initialize the database
        jobRequestRepository.save(jobRequest);

        int databaseSizeBeforeDelete = jobRequestRepository.findAll().size();

        // Delete the jobRequest
        restJobRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobRequest> jobRequestList = jobRequestRepository.findAll();
        assertThat(jobRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
