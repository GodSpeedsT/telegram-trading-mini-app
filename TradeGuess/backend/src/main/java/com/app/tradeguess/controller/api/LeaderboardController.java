package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.model.dto.response.LeaderboardResponse;
import com.app.tradeguess.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<LeaderboardResponse>> getLeaderboard(
            @RequestParam(defaultValue = "weekly") String period,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        LeaderboardResponse leaderboard = leaderboardService.getLeaderboard(period, page, size);
        return ResponseEntity.ok(ApiResponse.success(leaderboard));
    }
}