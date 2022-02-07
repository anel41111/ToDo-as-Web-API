package ru.nikulin.test.todo.webapi.exception;

public class EntityDoesNotExistException extends TaskManagerExtension {
    public EntityDoesNotExistException(String message) {
        super(message);
    }
}
