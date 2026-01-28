package com.example.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, Object>> notFoundExceptionHandler(NotFoundException ex, HttpServletRequest req) {
		System.out.println("ERROR:" + ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("status", HttpStatus.NOT_FOUND.value(), "message", "test"));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleException(Exception ex, HttpServletRequest req){
		System.out.println("ERROR: " + ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("status", HttpStatus.INTERNAL_SERVER_ERROR.value(), "message", "Opss! something went wrong...")); 
	}
	
	@ExceptionHandler(DuplicateEntryException.class)
	public ResponseEntity<Map<String, Object>> duplicateEntryExceptionHandle(DuplicateEntryException ex, HttpServletRequest req) {
		System.out.println("Duplicate Error: " + ex.getStackTrace());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", HttpStatus.CONFLICT.value(), "message", ex.getMessage()));
	}
	
	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<Map<String, Object>> invalideRefreshTokenExceptionHandle(InvalidRefreshTokenException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("status", HttpStatus.UNAUTHORIZED.value(), "message", ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> validationExceptionHandle(MethodArgumentNotValidException ex){
		Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("status", HttpStatus.BAD_REQUEST.value(), "errors", errors));
	}
	
}
