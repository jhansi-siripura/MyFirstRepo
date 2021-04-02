package com.jan.learning.service;

import com.jan.learning.domain.TradeSuggestion;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link TradeSuggestion}.
 */
public interface TradeSuggestionService {
    /**
     * Save a tradeSuggestion.
     *
     * @param tradeSuggestion the entity to save.
     * @return the persisted entity.
     */
    TradeSuggestion save(TradeSuggestion tradeSuggestion);

    /**
     * Partially updates a tradeSuggestion.
     *
     * @param tradeSuggestion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TradeSuggestion> partialUpdate(TradeSuggestion tradeSuggestion);

    /**
     * Get all the tradeSuggestions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TradeSuggestion> findAll(Pageable pageable);

    /**
     * Get the "id" tradeSuggestion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TradeSuggestion> findOne(Long id);

    /**
     * Delete the "id" tradeSuggestion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
