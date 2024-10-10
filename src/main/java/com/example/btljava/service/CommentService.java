package com.example.btljava.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.btljava.domain.Comment;
import com.example.btljava.domain.Post;
import com.example.btljava.domain.User;
import com.example.btljava.domain.request.CommentRequestDTO;
import com.example.btljava.domain.response.PaginatedCommentResponseDTO;
import com.example.btljava.domain.response.ResCommentDTO;
import com.example.btljava.domain.response.ResCreateCommentDTO;
import com.example.btljava.domain.response.ResultPaginationDTO;
import com.example.btljava.repository.CommentRepository;
import com.example.btljava.repository.PostRepository;
import com.example.btljava.repository.UserRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Create a new comment
    @Transactional
    public Comment handleCreateComment(Long postId, Long userId, CommentRequestDTO requestDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(requestDTO.getContent());
        comment.setCreatedAt(Instant.now());
        comment.setUpdatedAt(Instant.now());

        return commentRepository.save(comment);
    }

    // Delete a comment
    public void handleDeleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // Fetch a comment by ID
    public Comment fetchCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        return commentOptional.orElse(null); // Return null if comment not found
    }

    // Fetch comments for a post with pagination
    public ResultPaginationDTO fetchCommentsByPostId(Long postId, Specification<Comment> spec, Pageable pageable) {
        Page<Comment> pageComment = commentRepository.findAll(spec, pageable);
        ResultPaginationDTO result = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageComment.getTotalPages());
        meta.setTotal(pageComment.getTotalElements());

        result.setMeta(meta);

        // Map Comment entities to ResCommentDTO
        List<ResCommentDTO> commentList = pageComment.getContent()
                .stream()
                .map(this::convertToResCommentDTO)
                .collect(Collectors.toList());

        result.setResult(commentList);
        return result;
    }

    // Update a comment
    @Transactional
    public Comment handleUpdateComment(Long commentId, CommentRequestDTO requestDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment currentComment = commentOptional.get();
            currentComment.setContent(requestDTO.getContent());
            currentComment.setUpdatedAt(Instant.now()); // Update timestamp
            return commentRepository.save(currentComment);
        }
        return null; // Return null if comment is not found
    }

    // Convert Comment to ResCreateCommentDTO
    public ResCreateCommentDTO convertToResCreateCommentDTO(Comment comment) {
        ResCreateCommentDTO res = new ResCreateCommentDTO();
        res.setId(comment.getId());
        res.setPostId(comment.getPost().getId());
        res.setUserId(comment.getUser().getId());
        res.setContent(comment.getContent());
        res.setCreatedAt(comment.getCreatedAt());
        return res;
    }

    public PaginatedCommentResponseDTO fetchCommentsByUserIdWithPagination(Long userId, int page, int pageSize) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(page - 1, pageSize); // `page - 1` vì Spring bắt đầu từ 0
        Page<Comment> commentPage = commentRepository.findByUser(user, pageable);

        // Chuyển đổi các Comment sang DTO
        List<ResCommentDTO> commentDTOs = commentPage.getContent().stream()
                .map(this::convertToResCommentDTO)
                .collect(Collectors.toList());

        // Tạo đối tượng Meta chứa thông tin phân trang
        PaginatedCommentResponseDTO.Meta meta = new PaginatedCommentResponseDTO.Meta();
        meta.setPage(page);
        meta.setPageSize(pageSize);
        meta.setTotalPages(commentPage.getTotalPages());
        meta.setTotalComments(commentPage.getTotalElements());

        // Tạo PaginatedCommentResponseDTO để trả về
        PaginatedCommentResponseDTO response = new PaginatedCommentResponseDTO();
        response.setComments(commentDTOs);
        response.setMeta(meta);

        return response;
    }

    // Convert Comment to ResCommentDTO
    public ResCommentDTO convertToResCommentDTO(Comment comment) {
        ResCommentDTO res = new ResCommentDTO();
        res.setId(comment.getId());
        res.setPostId(comment.getPost().getId());
        res.setUserId(comment.getUser().getId());
        res.setContent(comment.getContent());
        res.setCreatedAt(comment.getCreatedAt());
        res.setUpdatedAt(comment.getUpdatedAt());
        return res;
    }

    public List<ResCommentDTO> fetchCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::convertToResCommentDTO)
                .collect(Collectors.toList());
    }
}
