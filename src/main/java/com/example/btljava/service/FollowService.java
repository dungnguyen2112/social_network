package com.example.btljava.service;

import com.example.btljava.domain.Follow;
import com.example.btljava.domain.User;
import com.example.btljava.util.error.FollowException;
import com.example.btljava.repository.FollowRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void followUser(User follower, User followed) {
        if (follower.getId().equals(followed.getId())) {
            throw new FollowException("Cannot follow yourself.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        follow.setCreatedAt(Instant.now());
        followRepository.save(follow);
    }

    public void unfollowUser(User follower, User followed) {
        if (follower.getId().equals(followed.getId())) {
            throw new FollowException("Cannot unfollow yourself.");
        }
        followRepository.deleteByFollowerAndFollowed(follower, followed);
    }

    public List<User> getFollowers(User user) {
        List<Follow> follows = followRepository.findByFollowed(user);
        return follows.stream()
                .map(Follow::getFollower)
                .toList();
    }

    public List<User> getFollowing(User user) {
        List<Follow> follows = followRepository.findByFollower(user);
        return follows.stream()
                .map(Follow::getFollowed)
                .toList();
    }
}
