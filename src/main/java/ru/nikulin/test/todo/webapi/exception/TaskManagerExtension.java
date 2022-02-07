package ru.nikulin.test.todo.webapi.exception;

import org.hibernate.service.spi.ServiceException;

public class TaskManagerExtension extends ServiceException {

    public TaskManagerExtension(String message) {
        super(message);
    }
}
