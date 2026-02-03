package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custom.CustomUserDetails;
import com.example.demo.dto.projection.IOrganizerProfileAdminView;
import com.example.demo.dto.request.OrganizerSearchDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.OrganizerProfileResponseDto;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.enums.OrganizerStatus;
import com.example.demo.mapper.OrganizerProfileMapper;
import com.example.demo.service.interfaces.IOrganizerProfileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	private final IOrganizerProfileService organizerService;
	
	public AdminController(IOrganizerProfileService organizerService) {
		this.organizerService = organizerService;
	}
	

	@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
	@GetMapping("/organizers")
	public ResponseEntity<ApiResponse<List<IOrganizerProfileAdminView>>> getPendingOrganizerProfileList(@RequestParam OrganizerStatus status) {
		List<IOrganizerProfileAdminView> profiles = organizerService.getOrganizerProfileListByStatus(status);
		return ResponseEntity.ok(ApiResponse.success(profiles));
	}
	
	@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
	@PatchMapping("/organizer/{organizerSlug}/verify")
	public ResponseEntity<ApiResponse<String>> verifyOrganizerProfile(@PathVariable String organizerSlug, @AuthenticationPrincipal CustomUserDetails auth) {
		organizerService.verifyOrganizerProfile(auth.getId(), organizerSlug);
		return ResponseEntity.ok(ApiResponse.success("Organizer approved successfully..."));
	}
	
	@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
	@GetMapping("/organizer/search")
	public ResponseEntity<ApiResponse<List<OrganizerProfileResponseDto>>> searchByName(@ModelAttribute OrganizerSearchDto searchDto){
		List<OrganizerProfile> organizers = organizerService.search(searchDto);
		List<OrganizerProfileResponseDto> response = organizers.stream().map(OrganizerProfileMapper::OrganizerProfileToResponse).toList();
		return ResponseEntity.ok(ApiResponse.success(response));
	}
}
