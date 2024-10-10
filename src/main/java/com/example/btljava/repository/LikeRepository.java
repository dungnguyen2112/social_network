package com.example.btljava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.btljava.domain.Like;
import com.example.btljava.domain.Post;
import com.example.btljava.domain.Comment;
import com.example.btljava.domain.User;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Optional<Like> findByUserAndComment(User user, Comment comment);

    List<Like> findByUser(User user);
}
