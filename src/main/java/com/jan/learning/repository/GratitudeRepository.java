package com.jan.learning.repository;

import com.jan.learning.domain.Gratitude;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Gratitude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GratitudeRepository extends JpaRepository<Gratitude, Long> {
    Page<Gratitude> findAllByAchieved(Boolean achieved, Pageable pageable);

    Page<Gratitude> findAllByLoved(Boolean loved, Pageable pageable);

    Page<Gratitude> findAllByCreatedDate(LocalDate dt, Pageable pageable);

    Page<Gratitude> findAllByCreatedDateBetween(LocalDate startDt, LocalDate endDt, Pageable pageable);

    Page<Gratitude> findAllByCreatedDateBetweenAndLoved(LocalDate startDt, LocalDate endDt, Boolean loved, Pageable pageable);
    //    List<GratitudeComment> findAllByGratefulDateBetween(Date startDt, Date endDt);
    //
    //
    //    @Query("select g from GratitudeComment g where g.gratefulDate <= :gratefulDate")
    //    List<GratitudeComment> findAllByGratefulDateBefore( @Param("gratefulDate") Date gratefulDate);
    //
}
