package com.jan.learning.service;

import com.jan.learning.domain.TradeWish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TradeWish}.
 */
public interface TradeWishService {
    /**
     * Save a tradeWish.
     *
     * @param tradeWish the entity to save.
     * @return the persisted entity.
     */
    TradeWish save(TradeWish tradeWish);

    /**
     * Partially updates a tradeWish.
     *
     * @param tradeWish the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TradeWish> partialUpdate(TradeWish tradeWish);

    /**
     * Get all the tradeWishes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TradeWish> findAll(Pageable pageable);

    /**
     * Get the "id" tradeWish.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TradeWish> findOne(Long id);

    /**
     * Delete the "id" tradeWish.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
