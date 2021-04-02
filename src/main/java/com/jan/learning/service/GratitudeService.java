package com.jan.learning.service;

import com.jan.learning.domain.Gratitude;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Gratitude}.
 */
public interface GratitudeService {
    /**
     * Save a gratitude.
     *
     * @param gratitude the entity to save.
     * @return the persisted entity.
     */
    Gratitude save(Gratitude gratitude);

    /**
     * Partially updates a gratitude.
     *
     * @param gratitude the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Gratitude> partialUpdate(Gratitude gratitude);

    /**
     * Get all the gratitudes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Gratitude> findAll(Pageable pageable);

    /**
     * Get the "id" gratitude.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Gratitude> findOne(Long id);

    /**
     * Delete the "id" gratitude.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
