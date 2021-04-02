package com.jan.learning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jan.learning.IntegrationTest;
import com.jan.learning.domain.Gratitude;
import com.jan.learning.repository.GratitudeRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GratitudeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GratitudeResourceIT {

    private static final String DEFAULT_GRATEFUL_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_GRATEFUL_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_LOVED = false;
    private static final Boolean UPDATED_LOVED = true;

    private static final Boolean DEFAULT_ACHIEVED = false;
    private static final Boolean UPDATED_ACHIEVED = true;

    private static final String ENTITY_API_URL = "/api/gratitudes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GratitudeRepository gratitudeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGratitudeMockMvc;

    private Gratitude gratitude;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gratitude createEntity(EntityManager em) {
        Gratitude gratitude = new Gratitude()
            .gratefulNote(DEFAULT_GRATEFUL_NOTE)
            .createdDate(DEFAULT_CREATED_DATE)
            .loved(DEFAULT_LOVED)
            .achieved(DEFAULT_ACHIEVED);
        return gratitude;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gratitude createUpdatedEntity(EntityManager em) {
        Gratitude gratitude = new Gratitude()
            .gratefulNote(UPDATED_GRATEFUL_NOTE)
            .createdDate(UPDATED_CREATED_DATE)
            .loved(UPDATED_LOVED)
            .achieved(UPDATED_ACHIEVED);
        return gratitude;
    }

    @BeforeEach
    public void initTest() {
        gratitude = createEntity(em);
    }

    @Test
    @Transactional
    void createGratitude() throws Exception {
        int databaseSizeBeforeCreate = gratitudeRepository.findAll().size();
        // Create the Gratitude
        restGratitudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gratitude)))
            .andExpect(status().isCreated());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeCreate + 1);
        Gratitude testGratitude = gratitudeList.get(gratitudeList.size() - 1);
        assertThat(testGratitude.getGratefulNote()).isEqualTo(DEFAULT_GRATEFUL_NOTE);
        assertThat(testGratitude.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGratitude.getLoved()).isEqualTo(DEFAULT_LOVED);
        assertThat(testGratitude.getAchieved()).isEqualTo(DEFAULT_ACHIEVED);
    }

    @Test
    @Transactional
    void createGratitudeWithExistingId() throws Exception {
        // Create the Gratitude with an existing ID
        gratitude.setId(1L);

        int databaseSizeBeforeCreate = gratitudeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGratitudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gratitude)))
            .andExpect(status().isBadRequest());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGratefulNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = gratitudeRepository.findAll().size();
        // set the field null
        gratitude.setGratefulNote(null);

        // Create the Gratitude, which fails.

        restGratitudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gratitude)))
            .andExpect(status().isBadRequest());

        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = gratitudeRepository.findAll().size();
        // set the field null
        gratitude.setCreatedDate(null);

        // Create the Gratitude, which fails.

        restGratitudeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gratitude)))
            .andExpect(status().isBadRequest());

        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGratitudes() throws Exception {
        // Initialize the database
        gratitudeRepository.saveAndFlush(gratitude);

        // Get all the gratitudeList
        restGratitudeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gratitude.getId().intValue())))
            .andExpect(jsonPath("$.[*].gratefulNote").value(hasItem(DEFAULT_GRATEFUL_NOTE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].loved").value(hasItem(DEFAULT_LOVED.booleanValue())))
            .andExpect(jsonPath("$.[*].achieved").value(hasItem(DEFAULT_ACHIEVED.booleanValue())));
    }

    @Test
    @Transactional
    void getGratitude() throws Exception {
        // Initialize the database
        gratitudeRepository.saveAndFlush(gratitude);

        // Get the gratitude
        restGratitudeMockMvc
            .perform(get(ENTITY_API_URL_ID, gratitude.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gratitude.getId().intValue()))
            .andExpect(jsonPath("$.gratefulNote").value(DEFAULT_GRATEFUL_NOTE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.loved").value(DEFAULT_LOVED.booleanValue()))
            .andExpect(jsonPath("$.achieved").value(DEFAULT_ACHIEVED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingGratitude() throws Exception {
        // Get the gratitude
        restGratitudeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGratitude() throws Exception {
        // Initialize the database
        gratitudeRepository.saveAndFlush(gratitude);

        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();

        // Update the gratitude
        Gratitude updatedGratitude = gratitudeRepository.findById(gratitude.getId()).get();
        // Disconnect from session so that the updates on updatedGratitude are not directly saved in db
        em.detach(updatedGratitude);
        updatedGratitude
            .gratefulNote(UPDATED_GRATEFUL_NOTE)
            .createdDate(UPDATED_CREATED_DATE)
            .loved(UPDATED_LOVED)
            .achieved(UPDATED_ACHIEVED);

        restGratitudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGratitude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGratitude))
            )
            .andExpect(status().isOk());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
        Gratitude testGratitude = gratitudeList.get(gratitudeList.size() - 1);
        assertThat(testGratitude.getGratefulNote()).isEqualTo(UPDATED_GRATEFUL_NOTE);
        assertThat(testGratitude.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGratitude.getLoved()).isEqualTo(UPDATED_LOVED);
        assertThat(testGratitude.getAchieved()).isEqualTo(UPDATED_ACHIEVED);
    }

    @Test
    @Transactional
    void putNonExistingGratitude() throws Exception {
        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();
        gratitude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGratitudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gratitude.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gratitude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGratitude() throws Exception {
        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();
        gratitude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGratitudeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gratitude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGratitude() throws Exception {
        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();
        gratitude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGratitudeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gratitude)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGratitudeWithPatch() throws Exception {
        // Initialize the database
        gratitudeRepository.saveAndFlush(gratitude);

        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();

        // Update the gratitude using partial update
        Gratitude partialUpdatedGratitude = new Gratitude();
        partialUpdatedGratitude.setId(gratitude.getId());

        partialUpdatedGratitude.achieved(UPDATED_ACHIEVED);

        restGratitudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGratitude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGratitude))
            )
            .andExpect(status().isOk());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
        Gratitude testGratitude = gratitudeList.get(gratitudeList.size() - 1);
        assertThat(testGratitude.getGratefulNote()).isEqualTo(DEFAULT_GRATEFUL_NOTE);
        assertThat(testGratitude.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testGratitude.getLoved()).isEqualTo(DEFAULT_LOVED);
        assertThat(testGratitude.getAchieved()).isEqualTo(UPDATED_ACHIEVED);
    }

    @Test
    @Transactional
    void fullUpdateGratitudeWithPatch() throws Exception {
        // Initialize the database
        gratitudeRepository.saveAndFlush(gratitude);

        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();

        // Update the gratitude using partial update
        Gratitude partialUpdatedGratitude = new Gratitude();
        partialUpdatedGratitude.setId(gratitude.getId());

        partialUpdatedGratitude
            .gratefulNote(UPDATED_GRATEFUL_NOTE)
            .createdDate(UPDATED_CREATED_DATE)
            .loved(UPDATED_LOVED)
            .achieved(UPDATED_ACHIEVED);

        restGratitudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGratitude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGratitude))
            )
            .andExpect(status().isOk());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
        Gratitude testGratitude = gratitudeList.get(gratitudeList.size() - 1);
        assertThat(testGratitude.getGratefulNote()).isEqualTo(UPDATED_GRATEFUL_NOTE);
        assertThat(testGratitude.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testGratitude.getLoved()).isEqualTo(UPDATED_LOVED);
        assertThat(testGratitude.getAchieved()).isEqualTo(UPDATED_ACHIEVED);
    }

    @Test
    @Transactional
    void patchNonExistingGratitude() throws Exception {
        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();
        gratitude.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGratitudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gratitude.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gratitude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGratitude() throws Exception {
        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();
        gratitude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGratitudeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gratitude))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGratitude() throws Exception {
        int databaseSizeBeforeUpdate = gratitudeRepository.findAll().size();
        gratitude.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGratitudeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gratitude))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gratitude in the database
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGratitude() throws Exception {
        // Initialize the database
        gratitudeRepository.saveAndFlush(gratitude);

        int databaseSizeBeforeDelete = gratitudeRepository.findAll().size();

        // Delete the gratitude
        restGratitudeMockMvc
            .perform(delete(ENTITY_API_URL_ID, gratitude.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gratitude> gratitudeList = gratitudeRepository.findAll();
        assertThat(gratitudeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
