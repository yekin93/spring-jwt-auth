package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custom.CustomUserDetails;
import com.example.demo.dto.request.VenueRequestDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.VenueResponseDto;
import com.example.demo.mapper.VenueMapper;
import com.example.demo.service.interfaces.IEventVenuService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/event")
public class EventController {
	
	private final IEventVenuService venueService;
	
	public EventController(IEventVenuService venueService) {
		this.venueService = venueService;
	}

	@PostMapping("/create-venue")
	public ResponseEntity<ApiResponse<VenueResponseDto>> addVenue(@Valid @RequestBody VenueRequestDto dto, @AuthenticationPrincipal CustomUserDetails user) {
		venueService.create(VenueMapper.venurRequestDtoToEventVenu(dto), user.getOrganizerId());
		return null;
	}
	
}
