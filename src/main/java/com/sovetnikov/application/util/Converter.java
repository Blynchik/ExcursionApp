package com.sovetnikov.application.util;

import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.model.Excursion;
import com.sovetnikov.application.model.User;

public class Converter {

    public static UserDto getUserDto(User user){
        return new UserDto(user.getName(), user.getEmail(), user.getPhoneNumber());
    }

    public static void getUser(User user, UserDto userDto){
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail().toLowerCase());
        user.setPhoneNumber(userDto.getPhoneNumber());
    }

    public static  ExcursionDto getExcursionDto(Excursion excursion){
        ExcursionDto excursionDto = new ExcursionDto(excursion.getName(), excursion.getPrice());
        excursionDto.setDate(excursion.getDate());
        excursionDto.setDescription(excursion.getDescription());
        return excursionDto;
    }

    public static void getExcursion(Excursion excursion, ExcursionDto excursionDto){
        excursion.setName(excursionDto.getName());
        excursion.setPrice(excursionDto.getPrice());
        excursion.setDate(excursionDto.getDate());
        excursion.setDescription(excursionDto.getDescription());
    }
}