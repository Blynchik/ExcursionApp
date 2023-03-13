package com.sovetnikov.application.controller.ExcursionController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.model.*;
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
import java.util.Optional;

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

        Optional<Excursion> excursion = excursionService.get(id);

        if (excursion.isPresent()) {
            ExcursionDto excursionDto = Converter.getExcursionDto(excursion.get());
            excursionDto.setLikesAmount(excursionService.getLikesAmount(id));

            return ResponseEntity.ok().body(excursionDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<List<ExcursionDto>> getAll(@RequestParam(required = false) boolean onlyNext,
                                                     @RequestParam(required = false, defaultValue = "0") int page) {
        return ResponseEntity.ok().body(excursionService.getAll(page, onlyNext).stream()
                .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/findByName")
    public ResponseEntity<List<ExcursionDto>> getByName(@RequestParam String query) {
        return ResponseEntity.ok().body(excursionService.getByNameLike(query).stream()
                .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/own")
    public ResponseEntity<List<ExcursionDto>> getOwnExcursions(@AuthenticationPrincipal AuthUser authUser,
                                                               @RequestParam boolean onlyNext) {
        return ResponseEntity.ok().body(
                userService.getWithExcursions(authUser.id(), onlyNext).stream()
                        .map(Converter::getExcursionDto).toList());
    }

    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentDto>> getAllExcursionComments(@PathVariable int id) {
        return ResponseEntity.ok().body(commentService.getExcursionComment(id).stream()
                .map(Converter::getCommentDto).toList());
    }

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
    public ResponseEntity<Object> createLike(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable int id) {

        Optional<Excursion> excursion = excursionService.get(id);
        Optional<User> user = userService.get(authUser.id());

        if (excursion.isPresent()
                && user.isPresent()) {

            if (likeService.getByExcursionAndUser(
                            excursion.get(),
                            user.get())
                    .isPresent()) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new RuntimeException("Уже существует").getMessage());
            }

            Like like = new Like(user.get(),
                    excursion.get());

            likeService.create(like);

            return ResponseEntity.ok().body(Converter.getLikeDto(like));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/own/attention")
    public ResponseEntity<ExcursionDto> getAttention(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(Converter.getExcursionDto(
                userService.getTimeTillExcursion(authUser.id())));
    }
}
