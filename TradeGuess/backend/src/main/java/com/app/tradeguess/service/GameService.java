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
    private final ObjectMapper objectMapper;
    private final AttemptService attemptService;

    public ChartResponse getTrainingChart(Long telegramId,String mode) {
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        int attemptsLeft = attemptService.getRemainingAttempts(user.getId());
        if (attemptsLeft <= 0) {
            throw new RuntimeException("Дневной лимит исчерпан. Попробуйте завтра!");
        }

        ChartSegment segment = getRandomSegment();
        if (segment == null) {
            throw new RuntimeException("Нет доступных графиков. Добавьте данные в chart_segments.");
        }

        List<ChartResponse.Candle> displayCandles = parseCandles(segment.getDisplayCandles(),ChartResponse.Candle.class);

        return ChartResponse.builder()
                .segmentId(segment.getId())
                .candles(displayCandles)
                .attemptsLeft(attemptsLeft == Integer.MAX_VALUE ? null : attemptsLeft)
                .build();
    }



    @Transactional
    public GuessResponse processGuess(Long telegramId, GuessRequest request,String mode) {
        User user = userRepository.findByTelegramId(telegramId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден. ID: " + telegramId));

        ChartSegment segment = chartSegmentRepository.findById(request.getSegmentId())
                .orElseThrow(() -> new RuntimeException("Сегмент не найден. ID: "));

        boolean isCorrect = checkIfCorrect(segment, request.getDirection());

        saveAttempt(user, segment, request.getDirection(), isCorrect);
        attemptService.incrementAttempts(user.getId());

        statsService.evictUserStatsCacheByTelegramId(telegramId);

        List<GuessResponse.Candle> resultCandles = parseCandles(segment.getResultCandles(), GuessResponse.Candle.class);

        if("single".equalsIgnoreCase(mode) && !resultCandles.isEmpty()) {
            resultCandles = List.of(resultCandles.get(0));
        }

        return GuessResponse.builder()
                .isCorrect(isCorrect)
                .message(isCorrect ? "Правильно!" : "Неправильно!")
                .resultCandles(resultCandles)
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

    @CacheEvict(value = "userStats", key = "#user.telegramId")
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