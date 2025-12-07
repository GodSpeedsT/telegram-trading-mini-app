package com.app.tradeguess.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "telegram_id",unique = true,nullable = false)
    private String telegramId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

}
