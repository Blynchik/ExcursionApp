package com.sovetnikov.application.dto.UserDto;

import com.sovetnikov.application.dto.CommentDto;
import com.sovetnikov.application.dto.ExcursionDto.ExcursionDto;
import com.sovetnikov.application.dto.LikeDto;

import java.util.List;

public class UserDto extends BaseUserDto {
    private List<ExcursionDto> excursions;

    private List<CommentDto> comments;

    private List<LikeDto> like;

    public UserDto() {
    }

    public UserDto(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber);
    }

    public List<ExcursionDto> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<ExcursionDto> excursions) {
        this.excursions = excursions;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<LikeDto> getLike() {
        return like;
    }

    public void setLike(List<LikeDto> like) {
        this.like = like;
    }
}