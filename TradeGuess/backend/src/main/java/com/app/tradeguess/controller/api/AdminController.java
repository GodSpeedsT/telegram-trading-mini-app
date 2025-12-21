package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.PromoteUserRequest;
import com.app.tradeguess.model.dto.response.AdminUserResponse;
import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<AdminUserResponse>>> getAllUsers(
            @RequestParam(required = false) String search) {

        List<AdminUserResponse> users;
        if (search != null && !search.trim().isEmpty()) {
            users = adminService.searchUsers(search);
        } else {
            users = adminService.getAllUsers();
        }

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminUserResponse>> getUserById(
            @PathVariable Long userId) {

        AdminUserResponse user = adminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping("/users/promote")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminUserResponse>> promoteToAdmin(
            @RequestBody PromoteUserRequest request,
            @RequestParam Long adminId) {

        AdminUserResponse response = adminService.promoteToAdmin(
                request.getTelegramUsername(), adminId);

        return ResponseEntity.ok(ApiResponse.success(response,
                "Пользователь @" + request.getTelegramUsername() + " назначен администратором"));
    }

    @PostMapping("/users/{userId}/demote")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminUserResponse>> demoteFromAdmin(
            @PathVariable Long userId,
            @RequestParam Long adminId) {

        AdminUserResponse response = adminService.demoteFromAdmin(userId, adminId);
        return ResponseEntity.ok(ApiResponse.success(response,
                "Администратор понижен до пользователя"));
    }

    @PostMapping("/users/{userId}/reset-attempts")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> resetUserAttempts(
            @PathVariable Long userId,
            @RequestParam Long adminId) {

        adminService.resetUserDailyAttempts(userId, adminId);
        return ResponseEntity.ok(ApiResponse.success("Дневные попытки пользователя сброшены"));
    }

    @GetMapping("/users/{userId}/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<?>> getUserStats(
            @PathVariable Long userId) {

        return ResponseEntity.ok(ApiResponse.success("Статистика пользователя"));
    }
}

