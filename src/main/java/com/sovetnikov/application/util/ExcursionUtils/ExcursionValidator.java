package com.sovetnikov.application.util.ExcursionUtils;

import com.sovetnikov.application.dto.ExcursionDto.BaseExcursionDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

//Валидирует введенные поля для экскурсий дополнительно
@Component
public class ExcursionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return BaseExcursionDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BaseExcursionDto excursion = (BaseExcursionDto) target;

        if (!Character.isUpperCase(excursion.getName().codePointAt(0)))
            errors.rejectValue("name", "", "Название экскурсии должно начинаться с заглавной буквы");
    }
}
