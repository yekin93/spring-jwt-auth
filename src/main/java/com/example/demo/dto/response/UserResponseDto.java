package com.example.demo.dto.response;

import java.time.Instant;

public record UserResponseDto(
		
		Long id,
		String username,
		String surname,
		String email,
		Instant createdAt
		
		) {}