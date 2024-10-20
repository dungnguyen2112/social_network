package com.example.btljava.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.example.btljava.domain.Comment;
import com.example.btljava.domain.Post;
import com.example.btljava.domain.User;
import com.example.btljava.domain.response.LikeResponseDTO;
import com.example.btljava.service.CommentService;
import com.example.btljava.service.LikeService;
import com.example.btljava.service.PostService;
import com.example.btljava.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final JwtEncoder jwtEncoder;

    public LikeController(LikeService likeService, UserService userService, PostService postService,
            CommentService commentService, JwtEncoder jwtEncoder) {
        this.likeService = likeService;
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.jwtEncoder = jwtEncoder;
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

    // Thích bài viết
    @PostMapping("/like/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likePost(@PathVariable("postId") Long postId) {
        Long userId = getCurrentUserId();
        likeService.likePost(userId, postId);
        Post post = postService.fetchPostById(postId);
        post.setLikesCount(post.getLikesCount() + 1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Thích bình luận
    @PostMapping("/like/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> likeComment(@PathVariable("commentId") Long commentId) {
        Long userId = getCurrentUserId();
        likeService.likeComment(userId, commentId);
        Comment comment = commentService.fetchCommentById(commentId);
        comment.setLikesCount(comment.getLikesCount() + 1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Hủy thích bài viết
    @DeleteMapping("/unlike/posts/{postId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikePost(@PathVariable("postId") Long postId) {
        Long userId = getCurrentUserId();
        likeService.unlikePost(userId, postId);
        Post post = postService.fetchPostById(postId);
        post.setLikesCount(post.getLikesCount() - 1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Hủy thích bình luận
    @DeleteMapping("/unlike/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlikeComment(@PathVariable("commentId") Long commentId) {
        Long userId = getCurrentUserId();
        likeService.unlikeComment(userId, commentId);
        Comment comment = commentService.fetchCommentById(commentId);
        comment.setLikesCount(comment.getLikesCount() - 1);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Endpoint để lấy danh sách các bài viết đã thích của người dùng
    @GetMapping("/like/posts/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LikeResponseDTO>> getLikedPostsByUser() {
        Long userId = getCurrentUserId();
        User user = userService.fetchUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<LikeResponseDTO> likedPosts = likeService.getLikedPostsByUser(user);
        return ResponseEntity.ok(likedPosts);
    }

    // Lấy danh sách các bình luận đã thích của người dùng
    @GetMapping("/like/comments/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<LikeResponseDTO>> getLikedCommentsByUser() {
        Long userId = getCurrentUserId();
        User user = userService.fetchUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        List<LikeResponseDTO> likedComments = likeService.getLikedCommentsByUser(user);
        return ResponseEntity.ok(likedComments);
    }
}
