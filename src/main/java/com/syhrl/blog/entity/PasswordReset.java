package com.syhrl.blog.entity;

import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.syhrl.blog.validator.PasswordConfirmation;

import lombok.Data;

@Data
@PasswordConfirmation(password = "password", passwordConfirmation = "passwordConfirmation", message = "PASSWORD_NOT_EQUAL")
@Table(name = "password_reset")
public class PasswordReset {
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordConfirmation;
    @NotEmpty
    private String token;
}
