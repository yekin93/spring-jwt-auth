package com.example.demo.dto.request;

import java.time.Instant;

public record OrganizerSearchDto(
		Boolean verified,
		String q,
		Instant startDate,
		Instant endDate,
		String email
		) {
	
}