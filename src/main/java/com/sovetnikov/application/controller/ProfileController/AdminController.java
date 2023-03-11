package com.sovetnikov.application.controller.ProfileController;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.UserUtils.ErrorList;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sovetnikov.application.util.UserUtils.Converter;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable final int id) {

        if (userService.get(id).isPresent()) {
            return ResponseEntity.ok().body(Converter.getUserDto(userService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok().body(userService.getAll().stream()
                .map(Converter::getUserDto).toList());
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

        return ResponseEntity.ok().body(Converter.getUserDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<UserDto>> delete(@PathVariable int id) {

        if (userService.get(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.ok().body(userService.getAll().stream()
                    .map(Converter::getUserDto).toList());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                                         @Valid @RequestBody UserDto userDto,
                                         BindingResult bindingResult,
                                         @RequestParam(required = false) String password,
                                         @RequestParam(required = false) Role role) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (userService.get(id).isPresent()) {
            User user = new User();
            Converter.getUser(user, userDto);
            userService.update(user, id);

            if(password!=null &&!password.isEmpty() && !password.isBlank()){
                userService.changePassword(id, password);
            }

            if(role!=null && !role.name().isBlank() && !role.name().isEmpty()){
                userService.changeAuthority(id, role);
            }

            return ResponseEntity.ok().body(Converter.getUserDto(userService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/byName")
    public ResponseEntity<List<UserDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(userService.getByNameLike(query).stream()
                .map(Converter::getUserDto).toList());
    }

}
