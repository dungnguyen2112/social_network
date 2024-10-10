package com.example.btljava.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.btljava.domain.Comment;
import com.example.btljava.domain.User;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    List<Comment> findByPostId(Long postId);

    Page<Comment> findByUser(User user, Pageable pageable);
}
