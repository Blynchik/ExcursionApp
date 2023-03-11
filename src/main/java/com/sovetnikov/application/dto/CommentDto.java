package com.sovetnikov.application.dto;

import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public class CommentDto {

    @NotBlank(message = "Комментарий не должен быть пустым")
    @Size(max = 300, message = "Комментарий должен быть не более 300 знаков")
    private String message;

    private User user;

    private Excursion excursion;

    public CommentDto() {
    }

    public CommentDto(String message, User user, Excursion excursion) {
        this.message = message;
        this.user = user;
        this.excursion = excursion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Excursion getExcursion() {
        return excursion;
    }

    public void setExcursion(Excursion excursion) {
        this.excursion = excursion;
    }
}
