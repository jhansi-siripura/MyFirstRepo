package com.jan.learning.web.rest;

import com.jan.learning.domain.TradeWish;
import com.jan.learning.repository.TradeWishRepository;
import com.jan.learning.service.TradeWishService;
import com.jan.learning.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.jan.learning.domain.TradeWish}.
 */
@RestController
@RequestMapping("/api")
public class TradeWishResource {

    private final Logger log = LoggerFactory.getLogger(TradeWishResource.class);

    private static final String ENTITY_NAME = "tradeWish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TradeWishService tradeWishService;

    private final TradeWishRepository tradeWishRepository;

    public TradeWishResource(TradeWishService tradeWishService, TradeWishRepository tradeWishRepository) {
        this.tradeWishService = tradeWishService;
        this.tradeWishRepository = tradeWishRepository;
    }

    /**
     * {@code POST  /trade-wishes} : Create a new tradeWish.
     *
     * @param tradeWish the tradeWish to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tradeWish, or with status {@code 400 (Bad Request)} if the tradeWish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trade-wishes")
    public ResponseEntity<TradeWish> createTradeWish(@Valid @RequestBody TradeWish tradeWish) throws URISyntaxException {
        log.debug("REST request to save TradeWish : {}", tradeWish);
        if (tradeWish.getId() != null) {
            throw new BadRequestAlertException("A new tradeWish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TradeWish result = tradeWishService.save(tradeWish);
        return ResponseEntity
            .created(new URI("/api/trade-wishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trade-wishes/:id} : Updates an existing tradeWish.
     *
     * @param id the id of the tradeWish to save.
     * @param tradeWish the tradeWish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeWish,
     * or with status {@code 400 (Bad Request)} if the tradeWish is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tradeWish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trade-wishes/{id}")
    public ResponseEntity<TradeWish> updateTradeWish(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TradeWish tradeWish
    ) throws URISyntaxException {
        log.debug("REST request to update TradeWish : {}, {}", id, tradeWish);
        if (tradeWish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeWish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeWishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TradeWish result = tradeWishService.save(tradeWish);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tradeWish.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trade-wishes/:id} : Partial updates given fields of an existing tradeWish, field will ignore if it is null
     *
     * @param id the id of the tradeWish to save.
     * @param tradeWish the tradeWish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tradeWish,
     * or with status {@code 400 (Bad Request)} if the tradeWish is not valid,
     * or with status {@code 404 (Not Found)} if the tradeWish is not found,
     * or with status {@code 500 (Internal Server Error)} if the tradeWish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trade-wishes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TradeWish> partialUpdateTradeWish(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TradeWish tradeWish
    ) throws URISyntaxException {
        log.debug("REST request to partial update TradeWish partially : {}, {}", id, tradeWish);
        if (tradeWish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tradeWish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tradeWishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TradeWish> result = tradeWishService.partialUpdate(tradeWish);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tradeWish.getId().toString())
        );
    }

    /**
     * {@code GET  /trade-wishes} : get all the tradeWishes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tradeWishes in body.
     */
    @GetMapping("/trade-wishes")
    public ResponseEntity<List<TradeWish>> getAllTradeWishes(Pageable pageable) {
        log.debug("REST request to get a page of TradeWishes");
        Page<TradeWish> page = tradeWishService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trade-wishes/:id} : get the "id" tradeWish.
     *
     * @param id the id of the tradeWish to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tradeWish, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trade-wishes/{id}")
    public ResponseEntity<TradeWish> getTradeWish(@PathVariable Long id) {
        log.debug("REST request to get TradeWish : {}", id);
        Optional<TradeWish> tradeWish = tradeWishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tradeWish);
    }

    /**
     * {@code DELETE  /trade-wishes/:id} : delete the "id" tradeWish.
     *
     * @param id the id of the tradeWish to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trade-wishes/{id}")
    public ResponseEntity<Void> deleteTradeWish(@PathVariable Long id) {
        log.debug("REST request to delete TradeWish : {}", id);
        tradeWishService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
