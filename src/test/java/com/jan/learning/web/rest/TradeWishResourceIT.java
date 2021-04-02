package com.jan.learning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jan.learning.IntegrationTest;
import com.jan.learning.domain.TradeWish;
import com.jan.learning.repository.TradeWishRepository;
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
 * Integration tests for the {@link TradeWishResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TradeWishResourceIT {

    private static final Integer DEFAULT_TRADE_WISH_NOTE = 1;
    private static final Integer UPDATED_TRADE_WISH_NOTE = 2;

    private static final Boolean DEFAULT_PICKED = false;
    private static final Boolean UPDATED_PICKED = true;

    private static final LocalDate DEFAULT_PICKED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PICKED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/trade-wishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TradeWishRepository tradeWishRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTradeWishMockMvc;

    private TradeWish tradeWish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeWish createEntity(EntityManager em) {
        TradeWish tradeWish = new TradeWish().tradeWishNote(DEFAULT_TRADE_WISH_NOTE).picked(DEFAULT_PICKED).pickedDate(DEFAULT_PICKED_DATE);
        return tradeWish;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeWish createUpdatedEntity(EntityManager em) {
        TradeWish tradeWish = new TradeWish().tradeWishNote(UPDATED_TRADE_WISH_NOTE).picked(UPDATED_PICKED).pickedDate(UPDATED_PICKED_DATE);
        return tradeWish;
    }

    @BeforeEach
    public void initTest() {
        tradeWish = createEntity(em);
    }

    @Test
    @Transactional
    void createTradeWish() throws Exception {
        int databaseSizeBeforeCreate = tradeWishRepository.findAll().size();
        // Create the TradeWish
        restTradeWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeWish)))
            .andExpect(status().isCreated());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeCreate + 1);
        TradeWish testTradeWish = tradeWishList.get(tradeWishList.size() - 1);
        assertThat(testTradeWish.getTradeWishNote()).isEqualTo(DEFAULT_TRADE_WISH_NOTE);
        assertThat(testTradeWish.getPicked()).isEqualTo(DEFAULT_PICKED);
        assertThat(testTradeWish.getPickedDate()).isEqualTo(DEFAULT_PICKED_DATE);
    }

    @Test
    @Transactional
    void createTradeWishWithExistingId() throws Exception {
        // Create the TradeWish with an existing ID
        tradeWish.setId(1L);

        int databaseSizeBeforeCreate = tradeWishRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTradeWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeWish)))
            .andExpect(status().isBadRequest());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTradeWishNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeWishRepository.findAll().size();
        // set the field null
        tradeWish.setTradeWishNote(null);

        // Create the TradeWish, which fails.

        restTradeWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeWish)))
            .andExpect(status().isBadRequest());

        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPickedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tradeWishRepository.findAll().size();
        // set the field null
        tradeWish.setPickedDate(null);

        // Create the TradeWish, which fails.

        restTradeWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeWish)))
            .andExpect(status().isBadRequest());

        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTradeWishes() throws Exception {
        // Initialize the database
        tradeWishRepository.saveAndFlush(tradeWish);

        // Get all the tradeWishList
        restTradeWishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeWish.getId().intValue())))
            .andExpect(jsonPath("$.[*].tradeWishNote").value(hasItem(DEFAULT_TRADE_WISH_NOTE)))
            .andExpect(jsonPath("$.[*].picked").value(hasItem(DEFAULT_PICKED.booleanValue())))
            .andExpect(jsonPath("$.[*].pickedDate").value(hasItem(DEFAULT_PICKED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTradeWish() throws Exception {
        // Initialize the database
        tradeWishRepository.saveAndFlush(tradeWish);

        // Get the tradeWish
        restTradeWishMockMvc
            .perform(get(ENTITY_API_URL_ID, tradeWish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tradeWish.getId().intValue()))
            .andExpect(jsonPath("$.tradeWishNote").value(DEFAULT_TRADE_WISH_NOTE))
            .andExpect(jsonPath("$.picked").value(DEFAULT_PICKED.booleanValue()))
            .andExpect(jsonPath("$.pickedDate").value(DEFAULT_PICKED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTradeWish() throws Exception {
        // Get the tradeWish
        restTradeWishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTradeWish() throws Exception {
        // Initialize the database
        tradeWishRepository.saveAndFlush(tradeWish);

        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();

        // Update the tradeWish
        TradeWish updatedTradeWish = tradeWishRepository.findById(tradeWish.getId()).get();
        // Disconnect from session so that the updates on updatedTradeWish are not directly saved in db
        em.detach(updatedTradeWish);
        updatedTradeWish.tradeWishNote(UPDATED_TRADE_WISH_NOTE).picked(UPDATED_PICKED).pickedDate(UPDATED_PICKED_DATE);

        restTradeWishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTradeWish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTradeWish))
            )
            .andExpect(status().isOk());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
        TradeWish testTradeWish = tradeWishList.get(tradeWishList.size() - 1);
        assertThat(testTradeWish.getTradeWishNote()).isEqualTo(UPDATED_TRADE_WISH_NOTE);
        assertThat(testTradeWish.getPicked()).isEqualTo(UPDATED_PICKED);
        assertThat(testTradeWish.getPickedDate()).isEqualTo(UPDATED_PICKED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTradeWish() throws Exception {
        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();
        tradeWish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeWishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tradeWish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeWish))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTradeWish() throws Exception {
        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();
        tradeWish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeWishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeWish))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTradeWish() throws Exception {
        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();
        tradeWish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeWishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeWish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTradeWishWithPatch() throws Exception {
        // Initialize the database
        tradeWishRepository.saveAndFlush(tradeWish);

        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();

        // Update the tradeWish using partial update
        TradeWish partialUpdatedTradeWish = new TradeWish();
        partialUpdatedTradeWish.setId(tradeWish.getId());

        partialUpdatedTradeWish.tradeWishNote(UPDATED_TRADE_WISH_NOTE).pickedDate(UPDATED_PICKED_DATE);

        restTradeWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeWish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeWish))
            )
            .andExpect(status().isOk());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
        TradeWish testTradeWish = tradeWishList.get(tradeWishList.size() - 1);
        assertThat(testTradeWish.getTradeWishNote()).isEqualTo(UPDATED_TRADE_WISH_NOTE);
        assertThat(testTradeWish.getPicked()).isEqualTo(DEFAULT_PICKED);
        assertThat(testTradeWish.getPickedDate()).isEqualTo(UPDATED_PICKED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTradeWishWithPatch() throws Exception {
        // Initialize the database
        tradeWishRepository.saveAndFlush(tradeWish);

        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();

        // Update the tradeWish using partial update
        TradeWish partialUpdatedTradeWish = new TradeWish();
        partialUpdatedTradeWish.setId(tradeWish.getId());

        partialUpdatedTradeWish.tradeWishNote(UPDATED_TRADE_WISH_NOTE).picked(UPDATED_PICKED).pickedDate(UPDATED_PICKED_DATE);

        restTradeWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeWish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeWish))
            )
            .andExpect(status().isOk());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
        TradeWish testTradeWish = tradeWishList.get(tradeWishList.size() - 1);
        assertThat(testTradeWish.getTradeWishNote()).isEqualTo(UPDATED_TRADE_WISH_NOTE);
        assertThat(testTradeWish.getPicked()).isEqualTo(UPDATED_PICKED);
        assertThat(testTradeWish.getPickedDate()).isEqualTo(UPDATED_PICKED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTradeWish() throws Exception {
        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();
        tradeWish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tradeWish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeWish))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTradeWish() throws Exception {
        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();
        tradeWish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeWish))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTradeWish() throws Exception {
        int databaseSizeBeforeUpdate = tradeWishRepository.findAll().size();
        tradeWish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeWishMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tradeWish))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeWish in the database
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTradeWish() throws Exception {
        // Initialize the database
        tradeWishRepository.saveAndFlush(tradeWish);

        int databaseSizeBeforeDelete = tradeWishRepository.findAll().size();

        // Delete the tradeWish
        restTradeWishMockMvc
            .perform(delete(ENTITY_API_URL_ID, tradeWish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TradeWish> tradeWishList = tradeWishRepository.findAll();
        assertThat(tradeWishList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
