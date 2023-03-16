package com.sovetnikov.application.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;

//Содержит список ошибок
public class ErrorList {

    public static List<String> getList(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }
}