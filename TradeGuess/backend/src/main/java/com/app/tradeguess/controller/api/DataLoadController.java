package com.app.tradeguess.controller.api;

import com.app.tradeguess.service.BinanceDataService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/data")
@RequiredArgsConstructor
public class DataLoadController {

    private final BinanceDataService binanceDataService;

    @PostMapping("/load/test")
    @Operation(summary = "Загрузить тестовые данные")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> loadTestData() {
        try {
            binanceDataService.loadTestData();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Тестовые данные успешно загружены"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/load/symbol")
    @Operation(summary = "Загрузить данные для конкретного символа")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> loadSymbolData(
            @RequestParam String symbol,
            @RequestParam(defaultValue = "1h") String interval,
            @RequestParam(defaultValue = "200") int limit) {
        try {
            binanceDataService.loadHistoricalData(symbol, interval, limit);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", String.format("Данные для %s загружены", symbol)
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PostMapping("/update-now")
    @Operation(summary = "Обновить данные сейчас")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateNow() {
        try {
            binanceDataService.updateDataNow();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Данные успешно обновлены"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
}