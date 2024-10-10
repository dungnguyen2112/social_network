package com.example.btljava.domain.request;

import jakarta.validation.constraints.NotNull;

public class LikeRequestDTO {
    @NotNull(message = "User ID is mandatory")
    private Long userId;

    @NotNull(message = "Post ID or Comment ID is mandatory")
    private Long postId; // For liking a post

    private Long commentId; // For liking a comment (optional)

    // Getters and Setters
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
}
