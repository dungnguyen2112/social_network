package com.example.btljava.service;

import org.springframework.stereotype.Service;
import com.example.btljava.domain.*;
import com.example.btljava.domain.response.LikeResponseDTO;
import com.example.btljava.repository.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LikeService {

        private final LikeRepository likeRepository;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;
        private final UserRepository userRepository;
        private final NotificationRepository notificationRepository; // Thêm repository này

        public LikeService(LikeRepository likeRepository, PostRepository postRepository,
                        CommentRepository commentRepository, UserRepository userRepository,
                        NotificationRepository notificationRepository) { // Thêm tham số này
                this.likeRepository = likeRepository;
                this.postRepository = postRepository;
                this.commentRepository = commentRepository;
                this.userRepository = userRepository;
                this.notificationRepository = notificationRepository; // Khởi tạo NotificationRepository
        }

        public void likePost(Long userId, Long postId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                // Kiểm tra xem người dùng đã thích bài viết này chưa
                if (likeRepository.findByUserAndPost(user, post).isPresent()) {
                        throw new RuntimeException("User has already liked this post");
                }

                Like like = new Like();
                like.setUser(user);
                like.setPost(post);
                like.setCreatedAt(Instant.now());

                likeRepository.save(like);

                // Tạo thông báo cho chủ sở hữu bài viết
                createNotification(post.getUser(), user, "liked your post");
        }

        public void likeComment(Long userId, Long commentId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new RuntimeException("Comment not found"));

                Like like = new Like();
                like.setUser(user);
                like.setComment(comment);
                like.setCreatedAt(Instant.now());

                likeRepository.save(like);

                // Tạo thông báo cho chủ sở hữu bình luận
                createNotification(comment.getUser(), user, "liked your comment");
        }

        public void unlikePost(Long userId, Long postId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RuntimeException("Post not found"));

                Like like = likeRepository.findByUserAndPost(user, post)
                                .orElseThrow(() -> new RuntimeException("Like not found"));
                likeRepository.delete(like);
        }

        public void unlikeComment(Long userId, Long commentId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));
                Comment comment = commentRepository.findById(commentId)
                                .orElseThrow(() -> new RuntimeException("Comment not found"));

                Like like = likeRepository.findByUserAndComment(user, comment)
                                .orElseThrow(() -> new RuntimeException("Like not found"));
                likeRepository.delete(like);
        }

        // Phương thức mới để lấy các bài viết đã thích của người dùng
        public List<LikeResponseDTO> getLikedPostsByUser(User user) {
                List<Like> likes = likeRepository.findByUser(user);
                return likes.stream()
                                .filter(like -> like.getPost() != null) // Chỉ lấy likes cho bài viết
                                .map(like -> new LikeResponseDTO(
                                                like.getId(),
                                                like.getPost().getId(),
                                                like.getPost().getUser().getId(),
                                                like.getComment().getId(),
                                                like.getCreatedAt()))
                                .collect(Collectors.toList());
        }

        // Phương thức để lấy các bình luận đã thích của người dùng
        public List<LikeResponseDTO> getLikedCommentsByUser(User user) {
                List<Like> likes = likeRepository.findByUser(user);
                return likes.stream()
                                .filter(like -> like.getComment() != null) // Chỉ lấy likes cho bình luận
                                .map(like -> new LikeResponseDTO(
                                                like.getId(),
                                                null, // Không có postId vì đây là bình luận
                                                like.getComment().getUser().getId(),
                                                like.getComment().getId(),
                                                like.getCreatedAt()))
                                .collect(Collectors.toList());
        }

        // Phương thức tạo thông báo
        private void createNotification(User recipient, User sender, String content) {
                Notification notification = new Notification();
                notification.setUser(recipient);
                notification.setContent(sender.getUsername() + " " + content);
                notification.setCreatedAt(Instant.now());
                notification.setIsRead(false);

                notificationRepository.save(notification);
        }
}
