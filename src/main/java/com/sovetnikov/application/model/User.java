package com.sovetnikov.application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

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
    private String phoneNumber;

    @Column(name = "registered_at", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date registeredAt = new Date();

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(max = 256)
    private String password;

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
}
