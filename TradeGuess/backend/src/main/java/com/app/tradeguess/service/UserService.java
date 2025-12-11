package com.app.tradeguess.service;

import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findOrCreateUser(
            AuthRequest authRequest
    ) {
        return userRepository.findByTelegramId(authRequest.getTelegramId())
                .orElseGet(() -> {
                    User user = new User();
                    user.setTelegramId(authRequest.getTelegramId());
                    user.setUsername(authRequest.getUsername());
                    user.setFirstName(authRequest.getFirstName());
                    return userRepository.save(user);
                });
    }
}