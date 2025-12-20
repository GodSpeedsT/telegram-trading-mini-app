package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.response.StatsResponse;
import com.app.tradeguess.model.entity.GuessAttempt;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.repository.GuessAttemptRepository;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {
    private final GuessAttemptRepository guessAttemptRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Cacheable(value = "userStats", key = "#telegramId")
    public StatsResponse getUserStats(Long telegramId) {
        log.info("Calculating stats for user {}", telegramId);

        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        Long totalAttempts = guessAttemptRepository.countByUserId(userId);
        Long correctAttempts = guessAttemptRepository.countByUserIdAndIsCorrect(userId, true);
        Double accuracy = totalAttempts > 0 ?
                (correctAttempts * 100.0 / totalAttempts) : 0.0;

        Integer dailyAttempts = getDailyAttempts(userId);
        Integer currentStreak = calculateCurrentStreak(userId);
        Integer bestStreak = calculateBestStreak(userId);

        return StatsResponse.builder()
                .totalAttempts(totalAttempts)
                .correctAttempts(correctAttempts)
                .accuracy(Math.round(accuracy * 100.0) / 100.0)
                .dailyAttempts(dailyAttempts)
                .attemptsLeft(Math.max(0, 10 - dailyAttempts))
                .currentStreak(currentStreak)
                .bestStreak(bestStreak)
                .build();
    }

    @CacheEvict(value = "userStats", key = "#telegramId")
    public void evictUserStatsCache(Long telegramId) {
        log.info("Evicting stats cache for user {}", telegramId);
    }

    @CacheEvict(value = "userStats", key = "#telegramId")
    public void evictUserStatsCacheByTelegramId(Long telegramId) {
        log.info("Evicting stats cache for user {}", telegramId);
    }


    private Integer getDailyAttempts(Long userId) {
        String key = "user:" + userId + ":attempts:" + LocalDate.now();
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null ? Integer.parseInt(attempts) : 0;
    }

    private Integer calculateBestStreak(Long userId) {
        List<GuessAttempt> attempts = guessAttemptRepository.findAllByUserIdOrderByAttemptedAtDesc(userId);

        int bestStreak = 0;
        int currentStreak = 0;

        for (GuessAttempt attempt : attempts) {
            if (Boolean.TRUE.equals(attempt.getIsCorrect())) {
                currentStreak++;
                bestStreak = Math.max(bestStreak, currentStreak);
            } else {
                currentStreak = 0;
            }
        }

        return bestStreak;
    }

    private Integer calculateCurrentStreak(Long userId) {
        List<GuessAttempt> attempts = guessAttemptRepository.findAllByUserIdOrderByAttemptedAtDesc(userId);

        int currentStreak = 0;

        for (GuessAttempt attempt : attempts) {
            if (Boolean.TRUE.equals(attempt.getIsCorrect())) {
                currentStreak++;
            } else {
                break;
            }
        }

        return currentStreak;
    }

    @CacheEvict(value = "userStats", key = "#telegramId")
    public void evictUserStats(Long telegramId) {
        log.info("Evicting stats cache for user {}", telegramId);
    }
}