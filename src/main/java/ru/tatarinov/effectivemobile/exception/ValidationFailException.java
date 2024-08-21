package ru.tatarinov.effectivemobile.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidationFailException extends RuntimeException {
    public ValidationFailException(String message) {
        super(message);
    }

}
