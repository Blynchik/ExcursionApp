package com.sovetnikov.application.util;

import com.sovetnikov.application.config.SecurityConfig;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDto user = (UserDto)target;

        if (userService.getByEmail(user.getEmail()).isPresent()){
            errors.rejectValue("email", "", "Электронная почта уже используется");
        }

        if (userService.getByPhoneNumber(user.getPhoneNumber()).isPresent()){
            errors.rejectValue("phoneNumber", "", "Номер телефона уже используется");
        }
    }
}
