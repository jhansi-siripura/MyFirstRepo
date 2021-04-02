package com.jan.learning.web.rest;

import static com.jan.learning.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jan.learning.IntegrationTest;
import com.jan.learning.domain.TradeSuggestion;
import com.jan.learning.domain.enumeration.TradeAction;
import com.jan.learning.domain.enumeration.TradeResult;
import com.jan.learning.domain.enumeration.TradeStatus;
import com.jan.learning.domain.enumeration.TradeType;
import com.jan.learning.repository.TradeSuggestionRepository;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TradeSuggestionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TradeSuggestionResourceIT {

    private static final TradeAction DEFAULT_ACTION = TradeAction.BUY;
    private static final TradeAction UPDATED_ACTION = TradeAction.SELL;

    private static final Integer DEFAULT_TRADE_IN_PRICE = 1;
    private static final Integer UPDATED_TRADE_IN_PRICE = 2;

    private static final Integer DEFAULT_MIN_TRADE_OUT_PRICE = 1;
    private static final Integer UPDATED_MIN_TRADE_OUT_PRICE = 2;

    private static final Integer DEFAULT_MIN_PROFIT_POINTS = 1;
    private static final Integer UPDATED_MIN_PROFIT_POINTS = 2;

    private static final Integer DEFAULT_BETTER_TRADEOUT_PRICE = 1;
    private static final Integer UPDATED_BETTER_TRADEOUT_PRICE = 2;

    private static final Integer DEFAULT_BETTER_TRADE_OUT_PROFIT_POINTS = 1;
    private static final Integer UPDATED_BETTER_TRADE_OUT_PROFIT_POINTS = 2;

    private static final Integer DEFAULT_ACTUAL_TRADEOUT_PRICE = 1;
    private static final Integer UPDATED_ACTUAL_TRADEOUT_PRICE = 2;

    private static final Integer DEFAULT_ACTUAL_PROFIT_POINTS = 1;
    private static final Integer UPDATED_ACTUAL_PROFIT_POINTS = 2;

    private static final Integer DEFAULT_SL_POINTS = 1;
    private static final Integer UPDATED_SL_POINTS = 2;

    private static final TradeStatus DEFAULT_TRADE_STATUS = TradeStatus.PENDING;
    private static final TradeStatus UPDATED_TRADE_STATUS = TradeStatus.EXECUTED;

    private static final TradeResult DEFAULT_TRADE_RESULTS = TradeResult.SUCCESS;
    private static final TradeResult UPDATED_TRADE_RESULTS = TradeResult.FAILURE;

    private static final ZonedDateTime DEFAULT_TRADE_IN_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRADE_IN_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TRADE_OUT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRADE_OUT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_TRADE_DURATION = 1;
    private static final Integer UPDATED_TRADE_DURATION = 2;

    private static final LocalDate DEFAULT_TRADE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRADE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_TRADE_SUGGESTION_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRADE_SUGGESTION_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final TradeType DEFAULT_TRADE_TYPE = TradeType.NRML;
    private static final TradeType UPDATED_TRADE_TYPE = TradeType.MIS;

    private static final Integer DEFAULT_ACTUAL_PL = 1;
    private static final Integer UPDATED_ACTUAL_PL = 2;

    private static final Integer DEFAULT_SL_PRICE = 1;
    private static final Integer UPDATED_SL_PRICE = 2;

    private static final Integer DEFAULT_CURRENT_MARKET_PRICE = 1;
    private static final Integer UPDATED_CURRENT_MARKET_PRICE = 2;

    private static final String ENTITY_API_URL = "/api/trade-suggestions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TradeSuggestionRepository tradeSuggestionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTradeSuggestionMockMvc;

    private TradeSuggestion tradeSuggestion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeSuggestion createEntity(EntityManager em) {
        TradeSuggestion tradeSuggestion = new TradeSuggestion()
            .action(DEFAULT_ACTION)
            .tradeInPrice(DEFAULT_TRADE_IN_PRICE)
            .minTradeOutPrice(DEFAULT_MIN_TRADE_OUT_PRICE)
            .minProfitPoints(DEFAULT_MIN_PROFIT_POINTS)
            .betterTradeoutPrice(DEFAULT_BETTER_TRADEOUT_PRICE)
            .betterTradeOutProfitPoints(DEFAULT_BETTER_TRADE_OUT_PROFIT_POINTS)
            .actualTradeoutPrice(DEFAULT_ACTUAL_TRADEOUT_PRICE)
            .actualProfitPoints(DEFAULT_ACTUAL_PROFIT_POINTS)
            .slPoints(DEFAULT_SL_POINTS)
            .tradeStatus(DEFAULT_TRADE_STATUS)
            .tradeResults(DEFAULT_TRADE_RESULTS)
            .tradeInTime(DEFAULT_TRADE_IN_TIME)
            .tradeOutTime(DEFAULT_TRADE_OUT_TIME)
            .tradeDuration(DEFAULT_TRADE_DURATION)
            .tradeDate(DEFAULT_TRADE_DATE)
            .tradeSuggestionTime(DEFAULT_TRADE_SUGGESTION_TIME)
            .tradeType(DEFAULT_TRADE_TYPE)
            .actualPL(DEFAULT_ACTUAL_PL)
            .slPrice(DEFAULT_SL_PRICE)
            .currentMarketPrice(DEFAULT_CURRENT_MARKET_PRICE);
        return tradeSuggestion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TradeSuggestion createUpdatedEntity(EntityManager em) {
        TradeSuggestion tradeSuggestion = new TradeSuggestion()
            .action(UPDATED_ACTION)
            .tradeInPrice(UPDATED_TRADE_IN_PRICE)
            .minTradeOutPrice(UPDATED_MIN_TRADE_OUT_PRICE)
            .minProfitPoints(UPDATED_MIN_PROFIT_POINTS)
            .betterTradeoutPrice(UPDATED_BETTER_TRADEOUT_PRICE)
            .betterTradeOutProfitPoints(UPDATED_BETTER_TRADE_OUT_PROFIT_POINTS)
            .actualTradeoutPrice(UPDATED_ACTUAL_TRADEOUT_PRICE)
            .actualProfitPoints(UPDATED_ACTUAL_PROFIT_POINTS)
            .slPoints(UPDATED_SL_POINTS)
            .tradeStatus(UPDATED_TRADE_STATUS)
            .tradeResults(UPDATED_TRADE_RESULTS)
            .tradeInTime(UPDATED_TRADE_IN_TIME)
            .tradeOutTime(UPDATED_TRADE_OUT_TIME)
            .tradeDuration(UPDATED_TRADE_DURATION)
            .tradeDate(UPDATED_TRADE_DATE)
            .tradeSuggestionTime(UPDATED_TRADE_SUGGESTION_TIME)
            .tradeType(UPDATED_TRADE_TYPE)
            .actualPL(UPDATED_ACTUAL_PL)
            .slPrice(UPDATED_SL_PRICE)
            .currentMarketPrice(UPDATED_CURRENT_MARKET_PRICE);
        return tradeSuggestion;
    }

    @BeforeEach
    public void initTest() {
        tradeSuggestion = createEntity(em);
    }

    @Test
    @Transactional
    void createTradeSuggestion() throws Exception {
        int databaseSizeBeforeCreate = tradeSuggestionRepository.findAll().size();
        // Create the TradeSuggestion
        restTradeSuggestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isCreated());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeCreate + 1);
        TradeSuggestion testTradeSuggestion = tradeSuggestionList.get(tradeSuggestionList.size() - 1);
        assertThat(testTradeSuggestion.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testTradeSuggestion.getTradeInPrice()).isEqualTo(DEFAULT_TRADE_IN_PRICE);
        assertThat(testTradeSuggestion.getMinTradeOutPrice()).isEqualTo(DEFAULT_MIN_TRADE_OUT_PRICE);
        assertThat(testTradeSuggestion.getMinProfitPoints()).isEqualTo(DEFAULT_MIN_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getBetterTradeoutPrice()).isEqualTo(DEFAULT_BETTER_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getBetterTradeOutProfitPoints()).isEqualTo(DEFAULT_BETTER_TRADE_OUT_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getActualTradeoutPrice()).isEqualTo(DEFAULT_ACTUAL_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getActualProfitPoints()).isEqualTo(DEFAULT_ACTUAL_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getSlPoints()).isEqualTo(DEFAULT_SL_POINTS);
        assertThat(testTradeSuggestion.getTradeStatus()).isEqualTo(DEFAULT_TRADE_STATUS);
        assertThat(testTradeSuggestion.getTradeResults()).isEqualTo(DEFAULT_TRADE_RESULTS);
        assertThat(testTradeSuggestion.getTradeInTime()).isEqualTo(DEFAULT_TRADE_IN_TIME);
        assertThat(testTradeSuggestion.getTradeOutTime()).isEqualTo(DEFAULT_TRADE_OUT_TIME);
        assertThat(testTradeSuggestion.getTradeDuration()).isEqualTo(DEFAULT_TRADE_DURATION);
        assertThat(testTradeSuggestion.getTradeDate()).isEqualTo(DEFAULT_TRADE_DATE);
        assertThat(testTradeSuggestion.getTradeSuggestionTime()).isEqualTo(DEFAULT_TRADE_SUGGESTION_TIME);
        assertThat(testTradeSuggestion.getTradeType()).isEqualTo(DEFAULT_TRADE_TYPE);
        assertThat(testTradeSuggestion.getActualPL()).isEqualTo(DEFAULT_ACTUAL_PL);
        assertThat(testTradeSuggestion.getSlPrice()).isEqualTo(DEFAULT_SL_PRICE);
        assertThat(testTradeSuggestion.getCurrentMarketPrice()).isEqualTo(DEFAULT_CURRENT_MARKET_PRICE);
    }

    @Test
    @Transactional
    void createTradeSuggestionWithExistingId() throws Exception {
        // Create the TradeSuggestion with an existing ID
        tradeSuggestion.setId(1L);

        int databaseSizeBeforeCreate = tradeSuggestionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTradeSuggestionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTradeSuggestions() throws Exception {
        // Initialize the database
        tradeSuggestionRepository.saveAndFlush(tradeSuggestion);

        // Get all the tradeSuggestionList
        restTradeSuggestionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tradeSuggestion.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].tradeInPrice").value(hasItem(DEFAULT_TRADE_IN_PRICE)))
            .andExpect(jsonPath("$.[*].minTradeOutPrice").value(hasItem(DEFAULT_MIN_TRADE_OUT_PRICE)))
            .andExpect(jsonPath("$.[*].minProfitPoints").value(hasItem(DEFAULT_MIN_PROFIT_POINTS)))
            .andExpect(jsonPath("$.[*].betterTradeoutPrice").value(hasItem(DEFAULT_BETTER_TRADEOUT_PRICE)))
            .andExpect(jsonPath("$.[*].betterTradeOutProfitPoints").value(hasItem(DEFAULT_BETTER_TRADE_OUT_PROFIT_POINTS)))
            .andExpect(jsonPath("$.[*].actualTradeoutPrice").value(hasItem(DEFAULT_ACTUAL_TRADEOUT_PRICE)))
            .andExpect(jsonPath("$.[*].actualProfitPoints").value(hasItem(DEFAULT_ACTUAL_PROFIT_POINTS)))
            .andExpect(jsonPath("$.[*].slPoints").value(hasItem(DEFAULT_SL_POINTS)))
            .andExpect(jsonPath("$.[*].tradeStatus").value(hasItem(DEFAULT_TRADE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tradeResults").value(hasItem(DEFAULT_TRADE_RESULTS.toString())))
            .andExpect(jsonPath("$.[*].tradeInTime").value(hasItem(sameInstant(DEFAULT_TRADE_IN_TIME))))
            .andExpect(jsonPath("$.[*].tradeOutTime").value(hasItem(sameInstant(DEFAULT_TRADE_OUT_TIME))))
            .andExpect(jsonPath("$.[*].tradeDuration").value(hasItem(DEFAULT_TRADE_DURATION)))
            .andExpect(jsonPath("$.[*].tradeDate").value(hasItem(DEFAULT_TRADE_DATE.toString())))
            .andExpect(jsonPath("$.[*].tradeSuggestionTime").value(hasItem(DEFAULT_TRADE_SUGGESTION_TIME.toString())))
            .andExpect(jsonPath("$.[*].tradeType").value(hasItem(DEFAULT_TRADE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].actualPL").value(hasItem(DEFAULT_ACTUAL_PL)))
            .andExpect(jsonPath("$.[*].slPrice").value(hasItem(DEFAULT_SL_PRICE)))
            .andExpect(jsonPath("$.[*].currentMarketPrice").value(hasItem(DEFAULT_CURRENT_MARKET_PRICE)));
    }

    @Test
    @Transactional
    void getTradeSuggestion() throws Exception {
        // Initialize the database
        tradeSuggestionRepository.saveAndFlush(tradeSuggestion);

        // Get the tradeSuggestion
        restTradeSuggestionMockMvc
            .perform(get(ENTITY_API_URL_ID, tradeSuggestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tradeSuggestion.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.tradeInPrice").value(DEFAULT_TRADE_IN_PRICE))
            .andExpect(jsonPath("$.minTradeOutPrice").value(DEFAULT_MIN_TRADE_OUT_PRICE))
            .andExpect(jsonPath("$.minProfitPoints").value(DEFAULT_MIN_PROFIT_POINTS))
            .andExpect(jsonPath("$.betterTradeoutPrice").value(DEFAULT_BETTER_TRADEOUT_PRICE))
            .andExpect(jsonPath("$.betterTradeOutProfitPoints").value(DEFAULT_BETTER_TRADE_OUT_PROFIT_POINTS))
            .andExpect(jsonPath("$.actualTradeoutPrice").value(DEFAULT_ACTUAL_TRADEOUT_PRICE))
            .andExpect(jsonPath("$.actualProfitPoints").value(DEFAULT_ACTUAL_PROFIT_POINTS))
            .andExpect(jsonPath("$.slPoints").value(DEFAULT_SL_POINTS))
            .andExpect(jsonPath("$.tradeStatus").value(DEFAULT_TRADE_STATUS.toString()))
            .andExpect(jsonPath("$.tradeResults").value(DEFAULT_TRADE_RESULTS.toString()))
            .andExpect(jsonPath("$.tradeInTime").value(sameInstant(DEFAULT_TRADE_IN_TIME)))
            .andExpect(jsonPath("$.tradeOutTime").value(sameInstant(DEFAULT_TRADE_OUT_TIME)))
            .andExpect(jsonPath("$.tradeDuration").value(DEFAULT_TRADE_DURATION))
            .andExpect(jsonPath("$.tradeDate").value(DEFAULT_TRADE_DATE.toString()))
            .andExpect(jsonPath("$.tradeSuggestionTime").value(DEFAULT_TRADE_SUGGESTION_TIME.toString()))
            .andExpect(jsonPath("$.tradeType").value(DEFAULT_TRADE_TYPE.toString()))
            .andExpect(jsonPath("$.actualPL").value(DEFAULT_ACTUAL_PL))
            .andExpect(jsonPath("$.slPrice").value(DEFAULT_SL_PRICE))
            .andExpect(jsonPath("$.currentMarketPrice").value(DEFAULT_CURRENT_MARKET_PRICE));
    }

    @Test
    @Transactional
    void getNonExistingTradeSuggestion() throws Exception {
        // Get the tradeSuggestion
        restTradeSuggestionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTradeSuggestion() throws Exception {
        // Initialize the database
        tradeSuggestionRepository.saveAndFlush(tradeSuggestion);

        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();

        // Update the tradeSuggestion
        TradeSuggestion updatedTradeSuggestion = tradeSuggestionRepository.findById(tradeSuggestion.getId()).get();
        // Disconnect from session so that the updates on updatedTradeSuggestion are not directly saved in db
        em.detach(updatedTradeSuggestion);
        updatedTradeSuggestion
            .action(UPDATED_ACTION)
            .tradeInPrice(UPDATED_TRADE_IN_PRICE)
            .minTradeOutPrice(UPDATED_MIN_TRADE_OUT_PRICE)
            .minProfitPoints(UPDATED_MIN_PROFIT_POINTS)
            .betterTradeoutPrice(UPDATED_BETTER_TRADEOUT_PRICE)
            .betterTradeOutProfitPoints(UPDATED_BETTER_TRADE_OUT_PROFIT_POINTS)
            .actualTradeoutPrice(UPDATED_ACTUAL_TRADEOUT_PRICE)
            .actualProfitPoints(UPDATED_ACTUAL_PROFIT_POINTS)
            .slPoints(UPDATED_SL_POINTS)
            .tradeStatus(UPDATED_TRADE_STATUS)
            .tradeResults(UPDATED_TRADE_RESULTS)
            .tradeInTime(UPDATED_TRADE_IN_TIME)
            .tradeOutTime(UPDATED_TRADE_OUT_TIME)
            .tradeDuration(UPDATED_TRADE_DURATION)
            .tradeDate(UPDATED_TRADE_DATE)
            .tradeSuggestionTime(UPDATED_TRADE_SUGGESTION_TIME)
            .tradeType(UPDATED_TRADE_TYPE)
            .actualPL(UPDATED_ACTUAL_PL)
            .slPrice(UPDATED_SL_PRICE)
            .currentMarketPrice(UPDATED_CURRENT_MARKET_PRICE);

        restTradeSuggestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTradeSuggestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTradeSuggestion))
            )
            .andExpect(status().isOk());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
        TradeSuggestion testTradeSuggestion = tradeSuggestionList.get(tradeSuggestionList.size() - 1);
        assertThat(testTradeSuggestion.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTradeSuggestion.getTradeInPrice()).isEqualTo(UPDATED_TRADE_IN_PRICE);
        assertThat(testTradeSuggestion.getMinTradeOutPrice()).isEqualTo(UPDATED_MIN_TRADE_OUT_PRICE);
        assertThat(testTradeSuggestion.getMinProfitPoints()).isEqualTo(UPDATED_MIN_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getBetterTradeoutPrice()).isEqualTo(UPDATED_BETTER_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getBetterTradeOutProfitPoints()).isEqualTo(UPDATED_BETTER_TRADE_OUT_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getActualTradeoutPrice()).isEqualTo(UPDATED_ACTUAL_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getActualProfitPoints()).isEqualTo(UPDATED_ACTUAL_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getSlPoints()).isEqualTo(UPDATED_SL_POINTS);
        assertThat(testTradeSuggestion.getTradeStatus()).isEqualTo(UPDATED_TRADE_STATUS);
        assertThat(testTradeSuggestion.getTradeResults()).isEqualTo(UPDATED_TRADE_RESULTS);
        assertThat(testTradeSuggestion.getTradeInTime()).isEqualTo(UPDATED_TRADE_IN_TIME);
        assertThat(testTradeSuggestion.getTradeOutTime()).isEqualTo(UPDATED_TRADE_OUT_TIME);
        assertThat(testTradeSuggestion.getTradeDuration()).isEqualTo(UPDATED_TRADE_DURATION);
        assertThat(testTradeSuggestion.getTradeDate()).isEqualTo(UPDATED_TRADE_DATE);
        assertThat(testTradeSuggestion.getTradeSuggestionTime()).isEqualTo(UPDATED_TRADE_SUGGESTION_TIME);
        assertThat(testTradeSuggestion.getTradeType()).isEqualTo(UPDATED_TRADE_TYPE);
        assertThat(testTradeSuggestion.getActualPL()).isEqualTo(UPDATED_ACTUAL_PL);
        assertThat(testTradeSuggestion.getSlPrice()).isEqualTo(UPDATED_SL_PRICE);
        assertThat(testTradeSuggestion.getCurrentMarketPrice()).isEqualTo(UPDATED_CURRENT_MARKET_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingTradeSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();
        tradeSuggestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeSuggestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tradeSuggestion.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTradeSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();
        tradeSuggestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeSuggestionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTradeSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();
        tradeSuggestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeSuggestionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTradeSuggestionWithPatch() throws Exception {
        // Initialize the database
        tradeSuggestionRepository.saveAndFlush(tradeSuggestion);

        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();

        // Update the tradeSuggestion using partial update
        TradeSuggestion partialUpdatedTradeSuggestion = new TradeSuggestion();
        partialUpdatedTradeSuggestion.setId(tradeSuggestion.getId());

        partialUpdatedTradeSuggestion
            .action(UPDATED_ACTION)
            .tradeInPrice(UPDATED_TRADE_IN_PRICE)
            .minTradeOutPrice(UPDATED_MIN_TRADE_OUT_PRICE)
            .minProfitPoints(UPDATED_MIN_PROFIT_POINTS)
            .actualTradeoutPrice(UPDATED_ACTUAL_TRADEOUT_PRICE)
            .actualProfitPoints(UPDATED_ACTUAL_PROFIT_POINTS)
            .slPoints(UPDATED_SL_POINTS)
            .tradeStatus(UPDATED_TRADE_STATUS)
            .tradeResults(UPDATED_TRADE_RESULTS)
            .tradeInTime(UPDATED_TRADE_IN_TIME)
            .tradeOutTime(UPDATED_TRADE_OUT_TIME)
            .tradeDuration(UPDATED_TRADE_DURATION)
            .tradeType(UPDATED_TRADE_TYPE)
            .actualPL(UPDATED_ACTUAL_PL)
            .slPrice(UPDATED_SL_PRICE);

        restTradeSuggestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeSuggestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeSuggestion))
            )
            .andExpect(status().isOk());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
        TradeSuggestion testTradeSuggestion = tradeSuggestionList.get(tradeSuggestionList.size() - 1);
        assertThat(testTradeSuggestion.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTradeSuggestion.getTradeInPrice()).isEqualTo(UPDATED_TRADE_IN_PRICE);
        assertThat(testTradeSuggestion.getMinTradeOutPrice()).isEqualTo(UPDATED_MIN_TRADE_OUT_PRICE);
        assertThat(testTradeSuggestion.getMinProfitPoints()).isEqualTo(UPDATED_MIN_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getBetterTradeoutPrice()).isEqualTo(DEFAULT_BETTER_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getBetterTradeOutProfitPoints()).isEqualTo(DEFAULT_BETTER_TRADE_OUT_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getActualTradeoutPrice()).isEqualTo(UPDATED_ACTUAL_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getActualProfitPoints()).isEqualTo(UPDATED_ACTUAL_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getSlPoints()).isEqualTo(UPDATED_SL_POINTS);
        assertThat(testTradeSuggestion.getTradeStatus()).isEqualTo(UPDATED_TRADE_STATUS);
        assertThat(testTradeSuggestion.getTradeResults()).isEqualTo(UPDATED_TRADE_RESULTS);
        assertThat(testTradeSuggestion.getTradeInTime()).isEqualTo(UPDATED_TRADE_IN_TIME);
        assertThat(testTradeSuggestion.getTradeOutTime()).isEqualTo(UPDATED_TRADE_OUT_TIME);
        assertThat(testTradeSuggestion.getTradeDuration()).isEqualTo(UPDATED_TRADE_DURATION);
        assertThat(testTradeSuggestion.getTradeDate()).isEqualTo(DEFAULT_TRADE_DATE);
        assertThat(testTradeSuggestion.getTradeSuggestionTime()).isEqualTo(DEFAULT_TRADE_SUGGESTION_TIME);
        assertThat(testTradeSuggestion.getTradeType()).isEqualTo(UPDATED_TRADE_TYPE);
        assertThat(testTradeSuggestion.getActualPL()).isEqualTo(UPDATED_ACTUAL_PL);
        assertThat(testTradeSuggestion.getSlPrice()).isEqualTo(UPDATED_SL_PRICE);
        assertThat(testTradeSuggestion.getCurrentMarketPrice()).isEqualTo(DEFAULT_CURRENT_MARKET_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateTradeSuggestionWithPatch() throws Exception {
        // Initialize the database
        tradeSuggestionRepository.saveAndFlush(tradeSuggestion);

        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();

        // Update the tradeSuggestion using partial update
        TradeSuggestion partialUpdatedTradeSuggestion = new TradeSuggestion();
        partialUpdatedTradeSuggestion.setId(tradeSuggestion.getId());

        partialUpdatedTradeSuggestion
            .action(UPDATED_ACTION)
            .tradeInPrice(UPDATED_TRADE_IN_PRICE)
            .minTradeOutPrice(UPDATED_MIN_TRADE_OUT_PRICE)
            .minProfitPoints(UPDATED_MIN_PROFIT_POINTS)
            .betterTradeoutPrice(UPDATED_BETTER_TRADEOUT_PRICE)
            .betterTradeOutProfitPoints(UPDATED_BETTER_TRADE_OUT_PROFIT_POINTS)
            .actualTradeoutPrice(UPDATED_ACTUAL_TRADEOUT_PRICE)
            .actualProfitPoints(UPDATED_ACTUAL_PROFIT_POINTS)
            .slPoints(UPDATED_SL_POINTS)
            .tradeStatus(UPDATED_TRADE_STATUS)
            .tradeResults(UPDATED_TRADE_RESULTS)
            .tradeInTime(UPDATED_TRADE_IN_TIME)
            .tradeOutTime(UPDATED_TRADE_OUT_TIME)
            .tradeDuration(UPDATED_TRADE_DURATION)
            .tradeDate(UPDATED_TRADE_DATE)
            .tradeSuggestionTime(UPDATED_TRADE_SUGGESTION_TIME)
            .tradeType(UPDATED_TRADE_TYPE)
            .actualPL(UPDATED_ACTUAL_PL)
            .slPrice(UPDATED_SL_PRICE)
            .currentMarketPrice(UPDATED_CURRENT_MARKET_PRICE);

        restTradeSuggestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTradeSuggestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTradeSuggestion))
            )
            .andExpect(status().isOk());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
        TradeSuggestion testTradeSuggestion = tradeSuggestionList.get(tradeSuggestionList.size() - 1);
        assertThat(testTradeSuggestion.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testTradeSuggestion.getTradeInPrice()).isEqualTo(UPDATED_TRADE_IN_PRICE);
        assertThat(testTradeSuggestion.getMinTradeOutPrice()).isEqualTo(UPDATED_MIN_TRADE_OUT_PRICE);
        assertThat(testTradeSuggestion.getMinProfitPoints()).isEqualTo(UPDATED_MIN_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getBetterTradeoutPrice()).isEqualTo(UPDATED_BETTER_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getBetterTradeOutProfitPoints()).isEqualTo(UPDATED_BETTER_TRADE_OUT_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getActualTradeoutPrice()).isEqualTo(UPDATED_ACTUAL_TRADEOUT_PRICE);
        assertThat(testTradeSuggestion.getActualProfitPoints()).isEqualTo(UPDATED_ACTUAL_PROFIT_POINTS);
        assertThat(testTradeSuggestion.getSlPoints()).isEqualTo(UPDATED_SL_POINTS);
        assertThat(testTradeSuggestion.getTradeStatus()).isEqualTo(UPDATED_TRADE_STATUS);
        assertThat(testTradeSuggestion.getTradeResults()).isEqualTo(UPDATED_TRADE_RESULTS);
        assertThat(testTradeSuggestion.getTradeInTime()).isEqualTo(UPDATED_TRADE_IN_TIME);
        assertThat(testTradeSuggestion.getTradeOutTime()).isEqualTo(UPDATED_TRADE_OUT_TIME);
        assertThat(testTradeSuggestion.getTradeDuration()).isEqualTo(UPDATED_TRADE_DURATION);
        assertThat(testTradeSuggestion.getTradeDate()).isEqualTo(UPDATED_TRADE_DATE);
        assertThat(testTradeSuggestion.getTradeSuggestionTime()).isEqualTo(UPDATED_TRADE_SUGGESTION_TIME);
        assertThat(testTradeSuggestion.getTradeType()).isEqualTo(UPDATED_TRADE_TYPE);
        assertThat(testTradeSuggestion.getActualPL()).isEqualTo(UPDATED_ACTUAL_PL);
        assertThat(testTradeSuggestion.getSlPrice()).isEqualTo(UPDATED_SL_PRICE);
        assertThat(testTradeSuggestion.getCurrentMarketPrice()).isEqualTo(UPDATED_CURRENT_MARKET_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingTradeSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();
        tradeSuggestion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTradeSuggestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tradeSuggestion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTradeSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();
        tradeSuggestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeSuggestionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isBadRequest());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTradeSuggestion() throws Exception {
        int databaseSizeBeforeUpdate = tradeSuggestionRepository.findAll().size();
        tradeSuggestion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTradeSuggestionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tradeSuggestion))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TradeSuggestion in the database
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTradeSuggestion() throws Exception {
        // Initialize the database
        tradeSuggestionRepository.saveAndFlush(tradeSuggestion);

        int databaseSizeBeforeDelete = tradeSuggestionRepository.findAll().size();

        // Delete the tradeSuggestion
        restTradeSuggestionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tradeSuggestion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TradeSuggestion> tradeSuggestionList = tradeSuggestionRepository.findAll();
        assertThat(tradeSuggestionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
