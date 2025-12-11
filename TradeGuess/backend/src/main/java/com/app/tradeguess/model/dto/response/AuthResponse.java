package com.app.tradeguess.model.dto.response;

import lombok.Data;


@Data
public class AuthResponse {
    private String token;
    private UserResponse user;
    private boolean isNewUser;
}