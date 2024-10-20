package com.example.btljava.controller;

import com.example.btljava.domain.Comment;
import com.example.btljava.domain.User;
import com.example.btljava.domain.request.CommentRequestDTO;
import com.example.btljava.domain.response.PaginatedCommentResponseDTO;
import com.example.btljava.domain.response.ResCommentDTO;
import com.example.btljava.domain.response.ResCreateCommentDTO;
import com.example.btljava.service.CommentService;
import com.example.btljava.util.annotation.ApiMessage;
import com.example.btljava.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
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

    // Tạo mới bình luận cho một bài viết
    @PostMapping("/posts/{postId}/comments")
    @ApiMessage("Add a new comment to the post")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResCreateCommentDTO> createNewComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDTO requestDTO) throws IdInvalidException {
        Long userId = getCurrentUserId();
        Comment createdComment = this.commentService.handleCreateComment(postId, userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.commentService.convertToResCreateCommentDTO(createdComment));
    }

    // Cập nhật bình luận
    @PutMapping("/comments/{commentId}")
    @ApiMessage("Update a comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ResCommentDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDTO requestDTO) throws IdInvalidException {
        Comment updatedComment = this.commentService.handleUpdateComment(commentId, requestDTO);
        return ResponseEntity.ok(this.commentService.convertToResCommentDTO(updatedComment));
    }

    // Xóa bình luận
    @DeleteMapping("/comments/{commentId}")
    @ApiMessage("Delete a comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) throws IdInvalidException {
        this.commentService.handleDeleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    // Lấy tất cả bình luận của một bài viết
    @GetMapping("/posts/{postId}/comments")
    @ApiMessage("Fetch all comments for a post")
    public ResponseEntity<List<ResCommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<ResCommentDTO> comments = this.commentService.fetchCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Lấy tất cả bình luận của người dùng với phân trang
    @GetMapping("/comments")
    @ApiMessage("Fetch all comments by the logged-in user with pagination")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PaginatedCommentResponseDTO> getCommentsByUserIdWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = getCurrentUserId();
        PaginatedCommentResponseDTO comments = commentService.fetchCommentsByUserIdWithPagination(userId, page,
                pageSize);
        return ResponseEntity.ok(comments);
    }
}
