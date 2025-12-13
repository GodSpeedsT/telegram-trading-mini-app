package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.model.dto.response.AuthResponse;
import com.app.tradeguess.service.UserService;
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

    private final UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<ApiResponse<AuthResponse>> auth(
            @Valid @RequestBody AuthRequest request
    ) {
        AuthResponse response = userService.findOrCreateUser(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Авторизация успешна"));
    }
}
