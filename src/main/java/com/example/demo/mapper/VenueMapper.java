package com.example.demo.mapper;

import com.example.demo.dto.request.VenueRequestDto;
import com.example.demo.dto.response.VenueResponseDto;
import com.example.demo.entity.EventVenue;

public class VenueMapper {

	
	public static EventVenue venueRequestDtoToEventVenu(VenueRequestDto dto) {
		return new EventVenue(null, dto.name(), dto.street(), dto.houseNumber(), dto.unit(), dto.countryCode(), dto.city(), dto.postalCode(), dto.lat(), dto.lng(), null, null);
	}
	
	public static VenueResponseDto venueResponse(EventVenue venue) {
		return new VenueResponseDto(venue.getName(), venue.getStreet(), venue.getHouseNumber(), venue.getUnit(), venue.getCountryCode(), venue.getCity(), venue.getPostalCode(), venue.getLat()	, venue.getLng());
	}
}
