package com.app.tradeguess.model.dto.request;

import lombok.Data;

@Data
public class TelegramAuthRequest {
    private Long id;
    private String username;
    private String firstName;
}