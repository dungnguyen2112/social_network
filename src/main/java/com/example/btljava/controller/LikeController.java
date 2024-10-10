package com.example.btljava.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.btljava.domain.User;
import com.example.btljava.domain.request.LikeRequestDTO;
import com.example.btljava.domain.response.LikeResponseDTO;
import com.example.btljava.service.LikeService;
import com.example.btljava.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    public LikeController(LikeService likeService, UserService userService) {
        this.likeService = likeService;
        this.userService = userService;
    }

    // Thích bài viết
    @PostMapping("/like/posts/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable("postId") Long postId, @RequestBody LikeRequestDTO request) {
        likeService.likePost(request.getUserId(), postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Thích bình luận
    @PostMapping("/like/comments/{commentId}")
    public ResponseEntity<Void> likeComment(@PathVariable("commentId") Long commentId,
            @RequestBody LikeRequestDTO request) {
        likeService.likeComment(request.getUserId(), commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/unlike/posts/{postId}/{userId}")
    public ResponseEntity<Void> unlikePost(@PathVariable("postId") Long postId, @PathVariable("userId") Long userId) {
        likeService.unlikePost(userId, postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Hủy thích bình luận
    @DeleteMapping("/unlike/comments/{commentId}/{userId}")
    public ResponseEntity<Void> unlikeComment(@PathVariable("commentId") Long commentId,
            @RequestBody LikeRequestDTO request) {
        likeService.unlikeComment(request.getUserId(), commentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Endpoint để lấy danh sách các bài viết đã thích của người dùng
    @GetMapping("/like/posts/user/{userId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikedPostsByUser(@PathVariable("userId") Long userId) {
        User user = userService.fetchUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<LikeResponseDTO> likedPosts = likeService.getLikedPostsByUser(user);
        return ResponseEntity.ok(likedPosts);
    }

    // Lấy danh sách các bình luận đã thích của người dùng
    @GetMapping("/like/comments/user/{userId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikedCommentsByUser(@PathVariable("userId") Long userId) {
        User user = userService.fetchUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<LikeResponseDTO> likedComments = likeService.getLikedCommentsByUser(user);
        return ResponseEntity.ok(likedComments);
    }
}
