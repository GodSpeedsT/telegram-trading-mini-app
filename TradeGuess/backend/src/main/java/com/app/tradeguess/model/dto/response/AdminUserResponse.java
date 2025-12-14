package com.app.tradeguess.model.dto.response;

import com.app.tradeguess.model.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {
    private Long id;
    private Long telegramId;
    private String username;
    private String firstName;
    private Role role;
    private Boolean isBlocked;
    private LocalDateTime createdAt;
    private Long totalAttempts;
    private Long correctAttempts;
    private Double accuracy;
}
