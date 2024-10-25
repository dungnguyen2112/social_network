package com.example.btljava.repository;

import com.example.btljava.domain.Notification;
import com.example.btljava.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByUser(User user, Pageable pageable);

    List<Notification> findAllByUserId(Long id);
}
