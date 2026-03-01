package com.example.demo.service.interfaces;

import com.example.demo.entity.EventVenue;

public interface IEventVenuService {

	EventVenue create(EventVenue venue, Long organizerProfileId);
}
