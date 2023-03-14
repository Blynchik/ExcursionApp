package com.sovetnikov.application.controller.CommentController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
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
    private final ExcursionService excursionService;
    private final UserService userService;

    @Autowired
    public AdminCommentController(CommentService commentService,
                                  ExcursionService excursionService,
                                  UserService userService) {
        this.commentService = commentService;
        this.excursionService = excursionService;
        this.userService = userService;
    }

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

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {

        if (commentService.get(id).isPresent()) {
            commentService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
