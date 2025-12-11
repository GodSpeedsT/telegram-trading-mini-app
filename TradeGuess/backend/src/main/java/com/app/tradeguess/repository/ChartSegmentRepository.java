package com.app.tradeguess.repository;

import com.app.tradeguess.model.entity.ChartSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChartSegmentRepository extends JpaRepository<ChartSegment, Long> {
    @Query(value = "SELECT * FROM chart_segments ORDER BY RANDOM() LIMIT 1",
            nativeQuery = true)
    Optional<ChartSegment> findRandom();
}
