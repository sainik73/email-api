package com.example.email.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is a global error handler for the application.
 * @author Kiran
 * @since 4/30/2021
 */
@Slf4j
@ControllerAdvice
public class EmailRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CustomEmailException.class)
    public ResponseEntity<Object> handleCustomEmailException(CustomEmailException ex, HttpServletRequest request) {
        log.debug("Entering handleCustomEmailException in EmailRestExceptionHandler class -> Status code: "+
                          HttpStatus.valueOf(ex.getHttpStatus()));
        CustomApiErrorResponse errorDto =
                new CustomApiErrorResponse.
                            CustomApiErrorResponseBuilder().
                            setHttpStatus(HttpStatus.valueOf(ex.getHttpStatus())).
                            setDebugMessage(ex.getMessage()).
                            setErrors(Collections.emptyList()).
                            build();
        return ResponseEntity.status(HttpStatus.valueOf(ex.getHttpStatus())).body(errorDto);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex)
    {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.debug("Entering handleRuntimeException in EmailRestExceptionHandler class -> Status code: "+
                          status);
        CustomApiErrorResponse errorDto =
                new CustomApiErrorResponse.
                            CustomApiErrorResponseBuilder().
                            setHttpStatus(status).
                            setErrors(Collections.emptyList()).
                            setDebugMessage(ex.getMessage()).
                            build();
        return ResponseEntity.status(status).body(errorDto);
    }

    /**
     * Handle MethodArgumentNotValidException
     * @param ex        object of MethodArgumentNotValidException
     * @param headers   object of HttpHeaders
     * @param status    object of HttpStatus
     * @param request   object of web request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        errors.addAll(ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList()));
        errors.addAll(ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> error.getObjectName() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList()));

        CustomApiErrorResponse errorDto =
                new CustomApiErrorResponse.
                            CustomApiErrorResponseBuilder().
                            setHttpStatus(HttpStatus.BAD_REQUEST).
                            setErrors(errors).
                            setDebugMessage(ex.getMessage()).
                            build();
        return new ResponseEntity<>(errorDto,HttpStatus.BAD_REQUEST);
    }

    /**
     * HttpMessageNotReadableException
     * @param ex        object of HttpMessageNotReadableException
     * @param headers   object of HttpHeaders
     * @param status    object of HttpStatus
     * @param request   object of web request
     * @return ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        CustomApiErrorResponse errorDto =
                new CustomApiErrorResponse.
                            CustomApiErrorResponseBuilder().
                            setHttpStatus(HttpStatus.BAD_REQUEST).
                            setErrors(errors).
                            setDebugMessage(ex.getMessage()).
                            build();
        return new ResponseEntity<>(errorDto,HttpStatus.BAD_REQUEST);

    }

}
