package com.syhrl.blog.repository;

import java.util.Optional;

import com.syhrl.blog.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
