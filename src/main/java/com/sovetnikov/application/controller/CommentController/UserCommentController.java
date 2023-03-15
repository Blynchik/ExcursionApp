package com.sovetnikov.application.controller.CommentController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.util.Converter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user/comment")
public class UserCommentController {

    private final CommentService commentService;

    @Autowired
    public UserCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Возвращает все комментарии данного пользователя " +
            "с именем, нащванием экскурсии, сообщением и временем кмментирования." +
            "Все комментарии отсортированы по времени." +
            "Ответ 200")
    @GetMapping("/own")
    public ResponseEntity<List<CommentDto>> getAllUserComments(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(commentService.getUserComment(authUser.id()).stream()
                .map(c -> {
                    CommentDto commentDto = Converter.getCommentDto(c);
                    commentDto.setExcursionDto(Converter.getExcursionDto(c.getExcursion()));
                    return commentDto;
                }).toList());
    }

    @Operation(summary = "Доступна всем пользователям. " +
            "Удаляет комментарий данного пользователя по id комментария. " +
            "Если комментарий не принадлежит пользователю - ответ 400." +
            "Ответ 200, если такой комментарий существует, иначе 404")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable int id) {

        Optional<Comment> comment = commentService.get(id);

        if (comment.isPresent()) {

            if (comment.get().getUser().getId() == authUser.id()) {
                commentService.delete(id);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
