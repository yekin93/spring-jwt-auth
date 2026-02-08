package com.example.demo.dto.response;

import java.time.Instant;

import com.example.demo.enums.OrganizerStatus;

public record OrganizerProfileResponseDto(
		Long id,
		String name,
		String email,
		String phone,
		String avatarUrl,
		String bio,
		Instant createdAt,
		UserResponseDto owner,
		OrganizerStatus status
		) {}