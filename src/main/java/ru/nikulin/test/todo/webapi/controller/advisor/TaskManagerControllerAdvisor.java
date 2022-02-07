package ru.nikulin.test.todo.webapi.controller.advisor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.nikulin.test.todo.webapi.exception.EntityDoesNotExistException;

@ControllerAdvice
public class TaskManagerControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler({EntityDoesNotExistException.class})
    public ResponseEntity<Object> handleEntityNotFoundExceptions(RuntimeException e,
                                                                 WebRequest request) {
        return handleExceptionInternal(e, e.getMessage(),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value
            = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException e, WebRequest request) {
        String bodyOfResponse = e.getMessage();
        return handleExceptionInternal(e, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
