package com.app.tradeguess.service;

import com.app.tradeguess.repository.GuessAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final GuessAttemptRepository guessAttemptRepository;

    public Map<String,Object> getUserStats(Long userId) {
        Long total = guessAttemptRepository.countByUserId(userId);
        Long correct = guessAttemptRepository.countByUserIdAndIsCorrect(userId, true);
        Double accuracy = total > 0 ? (correct * 100.0 / total) : 0.0;

        return Map.of(
          "totalAttempts", total,
          "correctAttempts", correct,
          "accuracy", String.format("%.1f%%", accuracy)
        );
    }
}
