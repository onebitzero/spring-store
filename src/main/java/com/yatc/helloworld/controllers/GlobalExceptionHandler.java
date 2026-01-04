package com.yatc.helloworld.controllers;

import java.util.HashMap;
import java.util.Map;

import com.yatc.helloworld.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException() {
        return ResponseEntity
                .badRequest()
                .body(new ErrorDto("Invalid request body"));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(
			MethodArgumentNotValidException exception) {
		Map<String, String> errors = new HashMap<>();

		exception.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.badRequest().body(errors);
	}
}
