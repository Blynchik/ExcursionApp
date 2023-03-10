package com.sovetnikov.application.controller;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sovetnikov.application.util.Converter;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable final int id) {

        if (userService.get(id).isPresent()) {
            return ResponseEntity.ok().body(userService.get(id).get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok().body(userService.getAll());
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto,
                                           BindingResult bindingResult,
                                           @RequestParam String password) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        User user = new User();
        Converter.getUser(user, userDto);
        user.setPassword(password);
        userService.create(user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<User>> delete(@PathVariable int id) {

        if (userService.get(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.ok().body(userService.getAll());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                                         @RequestBody @Valid UserDto userDto,
                                         BindingResult bindingResult) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (userService.get(id).isPresent()) {
            User user = new User();
            Converter.getUser(user, userDto);
            userService.update(user, id);
            return ResponseEntity.ok().body(userService.get(id).get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
