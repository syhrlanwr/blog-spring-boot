package com.syhrl.blog.repository;

import java.util.Optional;

import com.syhrl.blog.entity.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
