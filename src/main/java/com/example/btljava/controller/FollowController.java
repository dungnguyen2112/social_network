package com.example.btljava.controller;

import com.example.btljava.domain.User;
import com.example.btljava.domain.response.UserFollowResponseDTO;
import com.example.btljava.service.FollowService;
import com.example.btljava.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class FollowController {
    private final FollowService followService;
    private final UserService userService; // Service để tìm kiếm người dùng

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

     // Method to extract userId from SecurityContext
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication); // Log for debugging

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtAuthToken.getToken();
            System.out.println("JWT Claims: " + jwt.getClaims()); // Log to check claims

            Long userId = jwt.getClaim("id"); // Extract userId from JWT claims
            System.out.println("Extracted User ID: " + userId); // Log to check extracted ID

            if (userId == null) {
                throw new IllegalStateException("User ID is null; check your JWT claims.");
            }
            return userId;
        }

        throw new IllegalArgumentException("User is not authenticated");
    }


    @PostMapping("/{userId}/follow")
    public ResponseEntity<String> followUser(@PathVariable Long userId) {
        Long followerId = getCurrentUserId();
        User currentUser = userService.fetchUserById(followerId);
        User followedUser = userService.fetchUserById(userId);
        if (followedUser == null) {
            return ResponseEntity.notFound().build();
        }
        followService.followUser(currentUser, followedUser);
        return ResponseEntity.ok("Following user with ID: " + userId);
    }

    @PostMapping("/{userId}/unfollow")
    public ResponseEntity<String> unfollowUser(@PathVariable Long userId) {
        Long followerId = getCurrentUserId();
        User currentUser = userService.fetchUserById(followerId);
        User followedUser = userService.fetchUserById(userId);
        if (followedUser == null) {
            return ResponseEntity.notFound().build();
        }
        followService.unfollowUser(currentUser, followedUser);
        return ResponseEntity.ok("Unfollowed user with ID: " + userId);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserFollowResponseDTO>> getFollowers(@PathVariable Long userId) {
        User user = userService.fetchUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<UserFollowResponseDTO> followers = followService.getFollowers(user)
                .stream()
                .map(follower -> new UserFollowResponseDTO(follower.getId(),
                        follower.getUsername(),
                        follower.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<UserFollowResponseDTO>> getFollowing(@PathVariable Long userId) {
        User user = userService.fetchUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<UserFollowResponseDTO> following = followService.getFollowing(user)
                .stream()
                .map(followingUser -> new UserFollowResponseDTO(followingUser.getId(),
                        followingUser.getUsername(),
                        followingUser.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(following);
    }
}
