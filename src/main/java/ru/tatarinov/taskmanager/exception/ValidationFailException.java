package ru.tatarinov.taskmanager.exception;

public class ValidationFailException extends RuntimeException {
    public ValidationFailException(String message) {
        super(message);
    }

}
