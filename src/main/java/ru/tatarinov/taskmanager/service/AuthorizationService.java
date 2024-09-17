package ru.tatarinov.taskmanager.service;

public interface AuthorizationService {
    boolean ownerAuthorization(int taskId);
    boolean executorAuthorization(int taskId);
}
