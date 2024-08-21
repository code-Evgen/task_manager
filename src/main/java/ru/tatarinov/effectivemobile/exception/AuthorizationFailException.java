package ru.tatarinov.effectivemobile.exception;

public class AuthorizationFailException  extends RuntimeException{
    public AuthorizationFailException(String message) {
        super(message);
    }
}
