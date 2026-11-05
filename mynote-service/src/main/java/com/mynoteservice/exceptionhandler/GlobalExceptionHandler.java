package com.mynoteservice.exceptionhandler;

import com.mynotelibrary.exceptionmessage.ExceptionResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionResponseDTO> invalidInputExceptionHandler(InvalidInputException ex, HttpServletRequest request) {

        var exceptionResponse = ExceptionResponseDTO.
                builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> notFoundExceptionHandler(NotFoundException ex, HttpServletRequest request) {

        var exceptionResponse = ExceptionResponseDTO.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .error(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }


    public ResponseEntity<ExceptionResponseDTO> forbiddenExceptionHandler(Forbidden ex, HttpServletRequest request) {
        var forbiddenResponse = ExceptionResponseDTO.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .error(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(forbiddenResponse, HttpStatus.FORBIDDEN);
    }
}

