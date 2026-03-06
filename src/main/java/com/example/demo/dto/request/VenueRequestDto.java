package com.example.demo.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VenueRequestDto(
		
		@NotBlank(message = "Name is required")
		@Size(max = 200, message = "Name must not exceed 200 characters")
		String name,
		
		@NotBlank(message = "Street is required")
		@Size(max = 255, message = "Street must not exceed 255 characters")
		String street,
		
		@NotBlank(message = "House number is required")
		@Size(min = 1, max = 50, message = "")
		String houseNumber,
		
		String unit,
		
		@NotBlank(message = "Country code is required")
		@Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be ISO 2-letter code")
		String countryCode,
		
		@NotBlank(message = "City is required")
		@Size(max = 120, message = "City must not exceed 120 charecters")
		String city,
		
		@NotBlank(message = "Postal code is required")
		@Size(max = 20, message = "Postal code must not exceed 20 characters")
		String postalCode,
		
		@DecimalMax(value = "90.0")
		@DecimalMin(value = "-90.0")
		BigDecimal lat,
		
		@DecimalMax(value = "180.0")
		@DecimalMin(value = "-180.0")
		BigDecimal lng
		
		) {
	
}