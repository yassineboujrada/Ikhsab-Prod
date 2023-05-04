package com.agri40.sensoring.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agri40.sensoring.IntegrationTest;
import com.agri40.sensoring.domain.Chaleurs;
import com.agri40.sensoring.repository.ChaleursRepository;
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
 * Integration tests for the {@link ChaleursResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChaleursResourceIT {

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_JRS_LACT = "AAAAAAAAAA";
    private static final String UPDATED_JRS_LACT = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPS = "AAAAAAAAAA";
    private static final String UPDATED_TEMPS = "BBBBBBBBBB";

    private static final String DEFAULT_GROUPEID = "AAAAAAAAAA";
    private static final String UPDATED_GROUPEID = "BBBBBBBBBB";

    private static final String DEFAULT_ENCLOSID = "AAAAAAAAAA";
    private static final String UPDATED_ENCLOSID = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_FACTEUR_ELEVE = "AAAAAAAAAA";
    private static final String UPDATED_FACTEUR_ELEVE = "BBBBBBBBBB";

    private static final String DEFAULT_SUSPECT = "AAAAAAAAAA";
    private static final String UPDATED_SUSPECT = "BBBBBBBBBB";

    private static final String DEFAULT_ACT_AUGMENTEE = "AAAAAAAAAA";
    private static final String UPDATED_ACT_AUGMENTEE = "BBBBBBBBBB";

    private static final String DEFAULT_ALARME_CHALEUR = "AAAAAAAAAA";
    private static final String UPDATED_ALARME_CHALEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PAS_DE_CHALEUR = "AAAAAAAAAA";
    private static final String UPDATED_PAS_DE_CHALEUR = "BBBBBBBBBB";

    private static final String DEFAULT_COW_ID = "AAAAAAAAAA";
    private static final String UPDATED_COW_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chaleurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ChaleursRepository chaleursRepository;

    @Autowired
    private MockMvc restChaleursMockMvc;

    private Chaleurs chaleurs;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chaleurs createEntity() {
        Chaleurs chaleurs = new Chaleurs()
            .date(DEFAULT_DATE)
            .jrsLact(DEFAULT_JRS_LACT)
            .temps(DEFAULT_TEMPS)
            .groupeid(DEFAULT_GROUPEID)
            .enclosid(DEFAULT_ENCLOSID)
            .activite(DEFAULT_ACTIVITE)
            .facteurEleve(DEFAULT_FACTEUR_ELEVE)
            .suspect(DEFAULT_SUSPECT)
            .actAugmentee(DEFAULT_ACT_AUGMENTEE)
            .alarmeChaleur(DEFAULT_ALARME_CHALEUR)
            .pasDeChaleur(DEFAULT_PAS_DE_CHALEUR)
            .cowId(DEFAULT_COW_ID);
        return chaleurs;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chaleurs createUpdatedEntity() {
        Chaleurs chaleurs = new Chaleurs()
            .date(UPDATED_DATE)
            .jrsLact(UPDATED_JRS_LACT)
            .temps(UPDATED_TEMPS)
            .groupeid(UPDATED_GROUPEID)
            .enclosid(UPDATED_ENCLOSID)
            .activite(UPDATED_ACTIVITE)
            .facteurEleve(UPDATED_FACTEUR_ELEVE)
            .suspect(UPDATED_SUSPECT)
            .actAugmentee(UPDATED_ACT_AUGMENTEE)
            .alarmeChaleur(UPDATED_ALARME_CHALEUR)
            .pasDeChaleur(UPDATED_PAS_DE_CHALEUR)
            .cowId(UPDATED_COW_ID);
        return chaleurs;
    }

    @BeforeEach
    public void initTest() {
        chaleursRepository.deleteAll();
        chaleurs = createEntity();
    }

    @Test
    void createChaleurs() throws Exception {
        int databaseSizeBeforeCreate = chaleursRepository.findAll().size();
        // Create the Chaleurs
        restChaleursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chaleurs)))
            .andExpect(status().isCreated());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeCreate + 1);
        Chaleurs testChaleurs = chaleursList.get(chaleursList.size() - 1);
        assertThat(testChaleurs.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testChaleurs.getJrsLact()).isEqualTo(DEFAULT_JRS_LACT);
        assertThat(testChaleurs.getTemps()).isEqualTo(DEFAULT_TEMPS);
        assertThat(testChaleurs.getGroupeid()).isEqualTo(DEFAULT_GROUPEID);
        assertThat(testChaleurs.getEnclosid()).isEqualTo(DEFAULT_ENCLOSID);
        assertThat(testChaleurs.getActivite()).isEqualTo(DEFAULT_ACTIVITE);
        assertThat(testChaleurs.getFacteurEleve()).isEqualTo(DEFAULT_FACTEUR_ELEVE);
        assertThat(testChaleurs.getSuspect()).isEqualTo(DEFAULT_SUSPECT);
        assertThat(testChaleurs.getActAugmentee()).isEqualTo(DEFAULT_ACT_AUGMENTEE);
        assertThat(testChaleurs.getAlarmeChaleur()).isEqualTo(DEFAULT_ALARME_CHALEUR);
        assertThat(testChaleurs.getPasDeChaleur()).isEqualTo(DEFAULT_PAS_DE_CHALEUR);
        assertThat(testChaleurs.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void createChaleursWithExistingId() throws Exception {
        // Create the Chaleurs with an existing ID
        chaleurs.setId("existing_id");

        int databaseSizeBeforeCreate = chaleursRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChaleursMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chaleurs)))
            .andExpect(status().isBadRequest());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllChaleurs() throws Exception {
        // Initialize the database
        chaleursRepository.save(chaleurs);

        // Get all the chaleursList
        restChaleursMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chaleurs.getId())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].jrsLact").value(hasItem(DEFAULT_JRS_LACT)))
            .andExpect(jsonPath("$.[*].temps").value(hasItem(DEFAULT_TEMPS)))
            .andExpect(jsonPath("$.[*].groupeid").value(hasItem(DEFAULT_GROUPEID)))
            .andExpect(jsonPath("$.[*].enclosid").value(hasItem(DEFAULT_ENCLOSID)))
            .andExpect(jsonPath("$.[*].activite").value(hasItem(DEFAULT_ACTIVITE)))
            .andExpect(jsonPath("$.[*].facteurEleve").value(hasItem(DEFAULT_FACTEUR_ELEVE)))
            .andExpect(jsonPath("$.[*].suspect").value(hasItem(DEFAULT_SUSPECT)))
            .andExpect(jsonPath("$.[*].actAugmentee").value(hasItem(DEFAULT_ACT_AUGMENTEE)))
            .andExpect(jsonPath("$.[*].alarmeChaleur").value(hasItem(DEFAULT_ALARME_CHALEUR)))
            .andExpect(jsonPath("$.[*].pasDeChaleur").value(hasItem(DEFAULT_PAS_DE_CHALEUR)))
            .andExpect(jsonPath("$.[*].cowId").value(hasItem(DEFAULT_COW_ID)));
    }

    @Test
    void getChaleurs() throws Exception {
        // Initialize the database
        chaleursRepository.save(chaleurs);

        // Get the chaleurs
        restChaleursMockMvc
            .perform(get(ENTITY_API_URL_ID, chaleurs.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chaleurs.getId()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.jrsLact").value(DEFAULT_JRS_LACT))
            .andExpect(jsonPath("$.temps").value(DEFAULT_TEMPS))
            .andExpect(jsonPath("$.groupeid").value(DEFAULT_GROUPEID))
            .andExpect(jsonPath("$.enclosid").value(DEFAULT_ENCLOSID))
            .andExpect(jsonPath("$.activite").value(DEFAULT_ACTIVITE))
            .andExpect(jsonPath("$.facteurEleve").value(DEFAULT_FACTEUR_ELEVE))
            .andExpect(jsonPath("$.suspect").value(DEFAULT_SUSPECT))
            .andExpect(jsonPath("$.actAugmentee").value(DEFAULT_ACT_AUGMENTEE))
            .andExpect(jsonPath("$.alarmeChaleur").value(DEFAULT_ALARME_CHALEUR))
            .andExpect(jsonPath("$.pasDeChaleur").value(DEFAULT_PAS_DE_CHALEUR))
            .andExpect(jsonPath("$.cowId").value(DEFAULT_COW_ID));
    }

    @Test
    void getNonExistingChaleurs() throws Exception {
        // Get the chaleurs
        restChaleursMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingChaleurs() throws Exception {
        // Initialize the database
        chaleursRepository.save(chaleurs);

        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();

        // Update the chaleurs
        Chaleurs updatedChaleurs = chaleursRepository.findById(chaleurs.getId()).orElseThrow();
        updatedChaleurs
            .date(UPDATED_DATE)
            .jrsLact(UPDATED_JRS_LACT)
            .temps(UPDATED_TEMPS)
            .groupeid(UPDATED_GROUPEID)
            .enclosid(UPDATED_ENCLOSID)
            .activite(UPDATED_ACTIVITE)
            .facteurEleve(UPDATED_FACTEUR_ELEVE)
            .suspect(UPDATED_SUSPECT)
            .actAugmentee(UPDATED_ACT_AUGMENTEE)
            .alarmeChaleur(UPDATED_ALARME_CHALEUR)
            .pasDeChaleur(UPDATED_PAS_DE_CHALEUR)
            .cowId(UPDATED_COW_ID);

        restChaleursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChaleurs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedChaleurs))
            )
            .andExpect(status().isOk());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
        Chaleurs testChaleurs = chaleursList.get(chaleursList.size() - 1);
        assertThat(testChaleurs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChaleurs.getJrsLact()).isEqualTo(UPDATED_JRS_LACT);
        assertThat(testChaleurs.getTemps()).isEqualTo(UPDATED_TEMPS);
        assertThat(testChaleurs.getGroupeid()).isEqualTo(UPDATED_GROUPEID);
        assertThat(testChaleurs.getEnclosid()).isEqualTo(UPDATED_ENCLOSID);
        assertThat(testChaleurs.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testChaleurs.getFacteurEleve()).isEqualTo(UPDATED_FACTEUR_ELEVE);
        assertThat(testChaleurs.getSuspect()).isEqualTo(UPDATED_SUSPECT);
        assertThat(testChaleurs.getActAugmentee()).isEqualTo(UPDATED_ACT_AUGMENTEE);
        assertThat(testChaleurs.getAlarmeChaleur()).isEqualTo(UPDATED_ALARME_CHALEUR);
        assertThat(testChaleurs.getPasDeChaleur()).isEqualTo(UPDATED_PAS_DE_CHALEUR);
        assertThat(testChaleurs.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void putNonExistingChaleurs() throws Exception {
        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();
        chaleurs.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChaleursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, chaleurs.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chaleurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchChaleurs() throws Exception {
        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();
        chaleurs.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChaleursMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(chaleurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamChaleurs() throws Exception {
        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();
        chaleurs.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChaleursMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(chaleurs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateChaleursWithPatch() throws Exception {
        // Initialize the database
        chaleursRepository.save(chaleurs);

        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();

        // Update the chaleurs using partial update
        Chaleurs partialUpdatedChaleurs = new Chaleurs();
        partialUpdatedChaleurs.setId(chaleurs.getId());

        partialUpdatedChaleurs
            .groupeid(UPDATED_GROUPEID)
            .activite(UPDATED_ACTIVITE)
            .facteurEleve(UPDATED_FACTEUR_ELEVE)
            .suspect(UPDATED_SUSPECT)
            .actAugmentee(UPDATED_ACT_AUGMENTEE)
            .pasDeChaleur(UPDATED_PAS_DE_CHALEUR)
            .cowId(UPDATED_COW_ID);

        restChaleursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChaleurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChaleurs))
            )
            .andExpect(status().isOk());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
        Chaleurs testChaleurs = chaleursList.get(chaleursList.size() - 1);
        assertThat(testChaleurs.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testChaleurs.getJrsLact()).isEqualTo(DEFAULT_JRS_LACT);
        assertThat(testChaleurs.getTemps()).isEqualTo(DEFAULT_TEMPS);
        assertThat(testChaleurs.getGroupeid()).isEqualTo(UPDATED_GROUPEID);
        assertThat(testChaleurs.getEnclosid()).isEqualTo(DEFAULT_ENCLOSID);
        assertThat(testChaleurs.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testChaleurs.getFacteurEleve()).isEqualTo(UPDATED_FACTEUR_ELEVE);
        assertThat(testChaleurs.getSuspect()).isEqualTo(UPDATED_SUSPECT);
        assertThat(testChaleurs.getActAugmentee()).isEqualTo(UPDATED_ACT_AUGMENTEE);
        assertThat(testChaleurs.getAlarmeChaleur()).isEqualTo(DEFAULT_ALARME_CHALEUR);
        assertThat(testChaleurs.getPasDeChaleur()).isEqualTo(UPDATED_PAS_DE_CHALEUR);
        assertThat(testChaleurs.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void fullUpdateChaleursWithPatch() throws Exception {
        // Initialize the database
        chaleursRepository.save(chaleurs);

        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();

        // Update the chaleurs using partial update
        Chaleurs partialUpdatedChaleurs = new Chaleurs();
        partialUpdatedChaleurs.setId(chaleurs.getId());

        partialUpdatedChaleurs
            .date(UPDATED_DATE)
            .jrsLact(UPDATED_JRS_LACT)
            .temps(UPDATED_TEMPS)
            .groupeid(UPDATED_GROUPEID)
            .enclosid(UPDATED_ENCLOSID)
            .activite(UPDATED_ACTIVITE)
            .facteurEleve(UPDATED_FACTEUR_ELEVE)
            .suspect(UPDATED_SUSPECT)
            .actAugmentee(UPDATED_ACT_AUGMENTEE)
            .alarmeChaleur(UPDATED_ALARME_CHALEUR)
            .pasDeChaleur(UPDATED_PAS_DE_CHALEUR)
            .cowId(UPDATED_COW_ID);

        restChaleursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChaleurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedChaleurs))
            )
            .andExpect(status().isOk());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
        Chaleurs testChaleurs = chaleursList.get(chaleursList.size() - 1);
        assertThat(testChaleurs.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testChaleurs.getJrsLact()).isEqualTo(UPDATED_JRS_LACT);
        assertThat(testChaleurs.getTemps()).isEqualTo(UPDATED_TEMPS);
        assertThat(testChaleurs.getGroupeid()).isEqualTo(UPDATED_GROUPEID);
        assertThat(testChaleurs.getEnclosid()).isEqualTo(UPDATED_ENCLOSID);
        assertThat(testChaleurs.getActivite()).isEqualTo(UPDATED_ACTIVITE);
        assertThat(testChaleurs.getFacteurEleve()).isEqualTo(UPDATED_FACTEUR_ELEVE);
        assertThat(testChaleurs.getSuspect()).isEqualTo(UPDATED_SUSPECT);
        assertThat(testChaleurs.getActAugmentee()).isEqualTo(UPDATED_ACT_AUGMENTEE);
        assertThat(testChaleurs.getAlarmeChaleur()).isEqualTo(UPDATED_ALARME_CHALEUR);
        assertThat(testChaleurs.getPasDeChaleur()).isEqualTo(UPDATED_PAS_DE_CHALEUR);
        assertThat(testChaleurs.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void patchNonExistingChaleurs() throws Exception {
        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();
        chaleurs.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChaleursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chaleurs.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chaleurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchChaleurs() throws Exception {
        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();
        chaleurs.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChaleursMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(chaleurs))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamChaleurs() throws Exception {
        int databaseSizeBeforeUpdate = chaleursRepository.findAll().size();
        chaleurs.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChaleursMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(chaleurs)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chaleurs in the database
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteChaleurs() throws Exception {
        // Initialize the database
        chaleursRepository.save(chaleurs);

        int databaseSizeBeforeDelete = chaleursRepository.findAll().size();

        // Delete the chaleurs
        restChaleursMockMvc
            .perform(delete(ENTITY_API_URL_ID, chaleurs.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Chaleurs> chaleursList = chaleursRepository.findAll();
        assertThat(chaleursList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
