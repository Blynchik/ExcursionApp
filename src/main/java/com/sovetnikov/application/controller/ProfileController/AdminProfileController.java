package com.sovetnikov.application.controller.ProfileController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto.BaseUserDto;
import com.sovetnikov.application.dto.UserDto.UserDto;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.repository.UserRepository;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.sovetnikov.application.util.Converter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/user")
public class AdminProfileController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final CommentService commentService;
    private final LikeService likeService;
    private final UserRepository userRepository;

    @Autowired
    public AdminProfileController(UserService userService,
                                  UserValidator userValidator,
                                  CommentService commentService,
                                  LikeService likeService,
                                  UserRepository userRepository) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.commentService = commentService;
        this.likeService = likeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getOne(@PathVariable int id,
                                          @RequestParam boolean onlyNext) {

        Optional<User> user = userService.get(id);

        if (user.isPresent()) {

            UserDto userDto = Converter.getUserDto(user.get());
            userDto.setEmail(user.get().getEmail());
            userDto.setPhoneNumber(user.get().getPhoneNumber());

            userDto.setExcursions(userService.getWithExcursions(id, onlyNext).stream()
                    .map(e -> {
                        ExcursionDto exc = Converter.getExcursionDto(e);
                        exc.setDate(e.getDate());
                        return exc;
                    }).toList());

            userDto.setComments(commentService.getUserComment(id).stream()
                    .map(c->{
                        CommentDto comm = Converter.getCommentDto(c);
                        comm.setExcursionDto(Converter.getExcursionDto(c.getExcursion()));
                        return comm;
                    }).toList());

            userDto.setLike(likeService.getUserLikes(id).stream()
                    .map(Converter::getLikeDto).toList());

            return ResponseEntity.ok().body(userDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok().body(userService.getAll(page).stream()
                .map(u -> {
                    UserDto user = Converter.getUserDto(u);
                    user.setEmail(u.getEmail());
                    user.setPhoneNumber(u.getPhoneNumber());
                    return user;
                }).collect(Collectors.toList()));
    }

    @PostMapping()
    public ResponseEntity<Object> create(@Valid @RequestBody BaseUserDto userDto,
                                         BindingResult bindingResult,
                                         @RequestParam String password) {

        userValidator.validate(userDto, bindingResult);

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        User user = Converter.getUser(userDto);
        user.setPassword(password);
        userService.create(user);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<UserDto>> delete(@PathVariable int id) {

        if (userService.get(id).isPresent()) {
            userService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id,
                                         @Valid @RequestBody BaseUserDto userDto,
                                         BindingResult bindingResult,
                                         @RequestParam(required = false) String password,
                                         @RequestParam(required = false) Role role) {

        userValidator.validate(userDto, bindingResult);


        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorList.getList(bindingResult));
        }

        if (userService.get(id).isPresent()) {
            User user = Converter.getUser(userDto);
            userService.update(user, id);

            if (password != null && !password.isEmpty() && !password.isBlank()) {
                userService.changePassword(id, password);
            }

            if (role != null && !role.name().isBlank() && !role.name().isEmpty()) {
                userService.changeAuthority(id, role);
            }

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
