package com.app.tradeguess.model.dto.request;

import lombok.Data;

@Data
public class AdminRequest {
    private String telegramUsername;
    private Long userId;
    private Boolean block;
}
