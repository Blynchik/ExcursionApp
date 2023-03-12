package com.sovetnikov.application.controller.CommentController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.model.AuthUser;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.service.ExcursionService;
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
@RequestMapping("/api/user/comment")
public class UserCommentController {

    private final CommentService commentService;
    private final ExcursionService excursionService;
    private final UserService userService;

    @Autowired
    public UserCommentController(CommentService commentService,
                                 ExcursionService excursionService,
                                 UserService userService) {
        this.commentService = commentService;
        this.excursionService = excursionService;
        this.userService = userService;
    }

    @GetMapping("/own")
    public ResponseEntity<List<CommentDto>> getAllUserComments(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(commentService.getUserComment(authUser.id()).stream()
                .map(Converter::getCommentDto).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable int id) {

        if (commentService.get(id).isPresent()) {

            if (commentService.get(id).get().getUser().getId() == authUser.id()) {
                commentService.delete(id);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
