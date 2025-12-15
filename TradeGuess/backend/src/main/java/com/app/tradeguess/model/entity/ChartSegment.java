package com.app.tradeguess.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;


@Entity
@Table(name = "chart_segments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "display_candles", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String displayCandles;

    @Column(name = "result_candles", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String resultCandles;

    @Column(name = "price_at_decision")
    private BigDecimal priceAtDecision;

    @Column(name = "price_at_target")
    private BigDecimal priceAtTarget;

    @Column(name = "calculated_direction")
    private Integer calculatedDirection;

}