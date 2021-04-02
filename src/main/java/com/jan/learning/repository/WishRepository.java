package com.jan.learning.repository;

import com.jan.learning.domain.Wish;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Wish entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {}
