package com.sovetnikov.application.controller.CommentController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.UserService;
import com.sovetnikov.application.util.Converter;
import com.sovetnikov.application.util.ErrorList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comment")
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

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getOne(@PathVariable int id) {

        if (commentService.get(id).isPresent()) {
            return ResponseEntity.ok().body(Converter.getCommentDto(commentService.getCommentWithUserAndExcursion(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<CommentDto>> getAllUserComments(@PathVariable int id) {
        return ResponseEntity.ok().body(commentService.getUserComment(id).stream()
                .map(Converter::getCommentDto).toList());
    }

    @GetMapping("/excursion/{id}")
    public ResponseEntity<List<CommentDto>> getAllExcursionComments(@PathVariable int id) {
        return ResponseEntity.ok().body(commentService.getExcursionComment(id).stream()
                .map(Converter::getCommentDto).toList());
    }

    @PostMapping("/excursion/{id}")
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

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id){

        if(commentService.get(id).isPresent()){
            commentService.delete(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
