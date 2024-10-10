package com.example.btljava.domain.response;

import java.time.Instant;

public class UserFollowResponseDTO {

    private Long id;
    private String username; // Hoặc tên hiển thị của người dùng
    private Instant followedAt;

    public UserFollowResponseDTO(Long id, String username, Instant followedAt) {
        this.id = id;
        this.username = username;
        this.followedAt = followedAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Instant getFollowedAt() {
        return followedAt;
    }
}