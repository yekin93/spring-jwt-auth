package com.example.demo.dto.response;

import java.time.Instant;

import org.springframework.http.HttpStatus;

public record ApiResponsePagination<T>(
		int status,
		T data,
		String message,
		Instant time,
		long totalElements,
		long totalPages,
		boolean isFirst,
		boolean isLast,
		boolean hasNext,
		boolean hasPreviouse
		
		){
	
	public static <T> ApiResponsePagination<T> success(T data, long totalElements, long totalPages, boolean isFirst, boolean isLast, boolean hasNext, boolean hasPreviouse) {
		return new ApiResponsePagination<>(HttpStatus.OK.value(),
				data,
				"success",
				Instant.now(),
				totalElements,
				totalPages,
				isFirst,
				isLast,
				hasNext,
				hasPreviouse);
	}
}