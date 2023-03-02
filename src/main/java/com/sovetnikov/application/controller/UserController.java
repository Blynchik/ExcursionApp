package com.sovetnikov.application.controller;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.ErrorList;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto,
                                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorList.getList(bindingResult));
        }

        User user = new User();
        userService.create(Converter.getUser(user, userDto));
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

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ErrorList.getList(bindingResult));
        }

        if (userService.get(id).isPresent()) {
            userService.update(Converter.getUser(userService.get(id).get(), userDto), id);
            return ResponseEntity.ok().body(userService.get(id).get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
