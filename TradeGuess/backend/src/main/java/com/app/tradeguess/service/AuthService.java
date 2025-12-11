package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.request.TelegramAuthRequest;
import com.app.tradeguess.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse authenticateTelegramUser(TelegramAuthRequest request);
    AuthResponse authenticateSimple(TelegramAuthRequest request);
}