package com.sovetnikov.application.controller.CommentController;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.service.CommentService;
import com.sovetnikov.application.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/comment")
public class AdminCommentController {

    private final CommentService commentService;

    @Autowired
    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getOne(@PathVariable int id){

        if(commentService.get(id).isPresent()){
            return ResponseEntity.ok().body(Converter.getCommentDto(commentService.getCommentWithUserAndExcursion(id).get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
