package com.GeoPunch.demo.exception;

import com.GeoPunch.demo.dto.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

    return new ResponseEntity<>(
            ErrorResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .error("RESOURCE_NOT_FOUND")
                    .timestamp(LocalDateTime.now())
                    .build(),
            HttpStatus.NOT_FOUND
    );
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex) {

    return new ResponseEntity<>(
            ErrorResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .error("DUPLICATE_RESOURCE")
                    .timestamp(LocalDateTime.now())
                    .build(),
            HttpStatus.CONFLICT
    );
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {

    return new ResponseEntity<>(
            ErrorResponse.builder()
                    .success(false)
                    .message(ex.getMessage())
                    .error("BUSINESS_ERROR")
                    .timestamp(LocalDateTime.now())
                    .build(),
            HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {

    return new ResponseEntity<>(
            ErrorResponse.builder()
                    .success(false)
                    .message("Something went wrong")
                    .error("INTERNAL_SERVER_ERROR")
                    .timestamp(LocalDateTime.now())
                    .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
    );
  }
}
