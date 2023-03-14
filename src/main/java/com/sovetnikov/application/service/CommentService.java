package com.sovetnikov.application.service;

import com.sovetnikov.application.aspect.LogExecutionTime;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
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

    @LogExecutionTime
    @Transactional
    public void create(Comment comment){
        commentRepository.save(comment);
    }

    @LogExecutionTime
    public Optional<Comment> get(int id){
        return commentRepository.findById(id);
    }

    @LogExecutionTime
    public List<Comment> getUserComment(int userId){
        List<Comment> list = commentRepository.getUserComments(userId);
        list.sort(Comparator.comparing(Comment::getCreatedAt).reversed());

        return list;
    }

    @LogExecutionTime
    public List<Comment> getExcursionComment(int excursionId){
        List<Comment> list = commentRepository.getExcursionComments(excursionId);
        list.sort(Comparator.comparing(Comment::getCreatedAt).reversed());

        return list;
    }

    @LogExecutionTime
    @Transactional
    public void delete(int id){
        commentRepository.deleteById(id);
    }

    @LogExecutionTime
    public Optional<Comment> getCommentWithUserAndExcursion(int id){
        return commentRepository.getWithUserAndExcursion(id);
    }
}
