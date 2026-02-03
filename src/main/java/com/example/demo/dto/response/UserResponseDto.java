package com.example.demo.dto.response;

import java.time.Instant;
import java.util.Set;

import com.example.demo.entity.OrganizerProfile;
import com.example.demo.entity.Role;

public record UserResponseDto(
		
		Long id,
		String username,
		String surname,
		String email,
		Set<Role> roles,
		OrganizerProfileResponseDto organizerProfile,
		Instant createdAt
		
		) {}