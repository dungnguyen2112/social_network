package com.example.btljava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.btljava.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByEmail(String email);

    User findByUsername(String email);

    boolean existsByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

}
