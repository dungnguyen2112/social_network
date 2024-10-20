package com.example.btljava.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.example.btljava.domain.Post;
import com.example.btljava.domain.User;
import com.example.btljava.domain.request.CreatePostRequestDTO;
import com.example.btljava.domain.response.ResPostDTO;
import com.example.btljava.domain.response.ResCreatePostDTO;
import com.example.btljava.domain.response.ResultPaginationDTO;
import com.example.btljava.service.PostService;
import com.example.btljava.service.UserService;
import com.example.btljava.util.annotation.ApiMessage;
import com.example.btljava.util.error.IdInvalidException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
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

    @PostMapping("/posts/create")
    @ApiMessage("Create a new post")
    public ResponseEntity<ResCreatePostDTO> createNewPost(@Valid @RequestBody CreatePostRequestDTO request)
            throws IdInvalidException {
        // Tạo đối tượng Post từ yêu cầu
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        // Giả sử bạn có phương thức để lấy User từ userId
        User user = userService.fetchUserById(getCurrentUserId());
        if (user == null) {
            throw new IdInvalidException("User with id = " + getCurrentUserId() + " does not exist");
        }
        post.setUser(user); // Thiết lập người dùng

        Post createdPost = this.postService.handleCreatePost(post);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.postService.convertToResCreatePostDTO(createdPost));
    }

    // Delete a post by ID
    @DeleteMapping("/posts/{id}")
    @ApiMessage("Delete a post")
    public ResponseEntity<Void> deletePost(@PathVariable("id") long id) throws IdInvalidException {
        Post currentPost = this.postService.fetchPostById(id);
        if (currentPost == null) {
            throw new IdInvalidException("Post với id = " + id + " không tồn tại");
        }

        this.postService.handleDeletePost(id);
        return ResponseEntity.ok(null);
    }

    // Fetch post by ID
    @GetMapping("/posts/{id}")
    @ApiMessage("Fetch post by id")
    public ResponseEntity<ResPostDTO> getPostById(@PathVariable("id") long id) throws IdInvalidException {
        Post fetchPost = this.postService.fetchPostById(id);
        if (fetchPost == null) {
            throw new IdInvalidException("Post với id = " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.postService.convertToResPostDTO(fetchPost));
    }

    // Fetch all posts
    @GetMapping("/posts")
    @ApiMessage("Fetch all posts")
    public ResponseEntity<ResultPaginationDTO> getAllPosts(
            @Filter Specification<Post> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                this.postService.fetchAllPost(spec, pageable));
    }

    // Update a post
    @PutMapping("/posts/update")
    @ApiMessage("Update a post")
    public ResponseEntity<ResPostDTO> updatePost(@RequestBody Post post) throws IdInvalidException {
        Post updatedPost = this.postService.handleUpdatePost(post.getId(), post);
        if (updatedPost == null) {
            throw new IdInvalidException("Post với id = " + post.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(this.postService.convertToResPostDTO(updatedPost));
    }
}
