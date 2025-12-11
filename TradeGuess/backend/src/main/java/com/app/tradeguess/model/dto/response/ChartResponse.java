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
public class ChartResponse {
    private Long segmentId;
    private List<Candle> candles;
    private Integer attemptsLeft;

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
        private Double volume;
    }
}