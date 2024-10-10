package com.example.btljava.repository;

import com.example.btljava.domain.Follow;
import com.example.btljava.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowed(User followed);

    void deleteByFollowerAndFollowed(User follower, User followed);
}
