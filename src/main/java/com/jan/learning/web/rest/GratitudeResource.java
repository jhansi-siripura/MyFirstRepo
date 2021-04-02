package com.jan.learning.web.rest;

import com.jan.learning.domain.Gratitude;
import com.jan.learning.repository.GratitudeRepository;
import com.jan.learning.service.GratitudeService;
import com.jan.learning.web.rest.errors.BadRequestAlertException;
import com.jan.learning.web.rest.util.MyDateUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
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
 * REST controller for managing {@link com.jan.learning.domain.Gratitude}.
 */
@RestController
@RequestMapping("/api")
public class GratitudeResource {

    private final Logger log = LoggerFactory.getLogger(GratitudeResource.class);

    private static final String ENTITY_NAME = "gratitude";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GratitudeService gratitudeService;

    private final GratitudeRepository gratitudeRepository;

    public GratitudeResource(GratitudeService gratitudeService, GratitudeRepository gratitudeRepository) {
        this.gratitudeService = gratitudeService;
        this.gratitudeRepository = gratitudeRepository;
    }

    /**
     * {@code POST  /gratitudes} : Create a new gratitude.
     *
     * @param gratitude the gratitude to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gratitude, or with status {@code 400 (Bad Request)} if the gratitude has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gratitudes")
    public ResponseEntity<Gratitude> createGratitude(@Valid @RequestBody Gratitude gratitude) throws URISyntaxException {
        log.debug("REST request to save Gratitude : {}", gratitude);
        if (gratitude.getId() != null) {
            throw new BadRequestAlertException("A new gratitude cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gratitude result = gratitudeService.save(gratitude);
        return ResponseEntity
            .created(new URI("/api/gratitudes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gratitudes/:id} : Updates an existing gratitude.
     *
     * @param id the id of the gratitude to save.
     * @param gratitude the gratitude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gratitude,
     * or with status {@code 400 (Bad Request)} if the gratitude is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gratitude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gratitudes/{id}")
    public ResponseEntity<Gratitude> updateGratitude(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Gratitude gratitude
    ) throws URISyntaxException {
        log.debug("REST request to update Gratitude : {}, {}", id, gratitude);
        if (gratitude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gratitude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gratitudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Gratitude result = gratitudeService.save(gratitude);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gratitude.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gratitudes/:id} : Partial updates given fields of an existing gratitude, field will ignore if it is null
     *
     * @param id the id of the gratitude to save.
     * @param gratitude the gratitude to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gratitude,
     * or with status {@code 400 (Bad Request)} if the gratitude is not valid,
     * or with status {@code 404 (Not Found)} if the gratitude is not found,
     * or with status {@code 500 (Internal Server Error)} if the gratitude couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gratitudes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Gratitude> partialUpdateGratitude(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Gratitude gratitude
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gratitude partially : {}, {}", id, gratitude);
        if (gratitude.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gratitude.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gratitudeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gratitude> result = gratitudeService.partialUpdate(gratitude);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, gratitude.getId().toString())
        );
    }

    /**
     * {@code GET  /gratitudes} : get all the gratitudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gratitudes in body.
     */
    @GetMapping("/gratitudes")
    public ResponseEntity<List<Gratitude>> getAllGratitudes(Pageable pageable) {
        log.debug("REST request to get a page of Gratitudes");
        Page<Gratitude> page = gratitudeService.findAll(pageable);
        // Page<Gratitude> page = gratitudeRepository.findAllByAchieved(true,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gratitudes/:id} : get the "id" gratitude.
     *
     * @param id the id of the gratitude to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gratitude, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gratitudes/{id}")
    public ResponseEntity<Gratitude> getGratitude(@PathVariable Long id) {
        log.debug("REST request to get Gratitude : {}", id);
        Optional<Gratitude> gratitude = gratitudeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gratitude);
    }

    /**
     * {@code DELETE  /gratitudes/:id} : delete the "id" gratitude.
     *
     * @param id the id of the gratitude to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gratitudes/{id}")
    public ResponseEntity<Void> deleteGratitude(@PathVariable Long id) {
        log.debug("REST request to delete Gratitude : {}", id);
        gratitudeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /gratitudes} : get all the gratitudes achieved .
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gratitudes in body.
     */
    @GetMapping("/gratitudes/achieved")
    public ResponseEntity<List<Gratitude>> getAllGratitudesAchieved(Pageable pageable) {
        log.debug("REST request to get a page of Achieved ");
        Page<Gratitude> page = gratitudeRepository.findAllByAchieved(true, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gratitudes} : get all the gratitudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gratitudes in body.
     */
    @GetMapping("/gratitudes/loved")
    public ResponseEntity<List<Gratitude>> getAllGratitudesLoved(Pageable pageable) {
        log.debug("REST request to get a page of Achieved ");
        //Page<Gratitude> page = gratitudeRepository.findAllByLoved(true,pageable);

        //        LocalDate startDt= LocalDate.of(2021,04, 01);
        //        LocalDate endDt= LocalDate.of(2021,04, 10);

        LocalDate firstDayofThisMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayofThisMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        Page<Gratitude> page = gratitudeRepository.findAllByCreatedDateBetweenAndLoved(
            firstDayofThisMonth,
            lastDayofThisMonth,
            true,
            pageable
        );

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gratitudes} : get all the gratitudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gratitudes in body.
     */
    @GetMapping("/gratitudes/today")
    public ResponseEntity<List<Gratitude>> getAllGratitudesToday(Pageable pageable) {
        log.debug("REST request to get a page of Today ");

        //Date todayWithZeroTime = MyDateUtil.getDateWithoutTime(new Date());

        LocalDate todayWithZeroTime = LocalDate.now();

        Page<Gratitude> page = gratitudeRepository.findAllByCreatedDate(todayWithZeroTime, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gratitudes} : get all the gratitudes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gratitudes in body.
     */
    @GetMapping("/gratitudes/yday")
    public ResponseEntity<List<Gratitude>> getAllGratitudesYday(Pageable pageable) {
        log.debug("REST request to get a page of Yesterday ");
        //Date ydayWithZeroTime = MyDateUtil.getDateWithoutTime(MyDateUtil.getYesterday());

        LocalDate ydayWithZeroTime = LocalDate.now().minusDays(1);

        Page<Gratitude> page = gratitudeRepository.findAllByCreatedDate(ydayWithZeroTime, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
