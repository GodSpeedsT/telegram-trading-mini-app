//package com.app.tradeguess.service;
//
//import com.app.tradeguess.model.dto.request.AuthRequest;
//import com.app.tradeguess.model.dto.response.AuthResponse;
//import com.app.tradeguess.model.entity.User;
//import com.app.tradeguess.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserService {
//
//    private final UserRepository userRepository;
//
//    public AuthResponse authenticate(AuthRequest request) {
//        User user = findOrCreateUser(request);
//
//        return AuthResponse.builder()
//                .userId(user.getId())
//                .username(user.getUsername())
//                .firstName(user.getFirstName())
//                .token(generateToken(user.getId()))
//                .isNewUser(false)
//                .build();
//    }
//
//    public String generateToken(Long id) {
//        return "user-token-" + id + "-" + System.currentTimeMillis();
//    }
//}