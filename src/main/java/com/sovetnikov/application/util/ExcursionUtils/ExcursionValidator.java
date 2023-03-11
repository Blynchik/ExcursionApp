package com.sovetnikov.application.util.ExcursionUtils;

import com.sovetnikov.application.dto.ExcursionDto;
import com.sovetnikov.application.dto.UserDto;
import com.sovetnikov.application.service.ExcursionService;
import com.sovetnikov.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ExcursionValidator implements Validator {

    private final ExcursionService excursionService;

    @Autowired
    public ExcursionValidator(ExcursionService excursionService) {
        this.excursionService = excursionService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ExcursionDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
       ExcursionDto excursion = (ExcursionDto) target;

        if (!Character.isUpperCase(excursion.getName().codePointAt(0)))
            errors.rejectValue("name", "", "Название экскурсии должно начинаться с заглавной буквы");
    }
}
