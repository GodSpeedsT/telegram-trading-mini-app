package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.model.dto.response.AuthResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<AuthResponse>> auth(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Авторизация успешна"));
    }
}