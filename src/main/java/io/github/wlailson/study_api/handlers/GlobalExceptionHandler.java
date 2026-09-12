package io.github.wlailson.study_api.handlers;


import io.github.wlailson.study_api.service.exceptions.ConflictException;
import io.github.wlailson.study_api.service.exceptions.ForbiddenException;
import io.github.wlailson.study_api.service.exceptions.ResourceNotFoundException;
import io.github.wlailson.study_api.utils.ProblemDetailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

import static io.github.wlailson.study_api.utils.ProblemTypes.*;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        return ProblemDetailUtils.create(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                RESOURCE_NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex) {
        return ProblemDetailUtils.create(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex) {
        return ProblemDetailUtils.create(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex) {

        ProblemDetail problem = ProblemDetailUtils.create(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos são inválidos",
                "validation-error"
        );

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ProblemDetailUtils.create(HttpStatus.BAD_REQUEST, "Parâmetro inválido: " + ex.getName(), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        log.error("Erro inesperado no servidor", ex);
        return ProblemDetailUtils.create(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde",
                INTERNAL_SERVER_ERROR);
    }
}