package com.sbilhbank.insur.validation.user;

import com.sbilhbank.insur.entity.primary.User;
import com.sbilhbank.insur.service.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@Configuration
public class UsernameIsExistOnDBValidate implements ConstraintValidator<UsernameIsExistOnDBValidator, String> {
    private static final UsernameIsExistOnDBValidate holder = new UsernameIsExistOnDBValidate();
    private UserService userService;

    @Bean
    public static UsernameIsExistOnDBValidate bean(UserService userService) {
        holder.userService = userService;
        return holder;
    }

    @Override
    public void initialize(UsernameIsExistOnDBValidator constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<User> user = holder.userService.findByUsername(value);
        return !user.isPresent();
    }
}
