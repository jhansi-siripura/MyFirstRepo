package com.jan.learning.repository;

import com.jan.learning.domain.TradeSuggestion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TradeSuggestion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TradeSuggestionRepository extends JpaRepository<TradeSuggestion, Long> {}
