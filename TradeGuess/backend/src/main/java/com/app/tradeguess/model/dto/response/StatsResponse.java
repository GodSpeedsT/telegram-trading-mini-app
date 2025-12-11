package com.app.tradeguess.model.dto.response;

import com.app.tradeguess.model.enums.TradeDirection;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class StatsResponse {
    private OverviewStats overview;
    private DailyStats today;
    private StreakInfo streaks;
    private List<RecentAttempt> recentAttempts;

    @Data
    public static class OverviewStats {
        private Long totalAttempts;
        private Long correctAttempts;
        private BigDecimal accuracy;
        private BigDecimal rating;
        private Integer weeklyRank;
        private Integer monthlyRank;
        private LocalDateTime firstAttemptAt;
        private LocalDateTime lastAttemptAt;
    }

    @Data
    public static class DailyStats {
        private Long attempts;
        private Long correct;
        private BigDecimal accuracy;
        private Integer attemptsLeft;
        private LocalDateTime resetTime;
    }

    @Data
    public static class StreakInfo {
        private Integer current;
        private Integer best;
        private Integer todayInStreak;
    }

    @Data
    public static class RecentAttempt {
        private Long id;
        private Boolean isCorrect;
        private TradeDirection direction;
        private TradeDirection actualDirection;
        private BigDecimal priceChangePercent;
        private LocalDateTime attemptedAt;
        private String symbol;
        private String timeframe;
    }
}