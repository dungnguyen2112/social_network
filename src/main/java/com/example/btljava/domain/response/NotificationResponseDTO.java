package com.example.btljava.domain.response;

import java.time.Instant;

public class NotificationResponseDTO {

    private Long notificationId;
    private Long userId;
    private String content;
    private boolean isRead;
    private Instant createdAt;

    // Constructor
    public NotificationResponseDTO(Long notificationId, Long userId, String content, boolean isRead,
            Instant createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.content = content;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
