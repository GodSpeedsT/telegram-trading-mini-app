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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GameService {

    private final ChartSegmentRepository chartSegmentRepository;
    private final GuessAttemptRepository guessAttemptRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public ChartResponse getTrainingChart(Long userId) {
        Integer attemptsToday = getDailyAttempts(userId);
        if (attemptsToday >= 10) {
            throw new RuntimeException("Days limit exceeded");
        }

        ChartSegment segment = getRandomSegment();

        return ChartResponse.builder()
                .segmentId(segment.getId())
                .candles(parseCandles(segment.getDisplayCandles(),ChartResponse.Candle.class))
                .attemptsLeft(10 - attemptsToday - 1)
                .build();
    }

    public GuessResponse processGuess(
            Long userId,
            GuessRequest request
    ) {
        ChartSegment segment = chartSegmentRepository.findById(request.getSegmentId())
                .orElseThrow(() -> new RuntimeException("Segment not found"));

        boolean isCorrect = checkIfCorrect(segment, request.getDirection());

        saveAttempt(userId, segment, request.getDirection(), isCorrect);
        incrementDailyAttempts(userId);

        return GuessResponse.builder()
                .isCorrect(isCorrect)
                .message(isCorrect ? "Правильно" : "Неправильно")
                .resultCandles(parseCandles(segment.getResultCandles(),GuessResponse.Candle.class))
                .priceChangePercent(calculatePriceChangePercent(segment))
                .build();
    }

    private boolean checkIfCorrect(
            ChartSegment segment,
            TradeDirection direction) {
        Integer correctDirection = segment.getCalculatedDirection();

        if (direction == TradeDirection.LONG) {
            return correctDirection == 1;
        } else if (direction == TradeDirection.SHORT) {
            return correctDirection == -1;
        }
        return false;
    }

    private void saveAttempt(
            Long userId,
            ChartSegment segment,
            TradeDirection direction,
            boolean isCorrect) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GuessAttempt attempt = new GuessAttempt();
        attempt.setUser(user);
        attempt.setChartSegment(segment);
        attempt.setUserDirection(direction);
        attempt.setIsCorrect(isCorrect);
        attempt.setAttemptedAt(LocalDateTime.now());

        guessAttemptRepository.save(attempt);
    }

    private ChartSegment getRandomSegment() {
        Long total = chartSegmentRepository.count();
        if (total == 0) {
            throw new RuntimeException("No segments found");
        }
        Long randomId = ThreadLocalRandom.current().nextLong(1, total + 1);

        return chartSegmentRepository.findById(randomId)
                .orElseGet(() -> {
                    return chartSegmentRepository.findAll().stream()
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Segment not found"));
                });
    }

    private Integer getDailyAttempts(Long userId) {

        String key = "user:" + userId + ":attempts:" + LocalDate.now();
        String attempts = redisTemplate.opsForValue().get(key);
        return attempts != null ? Integer.parseInt(attempts) : 0;
    }

    private void incrementDailyAttempts(Long userId) {
        String key = "user:" + userId + ":attempts:" + LocalDate.now();
        Long attempts = redisTemplate.opsForValue().increment(key);

        if (attempts == 1) {
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }
    }

    private <T> List<T> parseCandles(String candlesJson, Class<T> candleClass) {
        try {
            return objectMapper.readValue(
                    candlesJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, candleClass)
            );
        } catch (Exception e) {
            throw new RuntimeException("Неверный формат данных свечей");
        }
    }

    private ChartResponse.Candle mapToCandleDto(Map<String, Object> rawCandle) {
        ChartResponse.Candle candle = new ChartResponse.Candle();

        Object timestamp = rawCandle.get("t");
        if (timestamp instanceof Number) {
            candle.setTimestamp(((Number) timestamp).longValue());
        } else if (timestamp instanceof String) {
            candle.setTimestamp(Long.parseLong((String) timestamp));
        }

        candle.setOpen(convertToDouble(rawCandle.get("o")));
        candle.setHigh(convertToDouble(rawCandle.get("h")));
        candle.setLow(convertToDouble(rawCandle.get("l")));
        candle.setClose(convertToDouble(rawCandle.get("c")));
        candle.setVolume(convertToDouble(rawCandle.get("v")));

        return candle;
    }

    private Double convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        return 0.0;
    }

    private Double calculatePriceChangePercent(ChartSegment segment) {
        if (segment.getPriceAtDecision() == null || segment.getPriceAtTarget() == null) {
            return 0.0;
        }

        double change = segment.getPriceAtTarget() - segment.getPriceAtDecision();
        double percent = (change / segment.getPriceAtDecision()) * 100;

        return BigDecimal.valueOf(percent)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
