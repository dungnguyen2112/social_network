package com.example.btljava.domain.response;

import java.time.Instant;

public class FollowResponseDTO {

    private Long id;
    private Long followerId;
    private Long followedId;
    private Instant createdAt;

    public FollowResponseDTO(Long id, Long followerId, Long followedId, Instant createdAt) {
        this.id = id;
        this.followerId = followerId;
        this.followedId = followedId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
