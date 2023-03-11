package com.sovetnikov.application.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class ExcursionDto {

    @NotBlank(message = "Введите название экскурсии")
    @Size(min = 2, max = 100, message = "Название должно быть больше 2 и меньше 100 символов")
    private String name;

    @Temporal(TemporalType.DATE)
    private LocalDate date;

    @Size(max = 300, message = "Описание должно быть не более 300 знаков")
    private String description;

    @Min(value = 0, message = "Не должно быть отрицательным")
    private int price;

    private List<UserDto> users;

    public ExcursionDto(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
