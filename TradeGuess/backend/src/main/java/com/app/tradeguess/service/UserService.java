package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.dto.response.AuthResponse;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public AuthResponse authenticate(AuthRequest request) {
        User user = findOrCreateUser(request);

        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .token(generateToken(user.getId()))
                .isNewUser(isNewUser(user))
                .build();
    }

    public User findOrCreateUser(AuthRequest request) {
        return userRepository.findByTelegramId(request.getTelegramId())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setTelegramId(request.getTelegramId());
                    newUser.setUsername(request.getUsername());
                    newUser.setFirstName(request.getFirstName());
                    newUser.setCreatedAt(LocalDateTime.now());
                    return userRepository.save(newUser);
                });
    }

    private String generateToken(Long userId) {
        return "tg-" + userId + "-" + System.currentTimeMillis();
    }

    private boolean isNewUser(User user) {
        return user.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(5));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
}