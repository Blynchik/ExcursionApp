package com.sovetnikov.application.util;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;

public class Converter {

    public static UserDto getUserDto(User user){
        return new UserDto(user.getName(), user.getEmail(), user.getPhoneNumber());
    }

    public static User getUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public static  ExcursionDto getExcursionDto(Excursion excursion){
        ExcursionDto excursionDto = new ExcursionDto(excursion.getName(), excursion.getPrice());
        excursionDto.setDate(excursion.getDate());
        excursionDto.setDescription(excursion.getDescription());
        return excursionDto;
    }

    public static Excursion getExcursion(ExcursionDto excursionDto){
        Excursion excursion = new Excursion();
        excursion.setName(excursionDto.getName());
        excursion.setPrice(excursionDto.getPrice());
        excursion.setDate(excursionDto.getDate());
        excursion.setDescription(excursionDto.getDescription());
        return excursion;
    }

    public static CommentDto getCommentDto(Comment comment){
        CommentDto commentDto =  new CommentDto(comment.getMessage(),
                Converter.getUserDto(comment.getUser()),
                Converter.getExcursionDto(comment.getExcursion()));

        commentDto.setCreatedAt(comment.getCreatedAt());

        return commentDto;
    }

    public static Comment getComment(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setMessage(commentDto.getMessage());
        comment.setUser(Converter.getUser(commentDto.getUserDto()));
        comment.setExcursion(Converter.getExcursion(commentDto.getExcursionDto()));
        return comment;
    }
}