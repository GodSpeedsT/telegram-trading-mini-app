package com.app.tradeguess.model.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserResponse implements Serializable {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isPremium;
    private LocalDateTime createdAt;
}
