package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.AuthRequest;
import com.app.tradeguess.model.dto.request.GuessRequest;
import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.model.dto.response.ChartResponse;
import com.app.tradeguess.model.dto.response.GuessResponse;
import com.app.tradeguess.model.entity.User;
import com.app.tradeguess.service.GameService;
import com.app.tradeguess.service.StatsService;
import com.app.tradeguess.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/chart")
    public ResponseEntity<ApiResponse<ChartResponse>> getChart(
            @RequestParam Long userId
    ) {
        ChartResponse chart = gameService.getTrainingChart(userId);

        return ResponseEntity.ok(ApiResponse.success(chart));
    }

    @PostMapping("/guess")
    public ResponseEntity<ApiResponse<GuessResponse>> guess(
            @RequestParam Long userId,
            @Valid @RequestBody GuessRequest request) {

        GuessResponse response = gameService.processGuess(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}