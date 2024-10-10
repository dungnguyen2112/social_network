package com.example.btljava.service;

import com.example.btljava.domain.Notification;
import com.example.btljava.domain.User;
import com.example.btljava.domain.request.NotificationRequestDTO;
import com.example.btljava.domain.response.NotificationResponseDTO;
import com.example.btljava.domain.response.ResultPaginationDTO;
import com.example.btljava.repository.NotificationRepository;
import com.example.btljava.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final UserRepository userRepository;

        public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
                this.notificationRepository = notificationRepository;
                this.userRepository = userRepository;
        }

        // Tạo thông báo mới
        public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
                User user = userRepository.findById(requestDTO.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Notification notification = new Notification();
                notification.setUser(user);
                notification.setContent(requestDTO.getContent());
                notification.setCreatedAt(Instant.now());
                notification.setIsRead(false);

                notificationRepository.save(notification);

                return new NotificationResponseDTO(
                                notification.getId(),
                                user.getId(),
                                notification.getContent(),
                                notification.getIsRead(),
                                notification.getCreatedAt());
        }

        // Đánh dấu thông báo là đã đọc
        public void markNotificationAsRead(Long notificationId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification not found"));

                notification.setIsRead(true);
                notificationRepository.save(notification);
        }

        // Xóa thông báo
        public void deleteNotification(Long notificationId) {
                Notification notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException("Notification not found"));

                notificationRepository.delete(notification);
        }

        // Fetch notifications by userId with pagination
        public ResultPaginationDTO fetchNotificationsByUser(Long userId, Pageable pageable) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Lấy tất cả thông báo của user này
                Page<Notification> pageNotification = notificationRepository.findAllByUser(user, pageable);
                ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
                ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

                // Set metadata cho phân trang
                meta.setPage(pageable.getPageNumber() + 1);
                meta.setPageSize(pageable.getPageSize());
                meta.setPages(pageNotification.getTotalPages());
                meta.setTotal(pageNotification.getTotalElements());

                resultPaginationDTO.setMeta(meta);

                // Map Notification entities to NotificationResponseDTO
                List<NotificationResponseDTO> notifications = pageNotification.getContent()
                                .stream()
                                .map(notification -> new NotificationResponseDTO(
                                                notification.getId(),
                                                notification.getUser().getId(),
                                                notification.getContent(),
                                                notification.getIsRead(),
                                                notification.getCreatedAt()))
                                .collect(Collectors.toList());

                resultPaginationDTO.setResult(notifications);
                return resultPaginationDTO;
        }
}
