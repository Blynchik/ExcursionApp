package com.sovetnikov.application.controller;

import com.sovetnikov.application.dto.UserDto.BaseUserDto;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.RegistrationService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;

    @Autowired
    public RegistrationController(RegistrationService registrationService, UserValidator userValidator) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
    }

    @Operation(summary="Создает нового пользователя без авторизации c правами пользователя. " +
            "Едиственная доступная функция для анонимных пользователей. " +
            "После авторизации недоступна. " +
            "При введении невалидных значений возвращает 400, иначе 200. " +
            "Ограничения. Пароль не может быть null, дополнительно шифруется перед " +
            " сохранением в БД. Имя не может быть пустым " +
            "и должно состоять от 2 до 100 знаков. " +
            "Электронная почта должна быть корректной (игнорируется регистр), уникальной, не превышать 100 знаков" +
            " и не должна быть пустой. " +
            "Номер телефона должен состоять из 10 цифр, быть уникальным и не пустым.")
    @PostMapping()
    public ResponseEntity<Object> register(@Valid @RequestBody BaseUserDto userDto,
                                           BindingResult bindingResult,
                                           @RequestParam String password) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        User user = Converter.getUser(userDto);
        user.setPassword(password);
        registrationService.register(user);

        return ResponseEntity.ok().build();
    }
}
