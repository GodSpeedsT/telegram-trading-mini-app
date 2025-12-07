package com.app.tradeguess.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "chart_segment")
public class ChartSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String symbol;
    private String timeFrame;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "display_candles", columnDefinition = "jsonb")
    private String displayCandles;

    @Column(name = "result_candles", columnDefinition = "jsonb")
    private String resultCandles;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
