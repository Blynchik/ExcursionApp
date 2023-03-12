package com.sovetnikov.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "excursion_id", referencedColumnName = "id")
    private Excursion excursion;

    public Like() {
    }

    public Like(User user, Excursion excursion) {
        this.user = user;
        this.excursion = excursion;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Like like = (Like) o;
        return id == like.id && Objects.equals(user, like.user) && Objects.equals(excursion, like.excursion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, excursion);
    }
}
