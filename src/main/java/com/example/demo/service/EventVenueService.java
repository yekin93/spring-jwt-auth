package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.EventVenue;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.EventVenuRepo;
import com.example.demo.repository.OrganizerProfileRepo;
import com.example.demo.service.interfaces.IEventVenuService;

import jakarta.transaction.Transactional;

@Service
public class EventVenueService implements IEventVenuService {


	private final EventVenuRepo venueRepo;
	private final OrganizerProfileRepo organizerRepo;
	
	public EventVenueService(OrganizerProfileRepo organizerRepo, EventVenuRepo venueRepo) {
		this.organizerRepo = organizerRepo;
		this.venueRepo = venueRepo;
	}
	
	@Override
	@Transactional
	public EventVenue create(EventVenue venue, Long organizerProfileId) {
		OrganizerProfile organizer = organizerRepo.findById(organizerProfileId).orElseThrow(() -> new NotFoundException("Not found organizer..."));
		venue.setOrganizer(organizer);
		EventVenue createdVenue = venueRepo.save(venue);
		return createdVenue;
	}

}
