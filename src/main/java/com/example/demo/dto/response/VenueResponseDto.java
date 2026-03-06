package com.example.demo.dto.response;

import java.math.BigDecimal;

public record VenueResponseDto(
		
		String name,
		String street,
		String houseNumber,
		String unit,
		String countryCode,
		String city,
		String postalCode,
		BigDecimal lat,
		BigDecimal lng
		) {
	
}