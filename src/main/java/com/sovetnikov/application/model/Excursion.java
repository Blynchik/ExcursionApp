package com.sovetnikov.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "excursion")
public class Excursion {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Введите название экскурсии")
    @Size(min = 2, max = 100, message = "Название должно быть больше 2 и меньше 100 символов")
    private String name;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    //время проведения экскурсии относительно
    //т.к. проходит всегда по местному времени
    private LocalDate date;

    @Column(name = "description")
    @Size(max = 300, message = "Описание должно быть не более 300 знаков")
    private String description;

    @Column(name = "price")
    @Min(value = 0, message = "Не должно быть отрицательным")
    //стоимость в целых числах, а не в вещественных
    //например, если стоимость указана в центах/копейках
    //удобно переводить в другую валюту, переводить в рубли/доллары
    private int price;

    @ManyToMany(mappedBy = "excursions")
    private List<User> users;

    @OneToMany(mappedBy = "excursion")
    private List<Comment> comments;

    @OneToMany(mappedBy = "excursion")
    private List<Like> likes;

    @Transient
    private int likesAmount;

    @Transient
    private long daysTillExcursion;

    public Excursion() {
    }

    public Excursion(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public long getDaysTillExcursion() {
        return daysTillExcursion;
    }

    public void setDaysTillExcursion(long tillExcursion) {
        this.daysTillExcursion = tillExcursion;
    }

    public int getLikesAmount() {
        return likesAmount;
    }

    public void setLikesAmount(int likesAmount) {
        this.likesAmount = likesAmount;
    }
}
