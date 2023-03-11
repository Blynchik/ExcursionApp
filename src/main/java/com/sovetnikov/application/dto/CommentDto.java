package com.sovetnikov.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private UserDto userDto;

    private ExcursionDto excursionDto;

    public CommentDto() {
    }

    public CommentDto(String message, UserDto userDto, ExcursionDto excursionDto) {
        this.message = message;
        this.userDto = userDto;
        this.excursionDto = excursionDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public ExcursionDto getExcursionDto() {
        return excursionDto;
    }

    public void setExcursionDto(ExcursionDto excursionDto) {
        this.excursionDto = excursionDto;
    }
}
