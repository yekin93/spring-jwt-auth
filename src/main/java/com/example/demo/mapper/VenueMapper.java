package com.example.demo.mapper;

import com.example.demo.dto.request.VenueRequestDto;
import com.example.demo.entity.EventVenue;

public class VenueMapper {

	
	public static EventVenue venurRequestDtoToEventVenu(VenueRequestDto dto) {
		return new EventVenue(null, null, null, dto.name(), dto.addressLine1(), dto.addressLine2(), dto.city(), dto.postalCode(), null, null, null, null); 
	}
}
