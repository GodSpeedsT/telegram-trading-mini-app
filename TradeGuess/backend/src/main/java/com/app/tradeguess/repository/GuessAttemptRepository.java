package com.app.tradeguess.repository;

import com.app.tradeguess.model.GuessAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GuessAttemptRepository extends JpaRepository<GuessAttempt, Long> {

    List<GuessAttempt> findByUserIdOrderByAttemptedAtDesc(Long userId);

    @Query("SELECT COUNT(a) FROM GuessAttempt a WHERE a.user.id = :userId " +
            "AND DATE(a.attemptedAt) = CURRENT_DATE")
    int countTodayAttempts(@Param("userId") Long userId);

    @Query("SELECT a FROM GuessAttempt a WHERE a.user.id = :userId " +
            "ORDER BY a.attemptedAt DESC LIMIT :limit")
    List<GuessAttempt> findRecentAttempts(@Param("userId") Long userId,
                                          @Param("limit") int limit);
}