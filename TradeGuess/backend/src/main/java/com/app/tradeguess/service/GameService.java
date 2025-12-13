package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.request.GuessRequest;
import com.app.tradeguess.model.dto.response.ChartResponse;
import com.app.tradeguess.model.dto.response.GuessResponse;
import com.app.tradeguess.model.entity.ChartSegment;
import com.app.tradeguess.model.entity.GuessAttempt;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.model.enums.TradeDirection;
import com.app.tradeguess.repository.ChartSegmentRepository;
import com.app.tradeguess.repository.GuessAttemptRepository;
import com.app.tradeguess.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {

    private final ChartSegmentRepository chartSegmentRepository;
    private final GuessAttemptRepository guessAttemptRepository;
    private final UserRepository userRepository;
    private final StatsService statsService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public ChartResponse getTrainingChart(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Пользователь не найден.");
        }

        Integer attemptsToday = getDailyAttempts(userId);
        if (attemptsToday >= 10) {
            throw new RuntimeException("Дневной лимит исчерпан. Попробуйте завтра!");
        }

        ChartSegment segment = getRandomSegment();

        if (segment == null) {
            throw new RuntimeException("Нет доступных графиков. Добавьте данные в chart_segments.");
        }

        return ChartResponse.builder()
                .segmentId(segment.getId())
                .candles(parseCandles(segment.getDisplayCandles(), ChartResponse.Candle.class))
                .attemptsLeft(10 - attemptsToday)
                .build();
    }

    @Transactional
    public GuessResponse processGuess(Long userId, GuessRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден. ID: " + userId));

        ChartSegment segment = chartSegmentRepository.findById(request.getSegmentId())
                .orElseThrow(() -> new RuntimeException("Сегмент не найден. ID: " + request.getSegmentId()));

        boolean isCorrect = checkIfCorrect(segment, request.getDirection());

        saveAttempt(user, segment, request.getDirection(), isCorrect);
        incrementDailyAttempts(userId);

        statsService.evictUserStatsCache(userId);

        return GuessResponse.builder()
                .isCorrect(isCorrect)
                .message(isCorrect ? "Правильно!" : "Неправильно!")
                .resultCandles(parseCandles(segment.getResultCandles(), GuessResponse.Candle.class))
                .priceChangePercent(calculatePriceChangePercent(segment))
                .build();
    }

    private boolean checkIfCorrect(ChartSegment segment, TradeDirection direction) {
        Integer correctDirection = segment.getCalculatedDirection();

        if (direction == TradeDirection.LONG) {
            return correctDirection == 1;
        } else if (direction == TradeDirection.SHORT) {
            return correctDirection == -1;
        }
        return false;
    }

    @CacheEvict(value = "userStats", key = "#user.id")
    public void saveAttempt(User user, ChartSegment segment,
                             TradeDirection direction, boolean isCorrect) {
        GuessAttempt attempt = new GuessAttempt();
        attempt.setUser(user);
        attempt.setChartSegment(segment);
        attempt.setUserDirection(direction);
        attempt.setIsCorrect(isCorrect);
        attempt.setAttemptedAt(LocalDateTime.now());

        guessAttemptRepository.save(attempt);
        log.info("Сохранена попытка пользователя {}: {}", user.getId(),
                isCorrect ? "правильно" : "неправильно");
    }

    private ChartSegment getRandomSegment() {
        long total = chartSegmentRepository.count();
        if (total == 0) {
            throw new RuntimeException("В базе данных нет сегментов графиков. Добавьте данные.");
        }

        return chartSegmentRepository.findRandom()
                .orElseGet(() -> {
                    long randomId = ThreadLocalRandom.current().nextLong(1, total + 1);
                    return chartSegmentRepository.findById(randomId)
                            .orElseThrow(() -> new RuntimeException("Сегмент не найден"));
                });
    }

    private Integer getDailyAttempts(Long userId) {
        String key = "user:" + userId + ":attempts:" + LocalDate.now();
        String attempts = redisTemplate.opsForValue().get(key);
        log.debug("Daily attempts for user {}: {}", userId, attempts);
        return attempts != null ? Integer.parseInt(attempts) : 0;
    }

    private void incrementDailyAttempts(Long userId) {
        String key = "user:" + userId + ":attempts:" + LocalDate.now();
        Long attempts = redisTemplate.opsForValue().increment(key);

        if (attempts == 1) {
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }

        log.info("Увеличено количество попыток для пользователя {}: {}", userId, attempts);
    }

    private <T> List<T> parseCandles(String candlesJson, Class<T> candleClass) {
        try {
            if (candlesJson == null || candlesJson.trim().isEmpty()) {
                return List.of();
            }
            return objectMapper.readValue(
                    candlesJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, candleClass)
            );
        } catch (Exception e) {
            log.error("Ошибка парсинга свечей: {}", e.getMessage());
            throw new RuntimeException("Неверный формат данных свечей");
        }
    }

    private Double calculatePriceChangePercent(ChartSegment segment) {
        if (segment.getPriceAtDecision() == null || segment.getPriceAtTarget() == null) {
            return 0.0;
        }

        BigDecimal decisionPrice = segment.getPriceAtDecision();
        BigDecimal targetPrice = segment.getPriceAtTarget();

        if (decisionPrice.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }

        BigDecimal change = targetPrice.subtract(decisionPrice);
        BigDecimal percent = change.divide(decisionPrice, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return percent.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}