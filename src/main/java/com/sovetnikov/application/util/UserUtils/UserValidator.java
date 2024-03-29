package com.sovetnikov.application.util.UserUtils;

import com.sovetnikov.application.dto.UserDto.BaseUserDto;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//Валидирует введенные поля для пользователя дополнительно
@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return BaseUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BaseUserDto user = (BaseUserDto) target;

        if (userService.getByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Электронная почта уже используется");
        }

        if (userService.getByPhoneNumber(user.getPhoneNumber()).isPresent()) {
            errors.rejectValue("phoneNumber", "", "Номер телефона уже используется");
        }
    }
}
