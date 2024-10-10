package com.example.btljava.domain.response;

import jakarta.validation.constraints.NotNull;

public class NotificationUpdateDTO {

    @NotNull(message = "Notification ID is required")
    private Long notificationId;

    private boolean isRead;

    // Getters and setters

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}
