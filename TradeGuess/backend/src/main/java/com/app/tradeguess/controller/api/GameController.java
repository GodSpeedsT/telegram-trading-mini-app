package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.dto.request.GuessRequest;
import com.app.tradeguess.model.dto.response.ChartResponse;
import com.app.tradeguess.model.dto.response.GuessResponse;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.service.GameService;
import com.app.tradeguess.service.StatsService;
import com.app.tradeguess.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final UserService userService;
    private final StatsService statsService;

    @PostMapping("/auth")
    public Map<String, Object> auth(@RequestBody AuthRequest request) {
        User user = userService.findOrCreateUser(request);
        return Map.of(
                "success", true,
                "userId", user.getTelegramId(),
                "message","Авторизация прошла успешно"
        );
    }

    @GetMapping("/chart")
    public ChartResponse getChart(@RequestParam Long userId) {
       return gameService.getTrainingChart(userId);
    }

    @PostMapping("/guess")
    public GuessResponse guess(
            @RequestBody GuessRequest request,
            @RequestParam Long userId
    ) {
       return gameService.processGuess(userId, request);
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestParam Long userId) {
       return statsService.getUserStats(userId);
    }
}