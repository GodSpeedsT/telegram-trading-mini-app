package com.app.tradeguess.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TradeDirection {
    LONG("long"),
    SHORT("short");

    private final String value;

    TradeDirection(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TradeDirection fromValue(String value) {
        for (TradeDirection direction : TradeDirection.values()) {

            if (direction.value.equalsIgnoreCase(value)) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Неизвестная догадка: " + value);
    }
    }
