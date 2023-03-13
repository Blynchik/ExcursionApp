package com.sovetnikov.application.util;

import com.sovetnikov.application.dto.*;
import com.sovetnikov.application.dto.ExcursionDto.BaseExcursionDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto.BaseUserDto;
import com.sovetnikov.application.dto.UserDto.UserDto;
import com.sovetnikov.application.model.Comment;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.Like;
import com.sovetnikov.application.model.User;

public class Converter {

    public static UserDto getUserDto(User user) {
        return new UserDto(user.getName(), user.getEmail(), user.getPhoneNumber());
    }

    public static User getUser(BaseUserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }

    public static ExcursionDto getExcursionDto(Excursion excursion) {
        ExcursionDto excursionDto = new ExcursionDto(excursion.getName(), excursion.getPrice());
        excursionDto.setDate(excursion.getDate());
        excursionDto.setDescription(excursion.getDescription());
        excursionDto.setDaysTillExcursion(excursion.getDaysTillExcursion());
        return excursionDto;
    }

    public static Excursion getExcursion(BaseExcursionDto excursionDto) {
        Excursion excursion = new Excursion();
        excursion.setName(excursionDto.getName());
        excursion.setPrice(excursionDto.getPrice());
        excursion.setDate(excursionDto.getDate());
        excursion.setDescription(excursionDto.getDescription());
        return excursion;
    }

    public static CommentDto getCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto(comment.getMessage(),
                Converter.getUserDto(comment.getUser()),
                Converter.getExcursionDto(comment.getExcursion()));

        commentDto.setCreatedAt(comment.getCreatedAt());

        return commentDto;
    }

    public static Comment getComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setMessage(commentDto.getMessage());
        comment.setUser(Converter.getUser(commentDto.getUserDto()));
        comment.setExcursion(Converter.getExcursion(commentDto.getExcursionDto()));
        return comment;
    }

    public static LikeDto getLikeDto(Like like) {
        return new LikeDto(Converter.getUserDto(like.getUser()),
                Converter.getExcursionDto(like.getExcursion()));
    }

    public static Like getLike(LikeDto likeDto) {
        return new Like(Converter.getUser(likeDto.getUserDto()),
                Converter.getExcursion(likeDto.getExcursionDto()));
    }
}