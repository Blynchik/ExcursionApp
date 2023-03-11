package com.sovetnikov.application.controller.ProfileController;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.UserUtils.ErrorList;
import com.sovetnikov.application.util.UserUtils.UserValidator;
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
public class UserProfileController {

    private final UserService userService;
    private final UserValidator userValidator;

    @Autowired
    public UserProfileController(UserService userService, UserValidator userValidator) {
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
                                         BindingResult bindingResult,
                                         @RequestParam(required = false) String password,
                                         @RequestParam(required = false) Role role) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (userService.get(authUser.id()).isPresent()) {
            User user = new User();
            Converter.getUser(user, userDto);
            userService.update(user, authUser.id());

            if(password!=null &&!password.isEmpty() && !password.isBlank()){
                userService.changePassword(authUser.id(), password);
            }

            if(role!=null && !role.name().isBlank() && !role.name().isEmpty()){
                userService.changeAuthority(authUser.id(), role);
            }

            return ResponseEntity.ok().body(Converter.getUserDto(userService.get(authUser.id()).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/byName")
    public ResponseEntity<List<UserDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(userService.getByNameLike(query).stream()
                .map(Converter::getUserDto).toList());
    }
}
