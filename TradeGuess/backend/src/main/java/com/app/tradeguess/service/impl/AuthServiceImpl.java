package com.app.tradeguess.service.impl;

import com.app.tradeguess.model.dto.request.TelegramAuthRequest;
import com.app.tradeguess.model.dto.response.AuthResponse;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.repository.UserRepository;
import com.app.tradeguess.security.JwtTokenProvider;
import com.app.tradeguess.service.TelegramAuthService;
import com.app.tradeguess.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TelegramAuthService telegramAuthService;
    private final UserStatsService userStatsService;

    @Override
    @Transactional
    public AuthResponse authenticateTelegramUser(TelegramAuthRequest request) {
        // Извлекаем данные пользователя из Telegram
        TelegramAuthService.TelegramUser telegramUser =
                telegramAuthService.extractUser(request.getInitData());

        return authenticateUser(
                telegramUser.getId(),
                telegramUser.getUsername(),
                telegramUser.getFirstName()
        );
    }

    @Override
    @Transactional
    public AuthResponse authenticateSimple(TelegramAuthRequest request) {
        return authenticateUser(
                request.getTelegramId(),
                request.getUsername(),
                request.getFirstName()
        );
    }

    private AuthResponse authenticateUser(Long telegramId, String username, String firstName) {
        // Ищем существующего пользователя
        Optional<User> existingUser = userRepository.findByTelegramId(telegramId);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            log.info("User found: {}", user.getTelegramId());
        } else {
            // Создаем нового пользователя
            user = createNewUser(telegramId, username, firstName);
            log.info("New user created: {}", user.getTelegramId());
        }

        // Генерируем JWT токен
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());

        // Получаем количество оставшихся попыток на сегодня
        Integer dailyAttemptsLeft = userStatsService.getDailyAttemptsLeft(user.getId());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .dailyAttemptsLeft(dailyAttemptsLeft)
                .build();
    }

    private User createNewUser(Long telegramId, String username, String firstName) {
        User user = new User();
        user.setTelegramId(telegramId);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
