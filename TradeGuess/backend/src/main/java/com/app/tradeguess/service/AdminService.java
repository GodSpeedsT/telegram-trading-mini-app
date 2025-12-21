package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.response.AdminUserResponse;
import com.app.tradeguess.model.dto.response.UserStatsResponse;
import com.app.tradeguess.model.entity.GuessAttempt;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.model.enums.Role;
import com.app.tradeguess.repository.GuessAttemptRepository;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final GuessAttemptRepository guessAttemptRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final UserService userService;
    private final AttemptService attemptService;

    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToAdminResponse)
                .sorted((u1, u2) -> {
                    boolean u1IsAdmin = u1.getRole().isAdmin();
                    boolean u2IsAdmin = u2.getRole().isAdmin();

                    if (u1IsAdmin && !u2IsAdmin) {
                        return -1;
                    } else if (!u1IsAdmin && u2IsAdmin) {
                        return 1;
                    }
                    return u1.getCreatedAt().compareTo(u2.getCreatedAt());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminUserResponse promoteToAdmin(String telegramUsername, Long currentAdminId) {
        User currentAdmin = userRepository.findById(currentAdminId)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));

        if (currentAdmin.getRole() != Role.ROLE_SUPER_ADMIN) {
            throw new RuntimeException("Только SUPER_ADMIN может назначать администраторов");
        }

        String cleanUsername = telegramUsername.startsWith("@")
                ? telegramUsername.substring(1)
                : telegramUsername;

        User user = userRepository.findByUsernameIgnoreCase(cleanUsername)
                .orElseThrow(() ->
                        new RuntimeException("Пользователь @" + cleanUsername + " не найден"));

        if (user.getRole() == Role.ROLE_ADMIN || user.getRole() == Role.ROLE_SUPER_ADMIN) {
            throw new RuntimeException("Пользователь @" + cleanUsername + " уже является администратором");
        }

        user.setRole(Role.ROLE_ADMIN);
        User savedUser = userRepository.save(user);

        log.info("Пользователь {} (@{}) повышен до ROLE_ADMIN администратором {}",
                savedUser.getFirstName(),
                savedUser.getUsername(),
                currentAdmin.getUsername());

        return convertToAdminResponse(savedUser);
    }

    @Transactional
    public AdminUserResponse demoteFromAdmin(Long userId, Long currentAdminId) {
        User currentAdmin = userRepository.findById(currentAdminId)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));

        if (currentAdmin.getRole() != Role.ROLE_SUPER_ADMIN) {
            throw new RuntimeException("Только SUPER_ADMIN может понижать администраторов");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (user.getId().equals(currentAdminId)) {
            throw new RuntimeException("Нельзя понизить самого себя");
        }

        if (user.getRole() == Role.ROLE_SUPER_ADMIN) {
            throw new RuntimeException("Нельзя понизить SUPER_ADMIN");
        }

        if (user.getRole() == Role.ROLE_USER) {
            throw new RuntimeException("Пользователь уже является обычным пользователем");
        }

        user.setRole(Role.ROLE_USER);
        User savedUser = userRepository.save(user);

        log.info("Пользователь {} (@{}) понижен до ROLE_USER администратором {}",
                savedUser.getFirstName(),
                savedUser.getUsername(),
                currentAdmin.getUsername());

        return convertToAdminResponse(savedUser);
    }

    public List<AdminUserResponse> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllUsers();
        }

        String cleanQuery = query.startsWith("@") ? query.substring(1) : query;

        return userRepository
                .findByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(cleanQuery)
                .stream()
                .map(this::convertToAdminResponse)
                .collect(Collectors.toList());
    }

    public AdminUserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        return convertToAdminResponse(user);
    }

    public UserStatsResponse getUserStats(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Long totalAttempts = guessAttemptRepository.countByUserId(userId);
        Long correctAttempts = guessAttemptRepository.countByUserIdAndIsCorrect(userId, true);

        BigDecimal accuracy = totalAttempts > 0
                ? BigDecimal.valueOf(correctAttempts)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalAttempts), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return UserStatsResponse.builder()
                .userId(userId)
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .totalAttempts(totalAttempts)
                .correctAttempts(correctAttempts)
                .accuracy(accuracy)
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    public void resetUserDailyAttempts(Long userId, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Администратор не найден"));

        if (!admin.getRole().isAdmin()) {
            throw new RuntimeException("Толкьо админ может откатить попытки");
        }

        attemptService.resetDailyAttempts(userId);

        log.info("Дневные попытки пользователя {} сброшены администратором {}",
                userId, admin.getUsername());
    }

    private AdminUserResponse convertToAdminResponse(User user) {
        // Получаем статистику
        Long totalAttempts = guessAttemptRepository.countByUserId(user.getId());
        Long correctAttempts = guessAttemptRepository.countByUserIdAndIsCorrect(user.getId(), true);

        BigDecimal accuracy = totalAttempts > 0
                ? BigDecimal.valueOf(correctAttempts)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalAttempts), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return AdminUserResponse.builder()
                .id(user.getId())
                .telegramId(user.getTelegramId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .totalAttempts(totalAttempts)
                .correctAttempts(correctAttempts)
                .accuracy(accuracy)
                .build();
    }
}
