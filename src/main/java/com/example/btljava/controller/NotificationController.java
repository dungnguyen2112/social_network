package com.example.btljava.controller;

import com.example.btljava.domain.request.NotificationRequestDTO;
import com.example.btljava.domain.response.NotificationResponseDTO;
import com.example.btljava.domain.response.ResultPaginationDTO;
import com.example.btljava.service.NotificationService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Đánh dấu thông báo là đã đọc
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable("notificationId") Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Xóa thông báo
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Thêm thông báo mới
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> addNotification(@RequestBody NotificationRequestDTO requestDTO) {
        NotificationResponseDTO responseDTO = notificationService.createNotification(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    // API lấy thông báo của người dùng với phân trang
    @GetMapping("/user/{userId}")
    public ResponseEntity<ResultPaginationDTO> getNotificationsByUser(
            @PathVariable Long userId, Pageable pageable) {
        ResultPaginationDTO notifications = notificationService.fetchNotificationsByUser(userId, pageable);
        return ResponseEntity.ok(notifications);
    }

}
