package com.app.tradeguess.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsResponse {
    private Long totalAttempts;
    private Long correctAttempts;
    private Double accuracy;
    private Integer currentStreak;
    private Integer bestStreak;
    private Integer dailyAttempts;
    private Integer attemptsLeft;
}
