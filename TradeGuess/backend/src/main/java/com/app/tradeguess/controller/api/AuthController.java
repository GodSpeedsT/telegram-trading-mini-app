package com.app.tradeguess.controller.api;

import com.app.tradeguess.security.JwtTokenProvider;
import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.model.dto.response.AuthResponse;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<ApiResponse<AuthResponse>> auth(@Valid @RequestBody AuthRequest request) {
        User user = userService.findOrCreateUser(request);

        String token = jwtTokenProvider.generateToken(user.getUsername());

        AuthResponse response = AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .token(token)
                .role(user.getRole())
                .isNewUser(user.getCreatedAt().isAfter(java.time.LocalDateTime.now().minusDays(1)))
                .build();

        return ResponseEntity.ok(ApiResponse.success(response, "Авторизация успешна"));
    }
}