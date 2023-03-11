package com.sovetnikov.application.controller;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.RegistrationService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import jakarta.validation.Valid;
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

    @PostMapping()
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto,
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
        return ResponseEntity.ok().body(Converter.getUserDto(user));
    }
}
