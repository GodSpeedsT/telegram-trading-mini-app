package com.app.tradeguess.model.dto.request;

import com.app.tradeguess.model.enums.TradeDirection;
import lombok.Data;

@Data
public class GuessRequest {
    private Long segmentId;
    private TradeDirection direction;
}