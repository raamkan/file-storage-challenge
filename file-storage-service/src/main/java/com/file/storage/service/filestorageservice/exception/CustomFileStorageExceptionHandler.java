package com.file.storage.service.filestorageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class CustomFileStorageExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoFileRecordFoundException.class)
    public final ResponseEntity<ApiError> handleFileNotFoundException(NoFileRecordFoundException noFileRecordFoundException, WebRequest request){
        ApiError apiError = new ApiError(LocalDateTime.now(), noFileRecordFoundException.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ApiError> handleBadRequestException(BadRequestException badRequestException, WebRequest request){
        ApiError apiError = new ApiError(LocalDateTime.now(), badRequestException.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleAllExceptions(Exception exception, WebRequest request) {
        ApiError apiError = new ApiError(LocalDateTime.now(), exception.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
