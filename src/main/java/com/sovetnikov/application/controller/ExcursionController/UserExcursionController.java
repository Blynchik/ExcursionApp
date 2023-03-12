package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.LikeService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<ExcursionDto> getOne(@PathVariable int id) {

        if (excursionService.get(id).isPresent()) {
            return ResponseEntity.ok().body(Converter.getExcursionDto(excursionService.get(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<ExcursionDto>> getAll() {
        return ResponseEntity.ok().body(excursionService.getAll().stream()
                .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<ExcursionDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(excursionService.getByNameLike(query).stream()
                .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/own")
    public ResponseEntity<List<ExcursionDto>> getOwnExcursions(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(
                userService.getWithExcursions(authUser.id()).stream()
                        .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentDto>> getAllExcursionComments(@PathVariable int id) {
        return ResponseEntity.ok().body(commentService.getExcursionComment(id).stream()
                .map(Converter::getCommentDto).toList());
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> create(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable int id,
                                         @RequestParam
                                         @Size(max = 300, message = "Комментарий должен быть не более 300 знаков")
                                         @NotBlank(message = "Комментарий не должен быть пустым")
                                         String message) {


        if (excursionService.get(id).isPresent() && userService.get(authUser.id()).isPresent()) {

            Comment comment = new Comment(message,
                    userService.get(authUser.id()).get(),
                    excursionService.get(id).get());

            commentService.create(comment);

            return ResponseEntity.ok().body(Converter.getCommentDto(comment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<List<LikeDto>> getAllExcursionLikes(@PathVariable int id) {
        return ResponseEntity.ok().body(likeService.getExcursionLikes(id).stream()
                .map(Converter::getLikeDto).toList());
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Object> create(@AuthenticationPrincipal AuthUser authUser,
                                         @PathVariable int id) {


        if (excursionService.get(id).isPresent()
                && userService.get(authUser.id()).isPresent()) {

            if (likeService.getByExcursionAndUser(
                            excursionService.get(id).get(),
                            userService.get(authUser.id()).get())
                    .isPresent()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new RuntimeException("Уже существует").getMessage());
            }

            Like like = new Like(userService.get(authUser.id()).get(),
                    excursionService.get(id).get());

            likeService.create(like);

            return ResponseEntity.ok().body(Converter.getLikeDto(like));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
