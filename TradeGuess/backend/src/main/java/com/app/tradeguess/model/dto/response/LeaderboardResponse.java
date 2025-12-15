package com.app.tradeguess.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class LeaderboardResponse implements Serializable {
    private String period;
    private LocalDateTime calculatedAt;
    private List<LeaderboardEntry> entries;

    @Data
    public static class LeaderboardEntry implements Serializable {
        private Integer rank;
        private UserResponse user;
        private Long attempts;
        private Long correct;
        private BigDecimal accuracy;
        private BigDecimal rating;
        private Integer change;

        @JsonProperty("isCurrentUser")
        private boolean isCurrentUser;
    }
}