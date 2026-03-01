package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VenueRequestDto(
		
		@NotBlank(message = "Please provide a name")
		@Size(max = 200, message = "You cannot entry the name bigger then 200 character")
		String name,
		
		@NotBlank(message = "Please provide a addess")
		@Size(max = 200, message = "You cannot entry the address bigger then 200 chatacter")
		String addressLine1,
		
		String addressLine2,
		
		@NotBlank(message = "Please provide a city")
		@Size(max = 120, message = "You cannot entry the city bigger then 120 character")
		String city,
		
		@NotBlank(message = "please provide a postal code")
		String postalCode
		
		) {
	
}