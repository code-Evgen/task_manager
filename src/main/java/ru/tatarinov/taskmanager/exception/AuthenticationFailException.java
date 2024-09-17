package ru.tatarinov.taskmanager.exception;

public class AuthenticationFailException extends RuntimeException{
    public AuthenticationFailException(String message) {
        super(message);
    }
}
