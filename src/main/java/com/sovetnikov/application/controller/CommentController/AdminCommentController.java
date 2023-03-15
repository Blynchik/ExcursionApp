package com.sovetnikov.application.controller.CommentController;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.util.Converter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/user")
public class AdminCommentController {

    private final CommentService commentService;

    @Autowired
    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Доступна только администратору." +
            "Возвращает комментарий по его id с сообщением, названием экскурсии, " +
            "именем пользователя и временем комментирования." +
            "Ответ 200, если комментарий существует, иначе 404")
    @LogExecutionTime
    @GetMapping("/comment/{id}")
    public ResponseEntity<CommentDto> getOne(@PathVariable int id) {

        Optional<Comment> comment = commentService.getCommentWithUserAndExcursion(id);

        if (commentService.get(id).isPresent()) {
            CommentDto commentDto = Converter.getCommentDto(comment.get());
            commentDto.setExcursionDto(Converter.getExcursionDto(comment.get().getExcursion()));
            return ResponseEntity.ok().body(commentDto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Доступна только администратору. " +
            "Возвращает все комментарии пользователя по его id " +
            "с сообщением, именем, названием экскурсии и временем комментирования." +
            "Все комментарии сортированы по времени." +
            "Ответ 200, если пользователь существует, иначе 404")
    @LogExecutionTime
    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentDto>> getAllUserComments(@PathVariable int id) {

        if (commentService.get(id).isPresent()) {
            return ResponseEntity.ok().body(commentService.getUserComment(id).stream()
                    .map(c -> {
                        CommentDto comm = Converter.getCommentDto(c);
                        comm.setExcursionDto(Converter.getExcursionDto(c.getExcursion()));
                        return comm;
                    }).toList());
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Доступна только администратору. " +
            "Удаляет комментарий по его id. Ответ 200, " +
            "если комментарий существует, иначе 404")
    @LogExecutionTime
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {

        if (commentService.get(id).isPresent()) {
            commentService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
