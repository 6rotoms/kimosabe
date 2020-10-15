package kimosabe.api.controller;

import kimosabe.api.entity.ExceptionResponse;
import kimosabe.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(MissingDatabaseEntryException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse exception(MissingDatabaseEntryException exception) {
        return new ExceptionResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingRoleException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse exception(MissingRoleException exception) {
        return new ExceptionResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse exception(EntityExistsException exception) {
        return new ExceptionResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse exception(IncorrectPasswordException exception) {
        return new ExceptionResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exception(BadRequestException exception) {
        return new ExceptionResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exception(MissingServletRequestParameterException ex) {
        String errorMessage = ex.getParameterName() + " query parameter is missing";
        return new ExceptionResponse(new BadRequestException(errorMessage), HttpStatus.BAD_REQUEST);
    }
}
