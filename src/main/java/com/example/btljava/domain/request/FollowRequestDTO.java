package com.example.btljava.domain.request;

import jakarta.validation.constraints.NotNull;

public class FollowRequestDTO {
    
    @NotNull(message = "User ID is required")
    private Long userId;

    public FollowRequestDTO() {
    }

    public FollowRequestDTO(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}