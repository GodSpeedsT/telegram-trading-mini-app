package com.app.tradeguess.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuessResponse {
    private Boolean isCorrect;
    private List<Candle> resultCandles;
    private String message;
    private Double priceChangePercent;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Candle {
        private Long timestamp;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
    }

}