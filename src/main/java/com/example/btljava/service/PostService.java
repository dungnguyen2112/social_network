package com.example.btljava.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.btljava.domain.Post;
import com.example.btljava.domain.response.ResCreatePostDTO;
import com.example.btljava.domain.response.ResPostDTO;
import com.example.btljava.domain.response.ResUpdatePostDTO;
import com.example.btljava.domain.response.ResultPaginationDTO;
import com.example.btljava.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post handleCreatePost(Post post) {
        return this.postRepository.save(post);
    }

    public void handleDeletePost(long id) {
        this.postRepository.deleteById(id);
    }

    public Post fetchPostById(long id) {
        Optional<Post> postOptional = this.postRepository.findById(id);
        return postOptional.orElse(null); // Return null if post not found
    }

    public ResultPaginationDTO fetchAllPost(Specification<Post> spec, Pageable pageable) {
        Page<Post> pagePost = this.postRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePost.getTotalPages());
        mt.setTotal(pagePost.getTotalElements());

        rs.setMeta(mt);

        // Map Post entities to ResPostDTO
        List<ResPostDTO> listPost = pagePost.getContent()
                .stream()
                .map(item -> new ResPostDTO(
                        item.getId(),
                        item.getTitle(),
                        item.getContent(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getUser().getName() // Assuming Post has a User relation to fetch the name
                ))
                .collect(Collectors.toList());

        rs.setResult(listPost);
        return rs;
    }

    public Post handleUpdatePost(long postId, Post postDetails) {
        Optional<Post> postOptional = this.postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post currentPost = postOptional.get();
            currentPost.setTitle(postDetails.getTitle());
            currentPost.setContent(postDetails.getContent());
            currentPost.setUpdatedAt(postDetails.getUpdatedAt()); // Or set it to LocalDateTime.now()
            // Update post in the database
            return this.postRepository.save(currentPost);
        }
        return null; // Return null if post is not found
    }

    public ResCreatePostDTO convertToResCreatePostDTO(Post post) {
        ResCreatePostDTO res = new ResCreatePostDTO();

        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setCreatedAt(post.getCreatedAt());
        res.setUpdatedAt(post.getUpdatedAt());
        res.setUserName(post.getUser().getName()); // Assuming Post has a User relationship
        return res;
    }

    public ResUpdatePostDTO convertToResUpdatePostDTO(Post post) {
        ResUpdatePostDTO res = new ResUpdatePostDTO();

        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setUpdatedAt(post.getUpdatedAt());
        res.setUserName(post.getUser().getName()); // Assuming Post has a User relationship
        return res;
    }

    public ResPostDTO convertToResPostDTO(Post post) {
        ResPostDTO res = new ResPostDTO();

        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setCreatedAt(post.getCreatedAt());
        res.setUpdatedAt(post.getUpdatedAt());
        res.setUserName(post.getUser().getName()); // Assuming Post has a User relationship
        return res;
    }

}
