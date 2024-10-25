package com.example.btljava.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.btljava.domain.Permission;
import com.example.btljava.domain.Role;
import com.example.btljava.domain.User;
import com.example.btljava.repository.PermissionRepository;
import com.example.btljava.repository.RoleRepository;
import com.example.btljava.repository.UserRepository;
import com.example.btljava.util.constant.GenderEnum;

@Service
public class DatabaseInitializer implements CommandLineRunner {

        private final PermissionRepository permissionRepository;
        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        public DatabaseInitializer(
                        PermissionRepository permissionRepository,
                        RoleRepository roleRepository,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
                this.permissionRepository = permissionRepository;
                this.roleRepository = roleRepository;
                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @Override
        public void run(String... args) throws Exception {
                System.out.println(">>> START INIT DATABASE");
                long countPermissions = this.permissionRepository.count();
                long countRoles = this.roleRepository.count();
                long countUsers = this.userRepository.count();

                if (countPermissions == 0) {
                        ArrayList<Permission> arr = new ArrayList<>();
                        arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST",
                                        "PERMISSIONS"));
                        arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT",
                                        "PERMISSIONS"));
                        arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}",
                                        "DELETE", "PERMISSIONS"));
                        arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}",
                                        "GET", "PERMISSIONS"));
                        arr.add(new Permission("Get permissions with pagination",
                                        "/api/v1/permissions", "GET", "PERMISSIONS"));

                        arr.add(new Permission("Create a role", "/api/v1/roles", "POST", "ROLES"));
                        arr.add(new Permission("Update a role", "/api/v1/roles", "PUT", "ROLES"));
                        arr.add(new Permission("Delete a role", "/api/v1/roles/{id}", "DELETE",
                                        "ROLES"));
                        arr.add(new Permission("Get a role by id", "/api/v1/roles/{id}", "GET",
                                        "ROLES"));
                        arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET",
                                        "ROLES"));
                        // New Comment permissions
                        arr.add(new Permission("Add a new comment to the post",
                                        "/api/v1/posts/{postId}/users/{userId}/comments",
                                        "POST", "COMMENTS"));
                        arr.add(new Permission("Update a comment", "/api/v1/comments/{commentId}",
                                        "PUT", "COMMENTS"));
                        arr.add(new Permission("Delete a comment", "/api/v1/comments/{commentId}",
                                        "DELETE", "COMMENTS"));
                        arr.add(new Permission("Fetch all comments for a post",
                                        "/api/v1/posts/{postId}/comments", "GET",
                                        "COMMENTS"));
                        arr.add(new Permission("Fetch all comments by a user with pagination",
                                        "/api/v1/users/{userId}/comments",
                                        "GET", "COMMENTS"));

                        // Like/Unlike permissions for posts
                        arr.add(new Permission("Like a post", "/api/v1/like/posts/{postId}", "POST",
                                        "LIKES"));
                        arr.add(new Permission("Unlike a post",
                                        "/api/v1/unlike/posts/{postId}", "DELETE", "LIKES"));
                        arr.add(new Permission("Get liked posts by user",
                                        "/api/v1/like/posts/user", "GET", "LIKES"));

                        // Like/Unlike permissions for comments
                        arr.add(new Permission("Like a comment", "/api/v1/like/comments/{commentId}",
                                        "POST", "LIKES"));
                        arr.add(new Permission("Unlike a comment",
                                        "/api/v1/unlike/comments/{commentId}", "DELETE",
                                        "LIKES"));
                        arr.add(new Permission("Get liked comments by user",
                                        "/api/v1/like/comments/user", "GET",
                                        "LIKES"));

                        arr.add(new Permission("Create a user", "/api/v1/users/create", "POST",
                                        "USERS"));
                        arr.add(new Permission("Update a user", "/api/v1/users/update", "PUT",
                                        "USERS"));
                        arr.add(new Permission("Delete a user", "/api/v1/users/{id}", "DELETE",
                                        "USERS"));
                        arr.add(new Permission("Get a user by id", "/api/v1/users/{id}", "GET",
                                        "USERS"));
                        arr.add(new Permission("Get users with pagination", "/api/v1/users", "GET",
                                        "USERS"));

                        // Follow-related permissions, as defined before...
                        arr.add(new Permission("Follow a user", "/api/users/{userId}/follow", "POST",
                                        "FOLLOW"));
                        arr.add(new Permission("Unfollow a user", "/api/users/{userId}/unfollow",
                                        "POST", "FOLLOW"));
                        arr.add(new Permission("Get followers of a user",
                                        "/api/users/{userId}/followers", "GET", "FOLLOW"));
                        arr.add(new Permission("Get following list of a user",
                                        "/api/users/{userId}/following", "GET", "FOLLOW"));

                        // Post-related permissions
                        arr.add(new Permission("Create a new post", "/api/v1/posts/create", "POST",
                                        "POSTS"));
                        arr.add(new Permission("Delete a post", "/api/v1/posts/{id}", "DELETE",
                                        "POSTS"));
                        arr.add(new Permission("Fetch a post by id", "/api/v1/posts/{id}", "GET",
                                        "POSTS"));
                        arr.add(new Permission("Fetch all posts with pagination and filtering",
                                        "/api/v1/posts", "GET", "POSTS"));
                        arr.add(new Permission("Update a post", "/api/v1/posts/update", "PUT",
                                        "POSTS"));

                        // Notification permissions
                        arr.add(new Permission("Read a notification", "/api/v1/notifications/{notificationId}/read",
                                        "PUT",
                                        "NOTIFICATIONS"));
                        arr.add(new Permission("Fetch all notifications by user", "/api/v1/notifications/user/{userId}",
                                        "GET", "NOTIFICATIONS"));
                        arr.add(new Permission("Delete a notification", "/api/v1/notifications/{notificationId}",
                                        "DELETE", "NOTIFICATIONS"));
                        arr.add(new Permission("Get all notifications of current user",
                                        "/api/v1/notifications/user/current",
                                        "GET", "NOTIFICATIONS"));

                        this.permissionRepository.saveAll(arr);
                }
                if (countRoles == 0 || countPermissions == 0) {
                        List<Permission> allPermissions = this.permissionRepository.findAll();

                        // Tạo role SUPER_ADMIN với toàn bộ quyền
                        Role adminRole = new Role();
                        adminRole.setName("SUPER_ADMIN");
                        adminRole.setDescription("Admin có toàn quyền");
                        adminRole.setActive(true);
                        adminRole.setPermissions(allPermissions);
                        this.roleRepository.save(adminRole);

                        // Lọc quyền cho role USER dựa trên các permission liên quan
                        List<Permission> userPermissions = new ArrayList<>();

                        // Quyền về bài viết (Post)
                        userPermissions.add(this.permissionRepository.findByName("Create a new post"));
                        userPermissions.add(this.permissionRepository.findByName("Update a post"));
                        userPermissions.add(this.permissionRepository.findByName("Delete a post"));
                        userPermissions.add(this.permissionRepository
                                        .findByName("Fetch all posts with pagination and filtering"));

                        // Quyền về Like/Unlike cho bài viết và bình luận
                        userPermissions.add(this.permissionRepository.findByName("Like a post"));
                        userPermissions.add(this.permissionRepository.findByName("Unlike a post"));
                        userPermissions.add(this.permissionRepository.findByName("Like a comment"));
                        userPermissions.add(this.permissionRepository.findByName("Unlike a comment"));

                        // Quyền về Comment
                        userPermissions.add(this.permissionRepository.findByName("Add a new comment to the post"));
                        userPermissions.add(this.permissionRepository.findByName("Update a comment"));
                        userPermissions.add(this.permissionRepository.findByName("Delete a comment"));

                        // Quyền Follow/Unfollow
                        userPermissions.add(this.permissionRepository.findByName("Follow a user"));
                        userPermissions.add(this.permissionRepository.findByName("Unfollow a user"));

                        // **Thêm các quyền về xem danh sách follower và following**
                        userPermissions.add(this.permissionRepository.findByName("Get followers of a user"));
                        userPermissions.add(this.permissionRepository.findByName("Get following list of a user"));

                        // Quyền về xóa tài khoản user (nếu cần)
                        userPermissions.add(this.permissionRepository.findByName("Get a user by id"));
                        userPermissions.add(this.permissionRepository.findByName("Delete a user"));

                        userPermissions.add(this.permissionRepository.findByName("Read a notification"));
                        userPermissions.add(
                                        this.permissionRepository.findByName("Get all notifications of current user"));
                        userPermissions.add(this.permissionRepository.findByName("Delete a notification"));
                        // Tạo role USER và gán quyền đã lọc
                        Role userRole = new Role();
                        userRole.setName("USER");
                        userRole.setDescription("Người dùng thông thường");
                        userRole.setActive(true);
                        userRole.setPermissions(userPermissions); // Gán các quyền hạn phù hợp
                        this.roleRepository.save(userRole);
                }

                if (countUsers == 0) {
                        User adminUser = new User();
                        adminUser.setUsername("admin");
                        adminUser.setEmail("admin@gmail.com");
                        adminUser.setAddress("hn");
                        adminUser.setAge(25);
                        adminUser.setGender(GenderEnum.MALE);
                        adminUser.setName("I'm super admin");
                        adminUser.setPasswordHash(this.passwordEncoder.encode("123456"));

                        Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
                        if (adminRole != null) {
                                adminUser.setRole(adminRole);
                        }

                        this.userRepository.save(adminUser);
                }

                if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
                        System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
                } else
                        System.out.println(">>> END INIT DATABASE");
        }

}
