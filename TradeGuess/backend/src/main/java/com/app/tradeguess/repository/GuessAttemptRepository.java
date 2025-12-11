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
}