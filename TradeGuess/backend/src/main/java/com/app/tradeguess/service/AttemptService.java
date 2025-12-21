package com.app.tradeguess.service;

import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AttemptService {

    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    private static final int DAILY_LIMIT = 10;

    public void resetDailyAttempts(Long userId) {
        String key = buildKey(userId);
        redisTemplate.delete(key);
    }

    public int getRemainingAttempts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.getRole().isAdmin()) {
            return Integer.MAX_VALUE;
        }

        String key = buildKey(userId);
        String attempts = redisTemplate.opsForValue().get(key);
        int used = attempts != null ? Integer.parseInt(attempts) : 0;
        return Math.max(0, DAILY_LIMIT - used);
    }

    public void incrementAttempts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.getRole().isAdmin()) {
            return;
        }

        String key = buildKey(userId);
        Long attempts = redisTemplate.opsForValue().increment(key);

        if (attempts == 1) {
            redisTemplate.expire(key, Duration.ofDays(1));
        }
    }

    private String buildKey(Long userId) {
        return "user:" + userId + ":attempts:" + LocalDate.now();
    }
}

