package com.app.tradeguess.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequest {
    @NotNull
    private Long telegramId;
    private String username;
    private String firstName;
}