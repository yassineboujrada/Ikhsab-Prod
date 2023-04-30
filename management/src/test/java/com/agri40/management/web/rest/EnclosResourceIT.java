package com.agri40.management.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agri40.management.IntegrationTest;
import com.agri40.management.domain.Enclos;
import com.agri40.management.repository.EnclosRepository;
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
 * Integration tests for the {@link EnclosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnclosResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_GROUP_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/enclos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private EnclosRepository enclosRepository;

    @Autowired
    private MockMvc restEnclosMockMvc;

    private Enclos enclos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enclos createEntity() {
        Enclos enclos = new Enclos().name(DEFAULT_NAME).userId(DEFAULT_USER_ID).groupId(DEFAULT_GROUP_ID);
        return enclos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enclos createUpdatedEntity() {
        Enclos enclos = new Enclos().name(UPDATED_NAME).userId(UPDATED_USER_ID).groupId(UPDATED_GROUP_ID);
        return enclos;
    }

    @BeforeEach
    public void initTest() {
        enclosRepository.deleteAll();
        enclos = createEntity();
    }

    @Test
    void createEnclos() throws Exception {
        int databaseSizeBeforeCreate = enclosRepository.findAll().size();
        // Create the Enclos
        restEnclosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enclos)))
            .andExpect(status().isCreated());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeCreate + 1);
        Enclos testEnclos = enclosList.get(enclosList.size() - 1);
        assertThat(testEnclos.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEnclos.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testEnclos.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
    }

    @Test
    void createEnclosWithExistingId() throws Exception {
        // Create the Enclos with an existing ID
        enclos.setId("existing_id");

        int databaseSizeBeforeCreate = enclosRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnclosMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enclos)))
            .andExpect(status().isBadRequest());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllEnclos() throws Exception {
        // Initialize the database
        enclos.setId(UUID.randomUUID().toString());
        enclosRepository.save(enclos);

        // Get all the enclosList
        restEnclosMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enclos.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].groupId").value(hasItem(DEFAULT_GROUP_ID)));
    }

    @Test
    void getEnclos() throws Exception {
        // Initialize the database
        enclos.setId(UUID.randomUUID().toString());
        enclosRepository.save(enclos);

        // Get the enclos
        restEnclosMockMvc
            .perform(get(ENTITY_API_URL_ID, enclos.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enclos.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.groupId").value(DEFAULT_GROUP_ID));
    }

    @Test
    void getNonExistingEnclos() throws Exception {
        // Get the enclos
        restEnclosMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingEnclos() throws Exception {
        // Initialize the database
        enclos.setId(UUID.randomUUID().toString());
        enclosRepository.save(enclos);

        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();

        // Update the enclos
        Enclos updatedEnclos = enclosRepository.findById(enclos.getId()).get();
        updatedEnclos.name(UPDATED_NAME).userId(UPDATED_USER_ID).groupId(UPDATED_GROUP_ID);

        restEnclosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnclos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnclos))
            )
            .andExpect(status().isOk());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
        Enclos testEnclos = enclosList.get(enclosList.size() - 1);
        assertThat(testEnclos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnclos.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEnclos.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
    }

    @Test
    void putNonExistingEnclos() throws Exception {
        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();
        enclos.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnclosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enclos.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enclos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchEnclos() throws Exception {
        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();
        enclos.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnclosMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(enclos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamEnclos() throws Exception {
        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();
        enclos.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnclosMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(enclos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateEnclosWithPatch() throws Exception {
        // Initialize the database
        enclos.setId(UUID.randomUUID().toString());
        enclosRepository.save(enclos);

        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();

        // Update the enclos using partial update
        Enclos partialUpdatedEnclos = new Enclos();
        partialUpdatedEnclos.setId(enclos.getId());

        partialUpdatedEnclos.name(UPDATED_NAME).userId(UPDATED_USER_ID);

        restEnclosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnclos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnclos))
            )
            .andExpect(status().isOk());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
        Enclos testEnclos = enclosList.get(enclosList.size() - 1);
        assertThat(testEnclos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnclos.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEnclos.getGroupId()).isEqualTo(DEFAULT_GROUP_ID);
    }

    @Test
    void fullUpdateEnclosWithPatch() throws Exception {
        // Initialize the database
        enclos.setId(UUID.randomUUID().toString());
        enclosRepository.save(enclos);

        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();

        // Update the enclos using partial update
        Enclos partialUpdatedEnclos = new Enclos();
        partialUpdatedEnclos.setId(enclos.getId());

        partialUpdatedEnclos.name(UPDATED_NAME).userId(UPDATED_USER_ID).groupId(UPDATED_GROUP_ID);

        restEnclosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnclos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnclos))
            )
            .andExpect(status().isOk());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
        Enclos testEnclos = enclosList.get(enclosList.size() - 1);
        assertThat(testEnclos.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEnclos.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testEnclos.getGroupId()).isEqualTo(UPDATED_GROUP_ID);
    }

    @Test
    void patchNonExistingEnclos() throws Exception {
        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();
        enclos.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnclosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enclos.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enclos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchEnclos() throws Exception {
        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();
        enclos.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnclosMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(enclos))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamEnclos() throws Exception {
        int databaseSizeBeforeUpdate = enclosRepository.findAll().size();
        enclos.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnclosMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(enclos)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enclos in the database
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteEnclos() throws Exception {
        // Initialize the database
        enclos.setId(UUID.randomUUID().toString());
        enclosRepository.save(enclos);

        int databaseSizeBeforeDelete = enclosRepository.findAll().size();

        // Delete the enclos
        restEnclosMockMvc
            .perform(delete(ENTITY_API_URL_ID, enclos.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Enclos> enclosList = enclosRepository.findAll();
        assertThat(enclosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
