package com.app.tradeguess.repository;

import com.app.tradeguess.model.entity.GuessAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuessAttemptRepository extends JpaRepository<GuessAttempt, Long> {
    Long countByUserId(Long userId);
    Long countByUserIdAndIsCorrect(Long userId, Boolean isCorrect);

    @Query("SELECT g FROM GuessAttempt g WHERE g.user.id = :userId ORDER BY g.attemptedAt DESC")
    List<GuessAttempt> findAllByUserIdOrderByAttemptedAtDesc(@Param("userId") Long userId);

    @Query(value = """
    SELECT user_id, 
           COUNT(*) as attempts_count,
           SUM(CASE WHEN is_correct THEN 1 ELSE 0 END) as correct_count,
           CASE WHEN COUNT(*) > 0 
                THEN ROUND(SUM(CASE WHEN is_correct THEN 1 ELSE 0 END) * 100.0 / COUNT(*), 2)
                ELSE 0 
           END as accuracy
    FROM guess_attempts 
    WHERE attempted_at >= CAST(:startDate AS timestamp)
    GROUP BY user_id
    ORDER BY accuracy DESC, attempts_count DESC
    LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> getLeaderboardData(@Param("startDate") String startDate,
                                      @Param("limit") int limit);
}