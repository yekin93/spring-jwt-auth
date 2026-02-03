package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record OrganizerProfileCreateDto(
		
		@NotBlank(message = "Please provide a name!")
		String displayName,
		
		@NotBlank(message = "Please provide a contact email!")
		@Email(message = "Please provide a valid email!")
		String email,
		
		@NotBlank(message = "Please provide a phone number!")
		String phone,
		
		String avatarUrl,
		String bio
		) {}