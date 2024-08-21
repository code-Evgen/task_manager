package ru.tatarinov.effectivemobile.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.tatarinov.effectivemobile.exception.ValidationFailException;

import java.util.List;

public class BindingResultValidation {

    public static void bindingResultCheck(BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors)
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            throw new ValidationFailException(errorMessage.toString());
        }
    }
}
