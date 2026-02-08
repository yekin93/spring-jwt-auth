package com.example.demo.dto.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
			int status,
			T data,
			String message,
			Instant time
		){
	
	public static <T> ApiResponse<T> success(T data){
		return new ApiResponse<>(HttpStatus.OK.value(), data, "success", Instant.now());
	}
	
	public static <T> ApiResponse<T> created(T data){
		return new ApiResponse<>(HttpStatus.CREATED.value(), data, "created", Instant.now());
	}
}