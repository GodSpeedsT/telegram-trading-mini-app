package com.app.tradeguess.model;

import com.app.tradeguess.enums.TradeDirection;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "guess_attempts")
public class GuessAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chart_segment_id", nullable = false)
    private ChartSegment chartSegment;

    @Enumerated(EnumType.STRING)
    private TradeDirection direction;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "attempted_at")
    private LocalDateTime attemptedAt = LocalDateTime.now();
}
