package com.example.btljava.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.btljava.util.SecurityUtil;
import com.example.btljava.util.constant.GenderEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "email không được để trống")
    @Column(unique = true, nullable = false)
    private String email;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @NotBlank(message = "password không được để trống")
    @Column(nullable = false)
    private String passwordHash;

    private String name;
    private String address;
    private String avatarUrl;
    private String bio;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private int age;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private Instant createdAt;
    private Instant updatedAt;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    // Getters and Setters
}
