package com.app.tradeguess.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// TelegramAuthRequest.java
@Data
public class TelegramAuthRequest {
    private Long id;
    private String username;
    private String firstName;
}