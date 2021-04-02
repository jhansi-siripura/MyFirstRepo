package com.jan.learning.repository;

import com.jan.learning.domain.TradeWish;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TradeWish entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TradeWishRepository extends JpaRepository<TradeWish, Long> {}
