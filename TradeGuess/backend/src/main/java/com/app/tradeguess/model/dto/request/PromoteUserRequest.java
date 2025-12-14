package com.app.tradeguess.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PromoteUserRequest {
    @NotBlank(message = "Username is required")
    private String telegramUsername;
}
