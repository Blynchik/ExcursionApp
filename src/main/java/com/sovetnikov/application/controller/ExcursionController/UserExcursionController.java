package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.model.*;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/excursion")
public class UserExcursionController {

    private final ExcursionService excursionService;
    private final CommentService commentService;
    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public UserExcursionController(ExcursionService excursionService,
                                   UserService userService,
                                   CommentService commentService,
                                   LikeService likeService) {
        this.excursionService = excursionService;
        this.userService = userService;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает название экскурсии, дату проведения, цену " +
            "и количество лайков по id экскурсии. Ответ 200, если id " +
            "существует, иначе 404")
    @LogExecutionTime
    @GetMapping("/{id}")
    public ResponseEntity<ExcursionDto> getOne(@PathVariable int id) {

        Optional<Excursion> excursion = excursionService.get(id);

        if (excursion.isPresent()) {
            ExcursionDto excursionDto = Converter.getExcursionDto(excursion.get());
            excursionDto.setDate(excursion.get().getDate());
            excursionDto.setPrice(excursion.get().getPrice());
            excursionDto.setLikesAmount(excursionService.getLikesAmount(id));

            return ResponseEntity.ok().body(excursionDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает список экскурсий с названием, датой проведения " +
            "и описанием. Есть разделение на страницы по 3 экскурсии, отсчет с 0. " +
            "При false возращает все экскурсии, сортированные по дате от самой дальней." +
            "При true возрващает только будущие экскурсии, начиная с самой ближайшей. " +
            "Ответ 200")
    @LogExecutionTime
    @GetMapping
    public ResponseEntity<List<ExcursionDto>> getAll(@RequestParam boolean onlyNext,
                                                     @RequestParam(required = false, defaultValue = "0") int page) {
        return ResponseEntity.ok().body(excursionService.getAll(page, onlyNext).stream()
                .map(e -> {
                    ExcursionDto exc = Converter.getExcursionDto(e);
                    exc.setDate(e.getDate());
                    exc.setDescription(e.getDescription());
                    return exc;
                }).collect(Collectors.toList()));
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Ищет экскурсии по совпадающим первым буквам " +
            "и возвращает список с названием, описанием и " +
            "датой проведения. Список отсортирован по алфавиту. " +
            "Ответ 200")
    @LogExecutionTime
    @GetMapping("/findByName")
    public ResponseEntity<List<ExcursionDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(excursionService.getByNameLike(query).stream()
                .map(e -> {
                    ExcursionDto exc = Converter.getExcursionDto(e);
                    exc.setDate(e.getDate());
                    exc.setDescription(e.getDescription());
                    return exc;
                }).collect(Collectors.toList()));
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает список экскурсий данного пользователя, на которые он записан. " +
            "При false вернутся все экскурсии пользователя, включая прошедшие. " +
            "Список отсортирован по дате проведения." +
            "При true вернутся только будущие экскурсии пользователя. " +
            "Список отсортирован от самой ближайшей до самой дальней.")
    @LogExecutionTime
    @GetMapping("/own")
    public ResponseEntity<List<ExcursionDto>> getOwnExcursions(@AuthenticationPrincipal AuthUser authUser,
                                                               @RequestParam boolean onlyNext) {
        return ResponseEntity.ok().body(
                userService.getWithExcursions(authUser.id(), onlyNext).stream()
                        .map(e -> {
                            ExcursionDto exc = Converter.getExcursionDto(e);
                            exc.setDate(e.getDate());
                            exc.setDate(e.getDate());
                            return exc;
                        }).toList());
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает список комментариев экскурсии по ее id с именем пользователя " +
            "и временем комментирования. Список отсртирован по времени. " +
            "Ответ 200, если id существует, иначе 404")
    @LogExecutionTime
    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentDto>> getAllExcursionComments(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {

            return ResponseEntity.ok().body(commentService.getExcursionComment(id).stream()
                    .map(Converter::getCommentDto).toList());
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Создает комментарий к экскурсии по ее id от " +
            "имени данного пользователя. Сообщение не " +
            "должно превышать 300 знаков. " +
            "Ответ 200, если id существует, иначе 404")
    @LogExecutionTime
    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@AuthenticationPrincipal AuthUser authUser,
                                                @PathVariable int id,
                                                @RequestParam
                                                @Size(max = 300, message = "Комментарий должен быть не более 300 знаков")
                                                @NotBlank(message = "Комментарий не должен быть пустым")
                                                String message) {

        Optional<Excursion> excursion = excursionService.get(id);
        Optional<User> user = userService.get(authUser.id());

        if (excursion.isPresent() && user.isPresent()) {

            Comment comment = new Comment(message,
                    user.get(),
                    excursion.get());

            commentService.create(comment);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает список лайков экскурсии по ее id с именем " +
            "пользователя и названием экскурсии. " +
            "Ответ 200, если экскурсия существует, иначе 404")
    @LogExecutionTime
    @GetMapping("/{id}/like")
    public ResponseEntity<List<LikeDto>> getAllExcursionLike(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            return ResponseEntity.ok().body(likeService.getExcursionLikes(id).stream()
                    .map(Converter::getLikeDto).toList());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Создает лайк для экскурсии по ее id. Все " +
            "лайки уникальны, один пользователь может поставить " +
            "лайк для одной экскурсии только один раз. " +
            "Ответ 200, если экскурсия существует, иначе 404. " +
            "Повторная попытка поставить лайк вернет 400")
    @LogExecutionTime
    @PostMapping("/{id}/like")
    public ResponseEntity<Object> createLike(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable int id) {

        Optional<Excursion> excursion = excursionService.get(id);
        Optional<User> user = userService.get(authUser.id());

        if (excursion.isPresent()
                && user.isPresent()) {

            if (likeService.getByExcursionAndUser(excursion.get(), user.get())
                    .isPresent()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new RuntimeException("Уже существует").getMessage());
            }

            Like like = new Like(user.get(), excursion.get());

            likeService.create(like);

            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает ближайшую по дате экскурсию, на " +
            "которую записан данный пользователь. Возвращается " +
            "название, дата экскурсии и количество дней до экскурсии." +
            "Ответ 200")
    @LogExecutionTime
    @GetMapping("/own/attention")
    public ResponseEntity<ExcursionDto> getAttention(@AuthenticationPrincipal AuthUser authUser) {

        Excursion excursion = userService.getTimeTillExcursion(authUser.id());
        ExcursionDto excursionDto = Converter.getExcursionDto(excursion);
        excursionDto.setDate(excursion.getDate());
        excursionDto.setDaysTillExcursion(excursion.getDaysTillExcursion());

        return ResponseEntity.ok().body(excursionDto);
    }
}
