package com.sovetnikov.application.util;

import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.dto.UserDtoWithPassword;
import com.sovetnikov.application.model.User;

public class Converter {

    public static UserDto getUserDto(User user){
        return new UserDto(user.getName(), user.getEmail(), user.getPhoneNumber());
    }

    public static User getUser(User user, UserDtoWithPassword userDto){
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        return user;
    }
}