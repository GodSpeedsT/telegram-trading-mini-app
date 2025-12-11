package com.app.tradeguess.model.dto.request;

import com.app.tradeguess.model.enums.TradeDirection;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GuessRequest {
    private Long segmentId;
    private String direction;
}