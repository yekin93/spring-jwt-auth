package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupDto(
		@NotBlank(message = "Please provide an username")
		String username,
		
		@NotBlank(message = "Please provide a surname")
		String surname,
		
		@NotBlank(message = "Please provide an email")
		@Email(message = "Please provide a valid email")
		String email,
		
		@NotBlank(message = "Please provide a password")
		@Size(min = 8, message = "You cannot entry password lower then 8 character")
		String password
		) {}