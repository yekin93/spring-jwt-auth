package com.example.demo.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.interfaces.IOrganizerProfileService;



@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {
	
	private final IOrganizerProfileService organizerService;
	
	public OrganizerController(IOrganizerProfileService organizerService) {
		this.organizerService = organizerService;
	}
}
