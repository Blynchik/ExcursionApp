package com.sovetnikov.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class UserDto {
    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 100, message = "Должно быть больше 2 и меньше 100 символов")
    private String name;

    @NotBlank(message = "Введите электронную почту")
    @Size(max = 100, message = "Должно быть меньше 100 символов")
    @Email(message = "Введите корректную электронную почту")
    private String email;

    @NotBlank(message = "Введите номер телефона")
    @Size(min = 10, max = 11, message = "Введите номер телефона без 8")
    @Pattern(regexp = "\\d{10}", message = "Номер телефона должен состоять из 10 цифр без 8")
    private String phoneNumber;

    private List<ExcursionDto> excursions;

    private List<CommentDto> comments;

    private List<LikeDto> like;

    public UserDto() {
    }

    public UserDto(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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