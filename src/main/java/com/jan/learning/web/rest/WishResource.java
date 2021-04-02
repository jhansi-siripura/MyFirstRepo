package com.jan.learning.web.rest;

import com.jan.learning.domain.Wish;
import com.jan.learning.repository.WishRepository;
import com.jan.learning.service.WishService;
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
 * REST controller for managing {@link com.jan.learning.domain.Wish}.
 */
@RestController
@RequestMapping("/api")
public class WishResource {

    private final Logger log = LoggerFactory.getLogger(WishResource.class);

    private static final String ENTITY_NAME = "wish";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WishService wishService;

    private final WishRepository wishRepository;

    public WishResource(WishService wishService, WishRepository wishRepository) {
        this.wishService = wishService;
        this.wishRepository = wishRepository;
    }

    /**
     * {@code POST  /wishes} : Create a new wish.
     *
     * @param wish the wish to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wish, or with status {@code 400 (Bad Request)} if the wish has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wishes")
    public ResponseEntity<Wish> createWish(@Valid @RequestBody Wish wish) throws URISyntaxException {
        log.debug("REST request to save Wish : {}", wish);
        if (wish.getId() != null) {
            throw new BadRequestAlertException("A new wish cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Wish result = wishService.save(wish);
        return ResponseEntity
            .created(new URI("/api/wishes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wishes/:id} : Updates an existing wish.
     *
     * @param id the id of the wish to save.
     * @param wish the wish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wish,
     * or with status {@code 400 (Bad Request)} if the wish is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wishes/{id}")
    public ResponseEntity<Wish> updateWish(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Wish wish)
        throws URISyntaxException {
        log.debug("REST request to update Wish : {}, {}", id, wish);
        if (wish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Wish result = wishService.save(wish);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wish.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wishes/:id} : Partial updates given fields of an existing wish, field will ignore if it is null
     *
     * @param id the id of the wish to save.
     * @param wish the wish to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wish,
     * or with status {@code 400 (Bad Request)} if the wish is not valid,
     * or with status {@code 404 (Not Found)} if the wish is not found,
     * or with status {@code 500 (Internal Server Error)} if the wish couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wishes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Wish> partialUpdateWish(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Wish wish
    ) throws URISyntaxException {
        log.debug("REST request to partial update Wish partially : {}, {}", id, wish);
        if (wish.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wish.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wishRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Wish> result = wishService.partialUpdate(wish);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, wish.getId().toString())
        );
    }

    /**
     * {@code GET  /wishes} : get all the wishes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wishes in body.
     */
    @GetMapping("/wishes")
    public ResponseEntity<List<Wish>> getAllWishes(Pageable pageable) {
        log.debug("REST request to get a page of Wishes");
        Page<Wish> page = wishService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wishes/:id} : get the "id" wish.
     *
     * @param id the id of the wish to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wish, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wishes/{id}")
    public ResponseEntity<Wish> getWish(@PathVariable Long id) {
        log.debug("REST request to get Wish : {}", id);
        Optional<Wish> wish = wishService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wish);
    }

    /**
     * {@code DELETE  /wishes/:id} : delete the "id" wish.
     *
     * @param id the id of the wish to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wishes/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        log.debug("REST request to delete Wish : {}", id);
        wishService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
