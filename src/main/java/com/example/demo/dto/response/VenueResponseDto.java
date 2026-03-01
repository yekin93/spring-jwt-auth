package com.example.demo.dto.response;

import java.math.BigDecimal;

public record VenueResponseDto(
		
		String name,
		String addressLine1,
		String addressLine2,
		String city,
		String postalCode,
		BigDecimal lat,
		BigDecimal lng
		) {
	
}