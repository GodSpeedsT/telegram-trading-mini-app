package com.app.tradeguess.controller.api;

import com.app.tradeguess.model.dto.response.ApiResponse;
import com.app.tradeguess.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/me/stats")
    public ResponseEntity<ApiResponse<StatsResponse>> getStats(
            @RequestParam Long userId
    ) {
       StatsResponse stats = statsService.getUserStats(userId);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }

}
