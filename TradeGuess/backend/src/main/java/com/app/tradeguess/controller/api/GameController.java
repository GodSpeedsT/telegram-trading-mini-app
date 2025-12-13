package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.request.GuessRequest;
import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.model.dto.response.ChartResponse;
import com.app.tradeguess.model.dto.response.GuessResponse;
import com.app.tradeguess.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/chart")
    public ResponseEntity<ApiResponse<ChartResponse>> getChart(@RequestParam Long userId) {
        ChartResponse chart = gameService.getTrainingChart(userId);
        return ResponseEntity.ok(ApiResponse.success(chart));
    }

    @PostMapping("/guess")
    public ResponseEntity<ApiResponse<GuessResponse>> guess(
            @RequestParam Long userId,
            @RequestBody GuessRequest request) {

        GuessResponse response = gameService.processGuess(userId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("GameController is working! " + java.time.LocalDateTime.now());
    }

}