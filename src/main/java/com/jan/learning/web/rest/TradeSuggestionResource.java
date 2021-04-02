package com.jan.learning.web.rest;

import com.jan.learning.domain.TradeSuggestion;
import com.jan.learning.repository.TradeSuggestionRepository;
import com.jan.learning.service.TradeSuggestionService;
import com.jan.learning.web.rest.businesslogic.TradeSuggestionLogic;
import com.jan.learning.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jan.learning.domain.TradeSuggestion}.
 */
@RestController
@RequestMapping("/api")
public class TradeSuggestionResource {

    private final Logger log = LoggerFactory.getLogger(TradeSuggestionResource.class);

    private static final String ENTITY_NAME = "tradeSuggestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TradeSuggestionService tradeSuggestionService;

    private final TradeSuggestionRepository tradeSuggestionRepository;

    public TradeSuggestionResource(TradeSuggestionService tradeSuggestionService, TradeSuggestionRepository tradeSuggestionRepository) {
        this.tradeSuggestionService = tradeSuggestionService;
        this.tradeSuggestionRepository = tradeSuggestionRepository;
    }

    /**
     * {@code POST  /trade-suggestions} : Create a new tradeSuggestion.
     *
     * @param tradeSuggestion the tradeSuggestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tradeSuggestion, or with status {@code 400 (Bad Request)} if the tradeSuggestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trade-suggestions")
    public ResponseEntity<TradeSuggestion> createTradeSuggestion(@RequestBody TradeSuggestion tradeSuggestion) throws URISyntaxException {
        log.debug("REST request to save TradeSuggestion : {}", tradeSuggestion);
        if (tradeSuggestion.getId() != null) {
            throw new BadRequestAlertException("A new tradeSuggestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TradeSuggestion result = tradeSuggestionService.save(tradeSuggestion);
        return ResponseEntity
            .created(new URI("/api/trade-suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/trade-suggestions/suggested")
    public ResponseEntity<TradeSuggestion> suggestedTradeSuggestion(@RequestBody TradeSuggestion tradeSuggestion)
        throws URISyntaxException {
        log.debug("REST request to save TradeSuggestion : {}", tradeSuggestion);
        if (tradeSuggestion.getId() != null) {
            throw new BadRequestAlertException("A new tradeSuggestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        //        tradeSuggestion.setTradeResults(TradeResult.SUCCESS);
        //        System.out.println(tradeSuggestion.getCurrentMarketPrice());
        //        System.out.println(tradeSuggestion);
        LocalDate tradeDate = LocalDate.now();
        tradeSuggestion.setTradeDate(tradeDate);
        //TODO HERE ASK
        TradeSuggestionLogic logic = new TradeSuggestionLogic();
        tradeSuggestion = logic.getSuggestionFromAngel(tradeSuggestion);
        TradeSuggestion result = tradeSuggestionService.save(tradeSuggestion);
        return ResponseEntity
            .created(new URI("/api/trade-suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trade-suggestions/:id} : Updates an existing tradeSuggestion.
     *
     * @param id the id of the tradeSuggestion to save.
     * @param tradeSuggestion the tradeSuggestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeSuggestion,
     * or with status {@code 400 (Bad Request)} if the tradeSuggestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tradeSuggestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trade-suggestions/{id}")
    public ResponseEntity<TradeSuggestion> updateTradeSuggestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TradeSuggestion tradeSuggestion
    ) throws URISyntaxException {
        log.debug("REST request to update TradeSuggestion : {}, {}", id, tradeSuggestion);
        if (tradeSuggestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeSuggestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeSuggestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TradeSuggestion result = tradeSuggestionService.save(tradeSuggestion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tradeSuggestion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trade-suggestions/:id} : Partial updates given fields of an existing tradeSuggestion, field will ignore if it is null
     *
     * @param id the id of the tradeSuggestion to save.
     * @param tradeSuggestion the tradeSuggestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeSuggestion,
     * or with status {@code 400 (Bad Request)} if the tradeSuggestion is not valid,
     * or with status {@code 404 (Not Found)} if the tradeSuggestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the tradeSuggestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trade-suggestions/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TradeSuggestion> partialUpdateTradeSuggestion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TradeSuggestion tradeSuggestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update TradeSuggestion partially : {}, {}", id, tradeSuggestion);
        if (tradeSuggestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeSuggestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeSuggestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TradeSuggestion> result = tradeSuggestionService.partialUpdate(tradeSuggestion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tradeSuggestion.getId().toString())
        );
    }

    /**
     * {@code GET  /trade-suggestions} : get all the tradeSuggestions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tradeSuggestions in body.
     */
    @GetMapping("/trade-suggestions")
    public ResponseEntity<List<TradeSuggestion>> getAllTradeSuggestions(Pageable pageable) {
        log.debug("REST request to get a page of TradeSuggestions");
        Page<TradeSuggestion> page = tradeSuggestionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trade-suggestions/:id} : get the "id" tradeSuggestion.
     *
     * @param id the id of the tradeSuggestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tradeSuggestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trade-suggestions/{id}")
    public ResponseEntity<TradeSuggestion> getTradeSuggestion(@PathVariable Long id) {
        log.debug("REST request to get TradeSuggestion : {}", id);
        Optional<TradeSuggestion> tradeSuggestion = tradeSuggestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tradeSuggestion);
    }

    /**
     * {@code DELETE  /trade-suggestions/:id} : delete the "id" tradeSuggestion.
     *
     * @param id the id of the tradeSuggestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trade-suggestions/{id}")
    public ResponseEntity<Void> deleteTradeSuggestion(@PathVariable Long id) {
        log.debug("REST request to delete TradeSuggestion : {}", id);
        tradeSuggestionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
