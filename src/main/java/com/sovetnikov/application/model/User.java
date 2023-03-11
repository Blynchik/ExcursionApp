package com.sovetnikov.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    @NotBlank(message = "Введите имя")
    @Size(min = 2, max = 100, message = "Должно быть больше 2 и меньше 100 символов")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Введите электронную почту")
    @Size(max = 100, message = "Должно бытьменьше 100 символов")
    @Email(message = "Введите корректную электронную почту")
    private String email;

    @Column(name = "phone_number")
    @NotBlank(message = "Введите номер телефона")
    @Size(min = 10, max = 11, message = "Введите номер телефона без 8")
    @Pattern(regexp = "\\d{10}", message = "Номер телефона должен состоять из 10 цифр без 8")
    private String phoneNumber;

    @Column(name = "registered_at", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredAt = new Date();

    @Column(name = "password", nullable = false)
    @NotBlank(message = "Введите пароль")
    @Size(min = 1, max = 256, message = "Пароль должен быть меньше 256 знаков")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany()
    @JoinTable(
            name = "users_excursion",
            joinColumns = @JoinColumn(name = "users_id"),
            inverseJoinColumns = @JoinColumn(name = "excursion_id")
    )
    private List<Excursion> excursions;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    public User() {
    }

    public User(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
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

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Excursion> getExcursions() {
        return excursions;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
