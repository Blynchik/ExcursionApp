package com.sovetnikov.application.model;

import org.springframework.lang.NonNull;

import java.util.Collections;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public AuthUser(@NonNull User user){
        super(user.getEmail(), user.getPassword(), Collections.emptyList());
        this.user = user;
    }

    public int id() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
