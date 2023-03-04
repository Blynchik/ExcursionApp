package com.sovetnikov.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDtoWithPassword extends UserDto{
    @NotBlank
    @Size(max = 256)
    private String password;

    public UserDtoWithPassword() {
    }

    public UserDtoWithPassword(String name, String email, String phoneNumber, String password) {
        super(name, email, phoneNumber);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
