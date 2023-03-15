package com.sovetnikov.application.model;

import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    private final User user;

    public AuthUser(@NonNull User user) {
        super(user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
        this.user = user;
    }

    public int id() {
        return user.getId();
    }

    public User getUser() {
        return user;
    }
}
