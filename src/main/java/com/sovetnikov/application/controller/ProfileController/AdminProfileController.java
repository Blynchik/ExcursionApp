package com.sovetnikov.application.controller.ProfileController;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto.BaseUserDto;
import com.sovetnikov.application.dto.UserDto.UserDto;
import com.sovetnikov.application.model.Role;
import com.sovetnikov.application.model.User;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.ErrorList;
import com.sovetnikov.application.util.UserUtils.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public AdminProfileController(UserService userService,
                                  UserValidator userValidator,
                                  CommentService commentService,
                                  LikeService likeService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @Operation(summary = "Доступна только администратору. " +
            "Возвращает контактные данные пользователя по id, " +
            "экскурсии, на которые он записан (название, дата выезда), " +
            "его комментарии (сообщение, имя, экскурсия, дата и время комментирования), " +
            "его лайки (имя, экскурсия). При false возвращает все экскурсии, " +
            "где записан пользотватель, включая прошедшие. При true возвращает только будущие экскурсии. " +
            "Экскурсии сортированы от ближайшей к более дальной. " +
            "Ответ 200, если пользователь найден, иначе 404.")
    @LogExecutionTime
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
                    .map(c -> {
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

    @Operation(summary = "Доступна только администратору." +
            " Возвращает список всех пользователей " +
            "в алфавитном порядке по 3 пользователя на странице. Нумерация страниц с 0." +
            " Пользователи возвращаются с информацией о имени, эл. почте и номере телефона")
    @LogExecutionTime
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

    @Operation(summary = "Доступна только для администратора. Создает нового пользователя." +
            " Администратор должен назначить роль. " +
            " При введении невалидных значений возвращает 400, иначе 200. " +
            " Ограничения. Пароль не может быть null, дополнительно шифруется перед " +
            " сохранением в БД. Имя не может быть пустым " +
            "и должно состоять от 2 до 100 знаков. " +
            "Электронная почта должна быть корректной (игнорируется регистр), уникальной, не превышать 100 знаков" +
            " и не должна быть пустой. " +
            "Номер телефона должен состоять из 10 цифр, быть уникальным и не пустым.")
    @LogExecutionTime
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

    @Operation(summary = "Доступна только администратору. " +
            "Удаляет пользователя по id. Все его записи на экскурсии отменяются. " +
            "Лайки удаляются. Комментарии остаются без пользователя. " +
            "Ответ 200, если такой id существует, иначе 404")
    @LogExecutionTime
    @DeleteMapping("/{id}")
    public ResponseEntity<List<UserDto>> delete(@PathVariable int id) {

        if (userService.get(id).isPresent()) {

            userService.delete(id);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Доступна только администратору. Полностью изменяет " +
            "личные данные пользователя по id. Все лайки, комментарии, экскурсии " +
            "остаются привязанными к этому пользователю. " +
            "Ограничения, такие же как и при создании пользователя." +
            "Ответ 200, если такой id есть, иначе 404. При ошибке - 400")
    @LogExecutionTime
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
