package com.sovetnikov.application.dto.ExcursionDto;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.LikeDto;
import com.sovetnikov.application.dto.UserDto.UserDto;

import java.util.List;

public class ExcursionDto extends BaseExcursionDto {

    private List<UserDto> users;

    private List<CommentDto> comments;

    private List<LikeDto> likes;

    private long daysTillExcursion;

    public ExcursionDto(String name, int price) {
        super(name, price);
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<LikeDto> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeDto> likes) {
        this.likes = likes;
    }

    public long getTillExcursion() {
        return daysTillExcursion;
    }

    public void setTillExcursion(long tillExcursion) {
        this.daysTillExcursion = tillExcursion;
    }
}
