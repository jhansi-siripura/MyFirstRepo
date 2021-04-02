package com.jan.learning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jan.learning.IntegrationTest;
import com.jan.learning.domain.Wish;
import com.jan.learning.domain.enumeration.WishCategory;
import com.jan.learning.repository.WishRepository;
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
 * Integration tests for the {@link WishResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WishResourceIT {

    private static final String DEFAULT_MY_WISH_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_MY_WISH_NOTE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_FULFILLED = false;
    private static final Boolean UPDATED_FULFILLED = true;

    private static final LocalDate DEFAULT_FULFILLED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FULFILLED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final WishCategory DEFAULT_CATEGORY = WishCategory.MISC;
    private static final WishCategory UPDATED_CATEGORY = WishCategory.LOVE;

    private static final String ENTITY_API_URL = "/api/wishes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWishMockMvc;

    private Wish wish;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wish createEntity(EntityManager em) {
        Wish wish = new Wish()
            .myWishNote(DEFAULT_MY_WISH_NOTE)
            .startDate(DEFAULT_START_DATE)
            .fulfilled(DEFAULT_FULFILLED)
            .fulfilledDate(DEFAULT_FULFILLED_DATE)
            .duration(DEFAULT_DURATION)
            .category(DEFAULT_CATEGORY);
        return wish;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Wish createUpdatedEntity(EntityManager em) {
        Wish wish = new Wish()
            .myWishNote(UPDATED_MY_WISH_NOTE)
            .startDate(UPDATED_START_DATE)
            .fulfilled(UPDATED_FULFILLED)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .duration(UPDATED_DURATION)
            .category(UPDATED_CATEGORY);
        return wish;
    }

    @BeforeEach
    public void initTest() {
        wish = createEntity(em);
    }

    @Test
    @Transactional
    void createWish() throws Exception {
        int databaseSizeBeforeCreate = wishRepository.findAll().size();
        // Create the Wish
        restWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isCreated());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeCreate + 1);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getMyWishNote()).isEqualTo(DEFAULT_MY_WISH_NOTE);
        assertThat(testWish.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testWish.getFulfilled()).isEqualTo(DEFAULT_FULFILLED);
        assertThat(testWish.getFulfilledDate()).isEqualTo(DEFAULT_FULFILLED_DATE);
        assertThat(testWish.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testWish.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    void createWishWithExistingId() throws Exception {
        // Create the Wish with an existing ID
        wish.setId(1L);

        int databaseSizeBeforeCreate = wishRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMyWishNoteIsRequired() throws Exception {
        int databaseSizeBeforeTest = wishRepository.findAll().size();
        // set the field null
        wish.setMyWishNote(null);

        // Create the Wish, which fails.

        restWishMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isBadRequest());

        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWishes() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get all the wishList
        restWishMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(wish.getId().intValue())))
            .andExpect(jsonPath("$.[*].myWishNote").value(hasItem(DEFAULT_MY_WISH_NOTE)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].fulfilled").value(hasItem(DEFAULT_FULFILLED.booleanValue())))
            .andExpect(jsonPath("$.[*].fulfilledDate").value(hasItem(DEFAULT_FULFILLED_DATE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    void getWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        // Get the wish
        restWishMockMvc
            .perform(get(ENTITY_API_URL_ID, wish.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(wish.getId().intValue()))
            .andExpect(jsonPath("$.myWishNote").value(DEFAULT_MY_WISH_NOTE))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.fulfilled").value(DEFAULT_FULFILLED.booleanValue()))
            .andExpect(jsonPath("$.fulfilledDate").value(DEFAULT_FULFILLED_DATE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWish() throws Exception {
        // Get the wish
        restWishMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Update the wish
        Wish updatedWish = wishRepository.findById(wish.getId()).get();
        // Disconnect from session so that the updates on updatedWish are not directly saved in db
        em.detach(updatedWish);
        updatedWish
            .myWishNote(UPDATED_MY_WISH_NOTE)
            .startDate(UPDATED_START_DATE)
            .fulfilled(UPDATED_FULFILLED)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .duration(UPDATED_DURATION)
            .category(UPDATED_CATEGORY);

        restWishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWish))
            )
            .andExpect(status().isOk());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getMyWishNote()).isEqualTo(UPDATED_MY_WISH_NOTE);
        assertThat(testWish.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWish.getFulfilled()).isEqualTo(UPDATED_FULFILLED);
        assertThat(testWish.getFulfilledDate()).isEqualTo(UPDATED_FULFILLED_DATE);
        assertThat(testWish.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testWish.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void putNonExistingWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();
        wish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, wish.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();
        wish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(wish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();
        wish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWishWithPatch() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Update the wish using partial update
        Wish partialUpdatedWish = new Wish();
        partialUpdatedWish.setId(wish.getId());

        partialUpdatedWish.myWishNote(UPDATED_MY_WISH_NOTE).startDate(UPDATED_START_DATE).category(UPDATED_CATEGORY);

        restWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWish))
            )
            .andExpect(status().isOk());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getMyWishNote()).isEqualTo(UPDATED_MY_WISH_NOTE);
        assertThat(testWish.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWish.getFulfilled()).isEqualTo(DEFAULT_FULFILLED);
        assertThat(testWish.getFulfilledDate()).isEqualTo(DEFAULT_FULFILLED_DATE);
        assertThat(testWish.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testWish.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void fullUpdateWishWithPatch() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        int databaseSizeBeforeUpdate = wishRepository.findAll().size();

        // Update the wish using partial update
        Wish partialUpdatedWish = new Wish();
        partialUpdatedWish.setId(wish.getId());

        partialUpdatedWish
            .myWishNote(UPDATED_MY_WISH_NOTE)
            .startDate(UPDATED_START_DATE)
            .fulfilled(UPDATED_FULFILLED)
            .fulfilledDate(UPDATED_FULFILLED_DATE)
            .duration(UPDATED_DURATION)
            .category(UPDATED_CATEGORY);

        restWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWish))
            )
            .andExpect(status().isOk());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
        Wish testWish = wishList.get(wishList.size() - 1);
        assertThat(testWish.getMyWishNote()).isEqualTo(UPDATED_MY_WISH_NOTE);
        assertThat(testWish.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testWish.getFulfilled()).isEqualTo(UPDATED_FULFILLED);
        assertThat(testWish.getFulfilledDate()).isEqualTo(UPDATED_FULFILLED_DATE);
        assertThat(testWish.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testWish.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void patchNonExistingWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();
        wish.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, wish.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();
        wish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(wish))
            )
            .andExpect(status().isBadRequest());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWish() throws Exception {
        int databaseSizeBeforeUpdate = wishRepository.findAll().size();
        wish.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWishMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(wish)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Wish in the database
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWish() throws Exception {
        // Initialize the database
        wishRepository.saveAndFlush(wish);

        int databaseSizeBeforeDelete = wishRepository.findAll().size();

        // Delete the wish
        restWishMockMvc
            .perform(delete(ENTITY_API_URL_ID, wish.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Wish> wishList = wishRepository.findAll();
        assertThat(wishList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
