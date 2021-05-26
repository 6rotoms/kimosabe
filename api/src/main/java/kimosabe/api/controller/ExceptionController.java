package kimosabe.api.controller;

import kimosabe.api.entity.ExceptionResponse;
import kimosabe.api.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@SuppressWarnings("unused")
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
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse exception(IncorrectPasswordException exception) {
        return new ExceptionResponse(exception, HttpStatus.UNAUTHORIZED);
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

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exception(MethodArgumentTypeMismatchException ex) {
        String errorMessage = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        return new ExceptionResponse(new BadRequestException(errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse exception(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return new ExceptionResponse(new BadRequestException("mandatory fields are missing from request object"), HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse exception(AccessDeniedException ex) {
        return new ExceptionResponse(ex, HttpStatus.FORBIDDEN);
    }
}
