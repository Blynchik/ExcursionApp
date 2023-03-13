package com.sovetnikov.application.service;

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

    @Transactional
    public void create(Comment comment){
        commentRepository.save(comment);
    }

    public Optional<Comment> get(int id){
        return commentRepository.findById(id);
    }

    public List<Comment> getUserComment(int userId){
        List<Comment> list = commentRepository.getUserComments(userId);
        list.sort(Comparator.comparing(Comment::getCreatedAt).reversed());

        return list;
    }

    public List<Comment> getExcursionComment(int excursionId){
        List<Comment> list = commentRepository.getExcursionComments(excursionId);
        list.sort(Comparator.comparing(Comment::getCreatedAt).reversed());

        return list;
    }

    @Transactional
    public void delete(int id){
        commentRepository.deleteById(id);
    }

    public Optional<Comment> getCommentWithUserAndExcursion(int id){
        return commentRepository.getWithUserAndExcursion(id);
    }
}
