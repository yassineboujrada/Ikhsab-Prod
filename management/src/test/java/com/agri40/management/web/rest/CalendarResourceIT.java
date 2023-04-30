package com.agri40.management.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.agri40.management.IntegrationTest;
import com.agri40.management.domain.Calendar;
import com.agri40.management.repository.CalendarRepository;
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
 * Integration tests for the {@link CalendarResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalendarResourceIT {

    private static final String DEFAULT_LACTATION = "AAAAAAAAAA";
    private static final String UPDATED_LACTATION = "BBBBBBBBBB";

    private static final String DEFAULT_JRS_LACT = "AAAAAAAAAA";
    private static final String UPDATED_JRS_LACT = "BBBBBBBBBB";

    private static final String DEFAULT_STATUT_REPRODUCTION = "AAAAAAAAAA";
    private static final String UPDATED_STATUT_REPRODUCTION = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT_PROD = "AAAAAAAAAA";
    private static final String UPDATED_ETAT_PROD = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_NAISSANCE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_NAISSANCE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_VELAGE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_VELAGE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHALEUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHALEUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_INSEMINATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSEMINATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COW_ID = "AAAAAAAAAA";
    private static final String UPDATED_COW_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calendars";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private MockMvc restCalendarMockMvc;

    private Calendar calendar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createEntity() {
        Calendar calendar = new Calendar()
            .lactation(DEFAULT_LACTATION)
            .jrsLact(DEFAULT_JRS_LACT)
            .statutReproduction(DEFAULT_STATUT_REPRODUCTION)
            .etatProd(DEFAULT_ETAT_PROD)
            .dateNaissance(DEFAULT_DATE_NAISSANCE)
            .velage(DEFAULT_VELAGE)
            .chaleur(DEFAULT_CHALEUR)
            .insemination(DEFAULT_INSEMINATION)
            .cowId(DEFAULT_COW_ID);
        return calendar;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Calendar createUpdatedEntity() {
        Calendar calendar = new Calendar()
            .lactation(UPDATED_LACTATION)
            .jrsLact(UPDATED_JRS_LACT)
            .statutReproduction(UPDATED_STATUT_REPRODUCTION)
            .etatProd(UPDATED_ETAT_PROD)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .velage(UPDATED_VELAGE)
            .chaleur(UPDATED_CHALEUR)
            .insemination(UPDATED_INSEMINATION)
            .cowId(UPDATED_COW_ID);
        return calendar;
    }

    @BeforeEach
    public void initTest() {
        calendarRepository.deleteAll();
        calendar = createEntity();
    }

    @Test
    void createCalendar() throws Exception {
        int databaseSizeBeforeCreate = calendarRepository.findAll().size();
        // Create the Calendar
        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendar)))
            .andExpect(status().isCreated());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate + 1);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getLactation()).isEqualTo(DEFAULT_LACTATION);
        assertThat(testCalendar.getJrsLact()).isEqualTo(DEFAULT_JRS_LACT);
        assertThat(testCalendar.getStatutReproduction()).isEqualTo(DEFAULT_STATUT_REPRODUCTION);
        assertThat(testCalendar.getEtatProd()).isEqualTo(DEFAULT_ETAT_PROD);
        assertThat(testCalendar.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);
        assertThat(testCalendar.getVelage()).isEqualTo(DEFAULT_VELAGE);
        assertThat(testCalendar.getChaleur()).isEqualTo(DEFAULT_CHALEUR);
        assertThat(testCalendar.getInsemination()).isEqualTo(DEFAULT_INSEMINATION);
        assertThat(testCalendar.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void createCalendarWithExistingId() throws Exception {
        // Create the Calendar with an existing ID
        calendar.setId("existing_id");

        int databaseSizeBeforeCreate = calendarRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendarMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendar)))
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCalendars() throws Exception {
        // Initialize the database
        calendarRepository.save(calendar);

        // Get all the calendarList
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId())))
            .andExpect(jsonPath("$.[*].lactation").value(hasItem(DEFAULT_LACTATION)))
            .andExpect(jsonPath("$.[*].jrsLact").value(hasItem(DEFAULT_JRS_LACT)))
            .andExpect(jsonPath("$.[*].statutReproduction").value(hasItem(DEFAULT_STATUT_REPRODUCTION)))
            .andExpect(jsonPath("$.[*].etatProd").value(hasItem(DEFAULT_ETAT_PROD)))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].velage").value(hasItem(DEFAULT_VELAGE.toString())))
            .andExpect(jsonPath("$.[*].chaleur").value(hasItem(DEFAULT_CHALEUR.toString())))
            .andExpect(jsonPath("$.[*].insemination").value(hasItem(DEFAULT_INSEMINATION.toString())))
            .andExpect(jsonPath("$.[*].cowId").value(hasItem(DEFAULT_COW_ID)));
    }

    @Test
    void getCalendar() throws Exception {
        // Initialize the database
        calendarRepository.save(calendar);

        // Get the calendar
        restCalendarMockMvc
            .perform(get(ENTITY_API_URL_ID, calendar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendar.getId()))
            .andExpect(jsonPath("$.lactation").value(DEFAULT_LACTATION))
            .andExpect(jsonPath("$.jrsLact").value(DEFAULT_JRS_LACT))
            .andExpect(jsonPath("$.statutReproduction").value(DEFAULT_STATUT_REPRODUCTION))
            .andExpect(jsonPath("$.etatProd").value(DEFAULT_ETAT_PROD))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.velage").value(DEFAULT_VELAGE.toString()))
            .andExpect(jsonPath("$.chaleur").value(DEFAULT_CHALEUR.toString()))
            .andExpect(jsonPath("$.insemination").value(DEFAULT_INSEMINATION.toString()))
            .andExpect(jsonPath("$.cowId").value(DEFAULT_COW_ID));
    }

    @Test
    void getNonExistingCalendar() throws Exception {
        // Get the calendar
        restCalendarMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCalendar() throws Exception {
        // Initialize the database
        calendarRepository.save(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar
        Calendar updatedCalendar = calendarRepository.findById(calendar.getId()).get();
        updatedCalendar
            .lactation(UPDATED_LACTATION)
            .jrsLact(UPDATED_JRS_LACT)
            .statutReproduction(UPDATED_STATUT_REPRODUCTION)
            .etatProd(UPDATED_ETAT_PROD)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .velage(UPDATED_VELAGE)
            .chaleur(UPDATED_CHALEUR)
            .insemination(UPDATED_INSEMINATION)
            .cowId(UPDATED_COW_ID);

        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalendar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getLactation()).isEqualTo(UPDATED_LACTATION);
        assertThat(testCalendar.getJrsLact()).isEqualTo(UPDATED_JRS_LACT);
        assertThat(testCalendar.getStatutReproduction()).isEqualTo(UPDATED_STATUT_REPRODUCTION);
        assertThat(testCalendar.getEtatProd()).isEqualTo(UPDATED_ETAT_PROD);
        assertThat(testCalendar.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testCalendar.getVelage()).isEqualTo(UPDATED_VELAGE);
        assertThat(testCalendar.getChaleur()).isEqualTo(UPDATED_CHALEUR);
        assertThat(testCalendar.getInsemination()).isEqualTo(UPDATED_INSEMINATION);
        assertThat(testCalendar.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void putNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendar.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCalendarWithPatch() throws Exception {
        // Initialize the database
        calendarRepository.save(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar using partial update
        Calendar partialUpdatedCalendar = new Calendar();
        partialUpdatedCalendar.setId(calendar.getId());

        partialUpdatedCalendar
            .lactation(UPDATED_LACTATION)
            .jrsLact(UPDATED_JRS_LACT)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .velage(UPDATED_VELAGE)
            .insemination(UPDATED_INSEMINATION);

        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getLactation()).isEqualTo(UPDATED_LACTATION);
        assertThat(testCalendar.getJrsLact()).isEqualTo(UPDATED_JRS_LACT);
        assertThat(testCalendar.getStatutReproduction()).isEqualTo(DEFAULT_STATUT_REPRODUCTION);
        assertThat(testCalendar.getEtatProd()).isEqualTo(DEFAULT_ETAT_PROD);
        assertThat(testCalendar.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testCalendar.getVelage()).isEqualTo(UPDATED_VELAGE);
        assertThat(testCalendar.getChaleur()).isEqualTo(DEFAULT_CHALEUR);
        assertThat(testCalendar.getInsemination()).isEqualTo(UPDATED_INSEMINATION);
        assertThat(testCalendar.getCowId()).isEqualTo(DEFAULT_COW_ID);
    }

    @Test
    void fullUpdateCalendarWithPatch() throws Exception {
        // Initialize the database
        calendarRepository.save(calendar);

        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

        // Update the calendar using partial update
        Calendar partialUpdatedCalendar = new Calendar();
        partialUpdatedCalendar.setId(calendar.getId());

        partialUpdatedCalendar
            .lactation(UPDATED_LACTATION)
            .jrsLact(UPDATED_JRS_LACT)
            .statutReproduction(UPDATED_STATUT_REPRODUCTION)
            .etatProd(UPDATED_ETAT_PROD)
            .dateNaissance(UPDATED_DATE_NAISSANCE)
            .velage(UPDATED_VELAGE)
            .chaleur(UPDATED_CHALEUR)
            .insemination(UPDATED_INSEMINATION)
            .cowId(UPDATED_COW_ID);

        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendar))
            )
            .andExpect(status().isOk());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
        Calendar testCalendar = calendarList.get(calendarList.size() - 1);
        assertThat(testCalendar.getLactation()).isEqualTo(UPDATED_LACTATION);
        assertThat(testCalendar.getJrsLact()).isEqualTo(UPDATED_JRS_LACT);
        assertThat(testCalendar.getStatutReproduction()).isEqualTo(UPDATED_STATUT_REPRODUCTION);
        assertThat(testCalendar.getEtatProd()).isEqualTo(UPDATED_ETAT_PROD);
        assertThat(testCalendar.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);
        assertThat(testCalendar.getVelage()).isEqualTo(UPDATED_VELAGE);
        assertThat(testCalendar.getChaleur()).isEqualTo(UPDATED_CHALEUR);
        assertThat(testCalendar.getInsemination()).isEqualTo(UPDATED_INSEMINATION);
        assertThat(testCalendar.getCowId()).isEqualTo(UPDATED_COW_ID);
    }

    @Test
    void patchNonExistingCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendar.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendar))
            )
            .andExpect(status().isBadRequest());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCalendar() throws Exception {
        int databaseSizeBeforeUpdate = calendarRepository.findAll().size();
        calendar.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendarMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(calendar)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Calendar in the database
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCalendar() throws Exception {
        // Initialize the database
        calendarRepository.save(calendar);

        int databaseSizeBeforeDelete = calendarRepository.findAll().size();

        // Delete the calendar
        restCalendarMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendar.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Calendar> calendarList = calendarRepository.findAll();
        assertThat(calendarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
