package com.agri40.management.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agri40.management.IntegrationTest;
import com.agri40.management.domain.Cow;
import com.agri40.management.repository.CowRepository;
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
 * Integration tests for the {@link CowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CowResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_GROUPE_ID = "AAAAAAAAAA";
    private static final String UPDATED_GROUPE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ENCLOS_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENCLOS_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REPONDEUR = "AAAAAAAAAA";
    private static final String UPDATED_REPONDEUR = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WAITING_FOR_INSEMINATOR = false;
    private static final Boolean UPDATED_WAITING_FOR_INSEMINATOR = true;

    private static final String DEFAULT_RFID = "AAAAAAAAAA";
    private static final String UPDATED_RFID = "BBBBBBBBBB";

    private static final String DEFAULT_PEDOMETRE = "AAAAAAAAAA";
    private static final String UPDATED_PEDOMETRE = "BBBBBBBBBB";

    private static final String DEFAULT_COLLAR = "AAAAAAAAAA";
    private static final String UPDATED_COLLAR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CowRepository cowRepository;

    @Autowired
    private MockMvc restCowMockMvc;

    private Cow cow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cow createEntity() {
        Cow cow = new Cow()
            .numero(DEFAULT_NUMERO)
            .groupeId(DEFAULT_GROUPE_ID)
            .enclosId(DEFAULT_ENCLOS_ID)
            .repondeur(DEFAULT_REPONDEUR)
            .nom(DEFAULT_NOM)
            .userId(DEFAULT_USER_ID)
            .waitingForInseminator(DEFAULT_WAITING_FOR_INSEMINATOR)
            .rfid(DEFAULT_RFID)
            .pedometre(DEFAULT_PEDOMETRE)
            .collar(DEFAULT_COLLAR);
        return cow;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cow createUpdatedEntity() {
        Cow cow = new Cow()
            .numero(UPDATED_NUMERO)
            .groupeId(UPDATED_GROUPE_ID)
            .enclosId(UPDATED_ENCLOS_ID)
            .repondeur(UPDATED_REPONDEUR)
            .nom(UPDATED_NOM)
            .userId(UPDATED_USER_ID)
            .waitingForInseminator(UPDATED_WAITING_FOR_INSEMINATOR)
            .rfid(UPDATED_RFID)
            .pedometre(UPDATED_PEDOMETRE)
            .collar(UPDATED_COLLAR);
        return cow;
    }

    @BeforeEach
    public void initTest() {
        cowRepository.deleteAll();
        cow = createEntity();
    }

    @Test
    void createCow() throws Exception {
        int databaseSizeBeforeCreate = cowRepository.findAll().size();
        // Create the Cow
        restCowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cow)))
            .andExpect(status().isCreated());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeCreate + 1);
        Cow testCow = cowList.get(cowList.size() - 1);
        assertThat(testCow.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCow.getGroupeId()).isEqualTo(DEFAULT_GROUPE_ID);
        assertThat(testCow.getEnclosId()).isEqualTo(DEFAULT_ENCLOS_ID);
        assertThat(testCow.getRepondeur()).isEqualTo(DEFAULT_REPONDEUR);
        assertThat(testCow.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCow.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testCow.getWaitingForInseminator()).isEqualTo(DEFAULT_WAITING_FOR_INSEMINATOR);
        assertThat(testCow.getRfid()).isEqualTo(DEFAULT_RFID);
        assertThat(testCow.getPedometre()).isEqualTo(DEFAULT_PEDOMETRE);
        assertThat(testCow.getCollar()).isEqualTo(DEFAULT_COLLAR);
    }

    @Test
    void createCowWithExistingId() throws Exception {
        // Create the Cow with an existing ID
        cow.setId("existing_id");

        int databaseSizeBeforeCreate = cowRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cow)))
            .andExpect(status().isBadRequest());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCows() throws Exception {
        // Initialize the database
        cowRepository.save(cow);

        // Get all the cowList
        restCowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cow.getId())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].groupeId").value(hasItem(DEFAULT_GROUPE_ID)))
            .andExpect(jsonPath("$.[*].enclosId").value(hasItem(DEFAULT_ENCLOS_ID)))
            .andExpect(jsonPath("$.[*].repondeur").value(hasItem(DEFAULT_REPONDEUR)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].waitingForInseminator").value(hasItem(DEFAULT_WAITING_FOR_INSEMINATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].rfid").value(hasItem(DEFAULT_RFID)))
            .andExpect(jsonPath("$.[*].pedometre").value(hasItem(DEFAULT_PEDOMETRE)))
            .andExpect(jsonPath("$.[*].collar").value(hasItem(DEFAULT_COLLAR)));
    }

    @Test
    void getCow() throws Exception {
        // Initialize the database
        cowRepository.save(cow);

        // Get the cow
        restCowMockMvc
            .perform(get(ENTITY_API_URL_ID, cow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cow.getId()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.groupeId").value(DEFAULT_GROUPE_ID))
            .andExpect(jsonPath("$.enclosId").value(DEFAULT_ENCLOS_ID))
            .andExpect(jsonPath("$.repondeur").value(DEFAULT_REPONDEUR))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.waitingForInseminator").value(DEFAULT_WAITING_FOR_INSEMINATOR.booleanValue()))
            .andExpect(jsonPath("$.rfid").value(DEFAULT_RFID))
            .andExpect(jsonPath("$.pedometre").value(DEFAULT_PEDOMETRE))
            .andExpect(jsonPath("$.collar").value(DEFAULT_COLLAR));
    }

    @Test
    void getNonExistingCow() throws Exception {
        // Get the cow
        restCowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCow() throws Exception {
        // Initialize the database
        cowRepository.save(cow);

        int databaseSizeBeforeUpdate = cowRepository.findAll().size();

        // Update the cow
        Cow updatedCow = cowRepository.findById(cow.getId()).orElseThrow();
        updatedCow
            .numero(UPDATED_NUMERO)
            .groupeId(UPDATED_GROUPE_ID)
            .enclosId(UPDATED_ENCLOS_ID)
            .repondeur(UPDATED_REPONDEUR)
            .nom(UPDATED_NOM)
            .userId(UPDATED_USER_ID)
            .waitingForInseminator(UPDATED_WAITING_FOR_INSEMINATOR)
            .rfid(UPDATED_RFID)
            .pedometre(UPDATED_PEDOMETRE)
            .collar(UPDATED_COLLAR);

        restCowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCow.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCow))
            )
            .andExpect(status().isOk());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
        Cow testCow = cowList.get(cowList.size() - 1);
        assertThat(testCow.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCow.getGroupeId()).isEqualTo(UPDATED_GROUPE_ID);
        assertThat(testCow.getEnclosId()).isEqualTo(UPDATED_ENCLOS_ID);
        assertThat(testCow.getRepondeur()).isEqualTo(UPDATED_REPONDEUR);
        assertThat(testCow.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCow.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCow.getWaitingForInseminator()).isEqualTo(UPDATED_WAITING_FOR_INSEMINATOR);
        assertThat(testCow.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testCow.getPedometre()).isEqualTo(UPDATED_PEDOMETRE);
        assertThat(testCow.getCollar()).isEqualTo(UPDATED_COLLAR);
    }

    @Test
    void putNonExistingCow() throws Exception {
        int databaseSizeBeforeUpdate = cowRepository.findAll().size();
        cow.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cow.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCow() throws Exception {
        int databaseSizeBeforeUpdate = cowRepository.findAll().size();
        cow.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCow() throws Exception {
        int databaseSizeBeforeUpdate = cowRepository.findAll().size();
        cow.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCowWithPatch() throws Exception {
        // Initialize the database
        cowRepository.save(cow);

        int databaseSizeBeforeUpdate = cowRepository.findAll().size();

        // Update the cow using partial update
        Cow partialUpdatedCow = new Cow();
        partialUpdatedCow.setId(cow.getId());

        partialUpdatedCow
            .enclosId(UPDATED_ENCLOS_ID)
            .repondeur(UPDATED_REPONDEUR)
            .nom(UPDATED_NOM)
            .userId(UPDATED_USER_ID)
            .rfid(UPDATED_RFID)
            .pedometre(UPDATED_PEDOMETRE)
            .collar(UPDATED_COLLAR);

        restCowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCow))
            )
            .andExpect(status().isOk());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
        Cow testCow = cowList.get(cowList.size() - 1);
        assertThat(testCow.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testCow.getGroupeId()).isEqualTo(DEFAULT_GROUPE_ID);
        assertThat(testCow.getEnclosId()).isEqualTo(UPDATED_ENCLOS_ID);
        assertThat(testCow.getRepondeur()).isEqualTo(UPDATED_REPONDEUR);
        assertThat(testCow.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCow.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCow.getWaitingForInseminator()).isEqualTo(DEFAULT_WAITING_FOR_INSEMINATOR);
        assertThat(testCow.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testCow.getPedometre()).isEqualTo(UPDATED_PEDOMETRE);
        assertThat(testCow.getCollar()).isEqualTo(UPDATED_COLLAR);
    }

    @Test
    void fullUpdateCowWithPatch() throws Exception {
        // Initialize the database
        cowRepository.save(cow);

        int databaseSizeBeforeUpdate = cowRepository.findAll().size();

        // Update the cow using partial update
        Cow partialUpdatedCow = new Cow();
        partialUpdatedCow.setId(cow.getId());

        partialUpdatedCow
            .numero(UPDATED_NUMERO)
            .groupeId(UPDATED_GROUPE_ID)
            .enclosId(UPDATED_ENCLOS_ID)
            .repondeur(UPDATED_REPONDEUR)
            .nom(UPDATED_NOM)
            .userId(UPDATED_USER_ID)
            .waitingForInseminator(UPDATED_WAITING_FOR_INSEMINATOR)
            .rfid(UPDATED_RFID)
            .pedometre(UPDATED_PEDOMETRE)
            .collar(UPDATED_COLLAR);

        restCowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCow))
            )
            .andExpect(status().isOk());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
        Cow testCow = cowList.get(cowList.size() - 1);
        assertThat(testCow.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testCow.getGroupeId()).isEqualTo(UPDATED_GROUPE_ID);
        assertThat(testCow.getEnclosId()).isEqualTo(UPDATED_ENCLOS_ID);
        assertThat(testCow.getRepondeur()).isEqualTo(UPDATED_REPONDEUR);
        assertThat(testCow.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCow.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testCow.getWaitingForInseminator()).isEqualTo(UPDATED_WAITING_FOR_INSEMINATOR);
        assertThat(testCow.getRfid()).isEqualTo(UPDATED_RFID);
        assertThat(testCow.getPedometre()).isEqualTo(UPDATED_PEDOMETRE);
        assertThat(testCow.getCollar()).isEqualTo(UPDATED_COLLAR);
    }

    @Test
    void patchNonExistingCow() throws Exception {
        int databaseSizeBeforeUpdate = cowRepository.findAll().size();
        cow.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cow.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCow() throws Exception {
        int databaseSizeBeforeUpdate = cowRepository.findAll().size();
        cow.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cow))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCow() throws Exception {
        int databaseSizeBeforeUpdate = cowRepository.findAll().size();
        cow.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cow)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cow in the database
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCow() throws Exception {
        // Initialize the database
        cowRepository.save(cow);

        int databaseSizeBeforeDelete = cowRepository.findAll().size();

        // Delete the cow
        restCowMockMvc.perform(delete(ENTITY_API_URL_ID, cow.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cow> cowList = cowRepository.findAll();
        assertThat(cowList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
