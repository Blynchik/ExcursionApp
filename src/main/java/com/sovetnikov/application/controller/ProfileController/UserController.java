package com.sovetnikov.application.controller.ProfileController;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, UserValidator userValidator) {
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

    @DeleteMapping()
    public ResponseEntity<List<User>> delete(@AuthenticationPrincipal AuthUser authUser) {

        if (userService.get(authUser.id()).isPresent()) {
            userService.delete(authUser.id());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping()
    public ResponseEntity<Object> update(@AuthenticationPrincipal AuthUser authUser,
                                         @RequestBody @Valid UserDto userDto,
                                         BindingResult bindingResult) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (userService.get(authUser.id()).isPresent()) {
            User user = new User();
            Converter.getUser(user, userDto);
            userService.update(user, authUser.id());
            return ResponseEntity.ok().body(Converter.getUserDto(userService.get(authUser.id()).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}