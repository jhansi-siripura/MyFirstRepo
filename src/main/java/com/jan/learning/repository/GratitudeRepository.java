package com.jan.learning.repository;

import com.jan.learning.domain.Gratitude;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gratitude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GratitudeRepository extends JpaRepository<Gratitude, Long> {}
