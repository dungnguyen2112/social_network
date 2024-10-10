package com.example.btljava.controller;

import com.example.btljava.domain.Comment;
import com.example.btljava.domain.request.CommentRequestDTO;
import com.example.btljava.domain.response.PaginatedCommentResponseDTO;
import com.example.btljava.domain.response.ResCommentDTO;
import com.example.btljava.domain.response.ResCreateCommentDTO;
import com.example.btljava.service.CommentService;
import com.example.btljava.util.annotation.ApiMessage;
import com.example.btljava.util.error.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/posts/{postId}/users/{userId}/comments")
    @ApiMessage("Add a new comment to the post")
    public ResponseEntity<ResCreateCommentDTO> createNewComment(
            @PathVariable Long postId,
            @PathVariable Long userId,
            @Valid @RequestBody CommentRequestDTO requestDTO) throws IdInvalidException {
        Comment createdComment = this.commentService.handleCreateComment(postId, userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.commentService.convertToResCreateCommentDTO(createdComment));
    }

    // Update a comment
    @PutMapping("/comments/{commentId}")
    @ApiMessage("Update a comment")
    public ResponseEntity<ResCommentDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDTO requestDTO) throws IdInvalidException {
        Comment updatedComment = this.commentService.handleUpdateComment(commentId, requestDTO);
        return ResponseEntity.ok(this.commentService.convertToResCommentDTO(updatedComment));
    }

    // Delete a comment
    @DeleteMapping("/comments/{commentId}")
    @ApiMessage("Delete a comment")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) throws IdInvalidException {
        this.commentService.handleDeleteComment(commentId);
        return ResponseEntity.ok(null);
    }

    // Fetch comments for a post
    @GetMapping("/posts/{postId}/comments")
    @ApiMessage("Fetch all comments for a post")
    public ResponseEntity<List<ResCommentDTO>> getCommentsByPostId(@PathVariable Long postId) {
        List<ResCommentDTO> comments = this.commentService.fetchCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // Fetch comments by user
    // API lấy tất cả bình luận theo userId với phân trang
    @GetMapping("/users/{userId}/comments")
    @ApiMessage("Fetch all comments by a user with pagination")
    public ResponseEntity<PaginatedCommentResponseDTO> getCommentsByUserIdWithPagination(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginatedCommentResponseDTO comments = commentService.fetchCommentsByUserIdWithPagination(userId, page,
                pageSize);
        return ResponseEntity.ok(comments);
    }
}
