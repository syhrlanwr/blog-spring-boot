package com.syhrl.blog.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

public class PasswordConfirmationValidator implements ConstraintValidator<PasswordConfirmation, Object> {
    private String passwordField;
    private String passwordConfirmationField;

    @Override
    public void initialize(PasswordConfirmation constraintAnnotation) {
        this.passwordField = constraintAnnotation.password();
        this.passwordConfirmationField = constraintAnnotation.passwordConfirmation();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object password = new BeanWrapperImpl(value).getPropertyValue(passwordField);
        Object passwordConfirmation = new BeanWrapperImpl(value).getPropertyValue(passwordConfirmationField);
        if (password != null) {
            return password.equals(passwordConfirmation);
        }
        return passwordConfirmation == null;
    }
}
