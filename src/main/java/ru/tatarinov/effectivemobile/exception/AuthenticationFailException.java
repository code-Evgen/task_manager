package ru.tatarinov.effectivemobile.exception;

public class AuthenticationFailException extends RuntimeException{
    public AuthenticationFailException(String message) {
        super(message);
    }
}
