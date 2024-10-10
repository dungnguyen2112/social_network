package com.example.btljava.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String address;
    private String avatarUrl;
    private String bio;
    private Instant createdAt;
    private Instant updatedAt;
    private RoleDTO role;

    public ResUserDTO(Long id, String username, String email, String name, String address, String avatarFileName,
            String bio, Instant createdAt, Instant updatedAt, RoleDTO role, String baseURI) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.address = address;
        this.avatarUrl = baseURI + "/avatars/" + avatarFileName;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleDTO {
        private Long id;
        private String roleName;
    }
}
