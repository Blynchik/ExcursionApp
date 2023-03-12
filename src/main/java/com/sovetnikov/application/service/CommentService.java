package com.sovetnikov.application.service;

import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void create(Comment comment){
        commentRepository.save(comment);
    }

    public Optional<Comment> get(int id){
        return commentRepository.findById(id);
    }

    public List<Comment> getUserComment(int userId){
        return commentRepository.getUserComments(userId);
    }

    public List<Comment> getExcursionComment(int excursionId){
        return  commentRepository.getExcursionComments(excursionId);
    }

    @Transactional
    public void delete(int id){
        commentRepository.deleteById(id);
    }

    public Optional<Comment> getCommentWithUserAndExcursion(int id){
        return commentRepository.getWithUserAndExcursion(id);
    }
}
