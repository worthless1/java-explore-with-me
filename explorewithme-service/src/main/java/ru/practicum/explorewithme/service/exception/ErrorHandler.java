package ru.practicum.explorewithme.service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {


    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Throwable e) {
        log.error("Error code: {}, {}", HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return Map.of(
                "status", "INTERNAL SERVER ERROR",
                "message", e.getMessage()

        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException e) {
        log.error("Error code: {}, {}", HttpStatus.NOT_FOUND, e.getMessage());
        return Map.of(
                "status", "NOT FOUND",
                "reason", "Object has not found",
                "message", e.getMessage()

        );
    }

    @ExceptionHandler({ConstraintViolationException.class, ValidateException.class, NotAvailableException.class,
            DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleConstraintViolationException(RuntimeException e) {
        log.error("Error code: {}, {}", HttpStatus.CONFLICT, e.getMessage());
        return Map.of(
                "status", "CONFLICT",
                "reason", "Constraint Violation Exception",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class, ValidateDateException.class,
            MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValidException(final RuntimeException e) {
        log.error("Error code: {}, {}", HttpStatus.BAD_REQUEST, e.getMessage());
        return Map.of(
                "status", "BAD REQUEST",
                "reason", "Request isn't correct",
                "message", e.getMessage()
        );
    }

}