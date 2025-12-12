package com.app.tradeguess.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "chart_segment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_candles", columnDefinition = "jsonb")
    private String displayCandles;

    @Column(name = "result_candles", columnDefinition = "jsonb")
    private String resultCandles;

    @Column(name = "price_at_decision")
    private Double priceAtDecision;

    @Column(name = "price_at_target")
    private Double priceAtTarget;

    @Column(name = "calculated_direction")
    private Integer calculatedDirection;

}