package kimosabe.api.controller;

import kimosabe.api.entity.ExceptionResponse;
import kimosabe.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MissingDatabaseEntryException.class)
    public ExceptionResponse exception(MissingDatabaseEntryException exception) {
        return new ExceptionResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRoleException.class)
    public ExceptionResponse exception(MissingRoleException exception) {
        return new ExceptionResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ExceptionResponse exception(UsernameTakenException exception) {
        return new ExceptionResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ExceptionResponse exception(IncorrectPasswordException exception) {
        return new ExceptionResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ExceptionResponse exception(BadRequestException exception) {
        return new ExceptionResponse(exception, HttpStatus.BAD_REQUEST);
    }
}
