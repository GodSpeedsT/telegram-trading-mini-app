package com.app.tradeguess.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChartResponse implements Serializable {
    private Long segmentId;
    private List<Candle> candles;
    private Integer attemptsLeft;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Candle {
        @JsonProperty("t")
        private Long timestamp;
        @JsonProperty("o")
        private Double open;
        @JsonProperty("h")
        private Double high;
        @JsonProperty("l")
        private Double low;
        @JsonProperty("c")
        private Double close;
        @JsonProperty("v")
        private Double volume;
    }
}