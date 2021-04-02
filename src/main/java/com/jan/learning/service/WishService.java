package com.jan.learning.service;

import com.jan.learning.domain.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Wish}.
 */
public interface WishService {
    /**
     * Save a wish.
     *
     * @param wish the entity to save.
     * @return the persisted entity.
     */
    Wish save(Wish wish);

    /**
     * Partially updates a wish.
     *
     * @param wish the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Wish> partialUpdate(Wish wish);

    /**
     * Get all the wishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Wish> findAll(Pageable pageable);

    /**
     * Get the "id" wish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Wish> findOne(Long id);

    /**
     * Delete the "id" wish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
