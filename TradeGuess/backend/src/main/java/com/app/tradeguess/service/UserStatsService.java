//package com.app.tradeguess.service;
//
//import com.app.tradeguess.repository.UserStatsRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class UserStatsService {
//
//    private final UserStatsRepository userStatsRepository;
//
//    public Integer getDailyAttemptsLeft(Long userId) {
//        UserStats stats = userStatsRepository.findByUserId(userId)
//                .orElseGet(() -> createDefaultStats(userId));
//
//        // Максимум 10 попыток в день
//        return Math.max(0, 10 - stats.getDailyAttempts());
//    }
//
//    private UserStats createDefaultStats(Long userId) {
//        UserStats stats = new UserStats();
//        stats.setUserId(userId);
//        stats.setDailyAttempts(0);
//        return userStatsRepository.save(stats);
//    }
//}