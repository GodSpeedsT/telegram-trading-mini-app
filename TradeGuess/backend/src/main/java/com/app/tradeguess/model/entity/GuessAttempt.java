package com.app.tradeguess.model.entity;

import com.app.tradeguess.model.enums.TradeDirection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "guess_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuessAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chart_segment_id", nullable = false)
    private ChartSegment chartSegment;

    @Column(name = "user_direction")
    private String userDirection;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "attempted_at")
    private LocalDateTime attemptedAt = LocalDateTime.now();

}