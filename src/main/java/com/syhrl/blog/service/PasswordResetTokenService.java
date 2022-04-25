package com.syhrl.blog.service;

import com.syhrl.blog.entity.PasswordResetToken;

public interface PasswordResetTokenService {
    PasswordResetToken getPasswordResetToken(String token);
    PasswordResetToken savePasswordResetToken(PasswordResetToken passwordResetToken);
}
