package com.example.btljava.domain.response;

import java.time.Instant;

public class LikeResponseDTO {
    private Long id;
    private Long userId;
    private Long postId;
    private Long commentId;
    private Instant createdAt;

    // Constructors
    public LikeResponseDTO(Long id, Long userId, Long postId, Long commentId, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
