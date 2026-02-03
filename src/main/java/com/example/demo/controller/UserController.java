package com.example.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custom.CustomUserDetails;
import com.example.demo.dto.request.OrganizerProfileCreateDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.OrganizerProfileResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.entity.User;
import com.example.demo.mapper.OrganizerProfileMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.interfaces.IOrganizerProfileService;
import com.example.demo.service.interfaces.IUserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private final IUserService userService;
	private final IOrganizerProfileService organizerService;
	
	public UserController(IUserService userService, IOrganizerProfileService organizerService) {
		this.userService = userService;
		this.organizerService = organizerService;
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponseDto>> me(@AuthenticationPrincipal CustomUserDetails auth) {
		User user = userService.findById(auth.getId());
		UserResponseDto userResponse = UserMapper.userToUserReponseDto(user);
		return ResponseEntity.ok(ApiResponse.success(userResponse));
	}
	
	@PostMapping("/apply-organizer-profile")
	public ResponseEntity<ApiResponse<OrganizerProfileResponseDto>> applyToOrganizerProfile(@Valid @RequestBody OrganizerProfileCreateDto dto, @AuthenticationPrincipal CustomUserDetails user) {
		OrganizerProfile profile = OrganizerProfileMapper.organizerProfileCreateDtoToOrganizerProfile(dto);
		OrganizerProfile createdProfile = organizerService.createOrganizerProfile(user.getId(), profile);
		OrganizerProfileResponseDto responseDto = OrganizerProfileMapper.OrganizerProfileToResponse(createdProfile);
		return ResponseEntity.ok(ApiResponse.created(responseDto));
	}
}
