package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.response.LeaderboardResponse;
import com.app.tradeguess.repository.GuessAttemptRepository;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final GuessAttemptRepository guessAttemptRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "leaderboard", key = "#period + '_' + #page + '_' + #size")
    public LeaderboardResponse getLeaderboard(String period, int page, int size) {
        log.info("Calculating leaderboard for period {} (not from cache)", period);

        LocalDate startDate = getStartDateForPeriod(period);
        String startDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        List<Object[]> rawData = guessAttemptRepository.getLeaderboardData(
                startDateStr,
                size * (page + 1)
        );

        List<LeaderboardResponse.LeaderboardEntry> entries = new ArrayList<>();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, rawData.size());

        for (int i = startIndex; i < endIndex; i++) {
            Object[] row = rawData.get(i);
            Long userId = ((Number) row[0]).longValue();
            Long attempts = ((Number) row[1]).longValue();
            Long correct = ((Number) row[2]).longValue();
            Double accuracy = ((Number) row[3]).doubleValue();

            var user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                double rating = accuracy * Math.log(attempts + 1);

                var entry = new LeaderboardResponse.LeaderboardEntry();
                entry.setRank(i - startIndex + 1);
                entry.setUser(userToResponse(user));
                entry.setAttempts(attempts);
                entry.setCorrect(correct);
                entry.setAccuracy(BigDecimal.valueOf(accuracy).setScale(2, RoundingMode.HALF_UP));
                entry.setRating(BigDecimal.valueOf(rating).setScale(2, RoundingMode.HALF_UP));
                entry.setChange(0); // Пока не реализовано
                entry.setCurrentUser(false);

                entries.add(entry);
            }
        }

        return LeaderboardResponse.builder()
                .period(period)
                .calculatedAt(LocalDateTime.now())
                .entries(entries)
                .build();
    }

    private LocalDate getStartDateForPeriod(String period) {
        LocalDate now = LocalDate.now();
        return switch (period.toLowerCase()) {
            case "weekly" -> now.minusWeeks(1);
            case "monthly" -> now.minusMonths(1);
            case "alltime" -> LocalDate.of(2000, 1, 1); // Начало времен
            default -> now.minusWeeks(1); // По умолчанию неделя
        };
    }

    private com.app.tradeguess.model.dto.response.UserResponse userToResponse(
            com.app.tradeguess.model.entity.User user) {
        var response = new com.app.tradeguess.model.dto.response.UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setFirstName(user.getFirstName());
        response.setLastName(null); // У вас нет этого поля
        response.setIsPremium(false); // Пока не реализовано
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}