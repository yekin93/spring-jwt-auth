package com.example.demo.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, Object>> notFoundExceptionHandler(NotFoundException ex, HttpServletRequest request) {
		log.error("Unhandled exception on path: {}", request.getRequestURI(), ex);
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
		log.warn("Duplicate Entry {}", ex);
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
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<Map<String, Object>> accessDeniedExceptionHandle(AuthorizationDeniedException ex, HttpServletRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth != null ? auth.getName() : "anonymous";
		log.warn("Access denied - user: {}, path: {}, authorities: {}", 
				email, 
				request.getRequestURI(),
				auth != null ? auth.getAuthorities() : "none");
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("status", HttpStatus.FORBIDDEN.value(), "message", "You don`t have permission to access"));
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Map<String, Object>> handleDuplicateEntry(DataIntegrityViolationException ex, WebRequest request) {
		String message = "Already exists!";
		String detailedMessage = ex.getMostSpecificCause().getMessage();
		if (detailedMessage.contains("UKefgbrv6ndqp03sf29wb7wghvi") || detailedMessage.contains("organizer_profile")) {
			message = "Already exists";
			}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("status", HttpStatus.CONFLICT.value(), "message", message, "time", Instant.now(), "req.desc", request.getDescription(false)));
	}
}
