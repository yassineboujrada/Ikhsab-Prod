package com.agri40.sensoring.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agri40.sensoring.IntegrationTest;
import com.agri40.sensoring.domain.Sante;
import com.agri40.sensoring.repository.SanteRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link SanteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SanteResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DUREE_POSITION_COUCHEE = "AAAAAAAAAA";
    private static final String UPDATED_DUREE_POSITION_COUCHEE = "BBBBBBBBBB";

    private static final String DEFAULT_LEVE = "AAAAAAAAAA";
    private static final String UPDATED_LEVE = "BBBBBBBBBB";

    private static final String DEFAULT_PAS = "AAAAAAAAAA";
    private static final String UPDATED_PAS = "BBBBBBBBBB";

    private static final String DEFAULT_COW_ID = "AAAAAAAAAA";
    private static final String UPDATED_COW_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/santes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SanteRepository santeRepository;

    @Autowired
    private MockMvc restSanteMockMvc;

    private Sante sante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sante createEntity() {
        Sante sante = new Sante()
            .date(DEFAULT_DATE)
            .dureePositionCouchee(DEFAULT_DUREE_POSITION_COUCHEE)
            .leve(DEFAULT_LEVE)
            .pas(DEFAULT_PAS)
            .cowId(DEFAULT_COW_ID);
        return sante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sante createUpdatedEntity() {
        Sante sante = new Sante()
            .date(UPDATED_DATE)
            .dureePositionCouchee(UPDATED_DUREE_POSITION_COUCHEE)
            .leve(UPDATED_LEVE)
            .pas(UPDATED_PAS)
            .cowId(UPDATED_COW_ID);
        return sante;
    }

    @BeforeEach
    public void initTest() {
        santeRepository.deleteAll();
        sante = createEntity();
    }

    @Test
    void createSante() throws Exception {
        int databaseSizeBeforeCreate = santeRepository.findAll().size();
        // Create the Sante
        restSanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sante)))
            .andExpect(status().isCreated());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeCreate + 1);
        Sante testSante = santeList.get(santeList.size() - 1);
        assertThat(testSante.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSante.getDureePositionCouchee()).isEqualTo(DEFAULT_DUREE_POSITION_COUCHEE);
        assertThat(testSante.getLeve()).isEqualTo(DEFAULT_LEVE);
        assertThat(testSante.getPas()).isEqualTo(DEFAULT_PAS);
        assertThat(testSante.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void createSanteWithExistingId() throws Exception {
        // Create the Sante with an existing ID
        sante.setId("existing_id");

        int databaseSizeBeforeCreate = santeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sante)))
            .andExpect(status().isBadRequest());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSantes() throws Exception {
        // Initialize the database
        santeRepository.save(sante);

        // Get all the santeList
        restSanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sante.getId())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].dureePositionCouchee").value(hasItem(DEFAULT_DUREE_POSITION_COUCHEE)))
            .andExpect(jsonPath("$.[*].leve").value(hasItem(DEFAULT_LEVE)))
            .andExpect(jsonPath("$.[*].pas").value(hasItem(DEFAULT_PAS)))
            .andExpect(jsonPath("$.[*].cowId").value(hasItem(DEFAULT_COW_ID)));
    }

    @Test
    void getSante() throws Exception {
        // Initialize the database
        santeRepository.save(sante);

        // Get the sante
        restSanteMockMvc
            .perform(get(ENTITY_API_URL_ID, sante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sante.getId()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.dureePositionCouchee").value(DEFAULT_DUREE_POSITION_COUCHEE))
            .andExpect(jsonPath("$.leve").value(DEFAULT_LEVE))
            .andExpect(jsonPath("$.pas").value(DEFAULT_PAS))
            .andExpect(jsonPath("$.cowId").value(DEFAULT_COW_ID));
    }

    @Test
    void getNonExistingSante() throws Exception {
        // Get the sante
        restSanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingSante() throws Exception {
        // Initialize the database
        santeRepository.save(sante);

        int databaseSizeBeforeUpdate = santeRepository.findAll().size();

        // Update the sante
        Sante updatedSante = santeRepository.findById(sante.getId()).orElseThrow();
        updatedSante
            .date(UPDATED_DATE)
            .dureePositionCouchee(UPDATED_DUREE_POSITION_COUCHEE)
            .leve(UPDATED_LEVE)
            .pas(UPDATED_PAS)
            .cowId(UPDATED_COW_ID);

        restSanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSante))
            )
            .andExpect(status().isOk());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
        Sante testSante = santeList.get(santeList.size() - 1);
        assertThat(testSante.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSante.getDureePositionCouchee()).isEqualTo(UPDATED_DUREE_POSITION_COUCHEE);
        assertThat(testSante.getLeve()).isEqualTo(UPDATED_LEVE);
        assertThat(testSante.getPas()).isEqualTo(UPDATED_PAS);
        assertThat(testSante.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void putNonExistingSante() throws Exception {
        int databaseSizeBeforeUpdate = santeRepository.findAll().size();
        sante.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSante() throws Exception {
        int databaseSizeBeforeUpdate = santeRepository.findAll().size();
        sante.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSante() throws Exception {
        int databaseSizeBeforeUpdate = santeRepository.findAll().size();
        sante.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(sante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSanteWithPatch() throws Exception {
        // Initialize the database
        santeRepository.save(sante);

        int databaseSizeBeforeUpdate = santeRepository.findAll().size();

        // Update the sante using partial update
        Sante partialUpdatedSante = new Sante();
        partialUpdatedSante.setId(sante.getId());

        partialUpdatedSante.leve(UPDATED_LEVE).pas(UPDATED_PAS);

        restSanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSante))
            )
            .andExpect(status().isOk());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
        Sante testSante = santeList.get(santeList.size() - 1);
        assertThat(testSante.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testSante.getDureePositionCouchee()).isEqualTo(DEFAULT_DUREE_POSITION_COUCHEE);
        assertThat(testSante.getLeve()).isEqualTo(UPDATED_LEVE);
        assertThat(testSante.getPas()).isEqualTo(UPDATED_PAS);
        assertThat(testSante.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void fullUpdateSanteWithPatch() throws Exception {
        // Initialize the database
        santeRepository.save(sante);

        int databaseSizeBeforeUpdate = santeRepository.findAll().size();

        // Update the sante using partial update
        Sante partialUpdatedSante = new Sante();
        partialUpdatedSante.setId(sante.getId());

        partialUpdatedSante
            .date(UPDATED_DATE)
            .dureePositionCouchee(UPDATED_DUREE_POSITION_COUCHEE)
            .leve(UPDATED_LEVE)
            .pas(UPDATED_PAS)
            .cowId(UPDATED_COW_ID);

        restSanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSante))
            )
            .andExpect(status().isOk());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
        Sante testSante = santeList.get(santeList.size() - 1);
        assertThat(testSante.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testSante.getDureePositionCouchee()).isEqualTo(UPDATED_DUREE_POSITION_COUCHEE);
        assertThat(testSante.getLeve()).isEqualTo(UPDATED_LEVE);
        assertThat(testSante.getPas()).isEqualTo(UPDATED_PAS);
        assertThat(testSante.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void patchNonExistingSante() throws Exception {
        int databaseSizeBeforeUpdate = santeRepository.findAll().size();
        sante.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSante() throws Exception {
        int databaseSizeBeforeUpdate = santeRepository.findAll().size();
        sante.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSante() throws Exception {
        int databaseSizeBeforeUpdate = santeRepository.findAll().size();
        sante.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSanteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(sante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Sante in the database
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSante() throws Exception {
        // Initialize the database
        santeRepository.save(sante);

        int databaseSizeBeforeDelete = santeRepository.findAll().size();

        // Delete the sante
        restSanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, sante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sante> santeList = santeRepository.findAll();
        assertThat(santeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
