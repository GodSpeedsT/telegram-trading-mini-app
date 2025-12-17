package com.app.tradeguess.model.dto.response;

import com.app.tradeguess.model.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UserStatsResponse {
    private Long userId;
    private String username;
    private String firstName;
    private Long totalAttempts;
    private Long correctAttempts;
    private BigDecimal accuracy;
    private Role role;
    private LocalDateTime createdAt;
}