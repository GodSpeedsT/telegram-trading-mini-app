package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.TelegramAuthRequest;
import com.app.tradeguess.model.dto.response.AuthResponse;
import com.app.tradeguess.service.TelegramAuthService;
import com.app.tradeguess.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final TelegramAuthService telegramAuthService;

    @PostMapping("/telegram")
    public ResponseEntity<AuthResponse> authViaTelegram(
            @Valid @RequestBody TelegramAuthRequest request) {

        log.info("Telegram auth request for user: {}", request.getTelegramId());

        if (!telegramAuthService.validateTelegramData(request.getInitData())) {
            log.warn("Invalid Telegram data for user: {}", request.getTelegramId());
            return ResponseEntity.badRequest().build();
        }

        AuthResponse response = authService.authenticateTelegramUser(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/simple")
    public ResponseEntity<AuthResponse> simpleAuth(
            @RequestParam Long telegramId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String firstName) {

        log.info("Simple auth for telegramId: {}", telegramId);

        TelegramAuthRequest request = new TelegramAuthRequest();
        request.setTelegramId(telegramId);
        request.setUsername(username);
        request.setFirstName(firstName);

        AuthResponse response = authService.authenticateSimple(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken() {
        return ResponseEntity.ok("Token is valid");
    }
}
