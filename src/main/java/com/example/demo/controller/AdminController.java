package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.example.demo.dto.request.UserSearchDto;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.ApiResponsePagination;
import com.example.demo.dto.response.OrganizerProfileResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.entity.User;
import com.example.demo.enums.OrganizerStatus;
import com.example.demo.mapper.OrganizerProfileMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.interfaces.IOrganizerProfileService;
import com.example.demo.service.interfaces.IUserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	private final IOrganizerProfileService organizerService;
	private final IUserService userService;
	
	public AdminController(IOrganizerProfileService organizerService, IUserService userService) {
		this.organizerService = organizerService;
		this.userService = userService;
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
	public ResponseEntity<ApiResponsePagination<List<OrganizerProfileResponseDto>>> searchByName(@ModelAttribute OrganizerSearchDto searchDto,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "0") int size,
			@RequestParam(defaultValue = "ASC") String direction,
			@RequestParam(defaultValue = "createdAt") String sortBy){
		
		Sort.Direction sortDirection = direction.equals("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
		PageRequest pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<OrganizerProfile> organizers = organizerService.search(searchDto, pageable);
		List<OrganizerProfileResponseDto> response = organizers.getContent().stream().map(OrganizerProfileMapper::OrganizerProfileToResponse).toList();
		return ResponseEntity.ok(ApiResponsePagination.success(response, organizers.getTotalElements(), organizers.getTotalPages(), organizers.isFirst(), organizers.isLast(),
				organizers.hasNext(), organizers.hasPrevious()));
	}
	
	@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
	@GetMapping("/user/search")
	public ResponseEntity<ApiResponsePagination<List<UserResponseDto>>> userSearch(@ModelAttribute UserSearchDto searchDto, 
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "ASC") String direction,
			@RequestParam(defaultValue = "createdAt") String sortBy){
		
		
		Sort.Direction sortDirection = direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
		
		PageRequest pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
		Page<User> users = userService.search(searchDto, pageable);
		List<UserResponseDto> response = users.getContent().stream().map(UserMapper::userToUserReponseDto).toList();
		return ResponseEntity.ok(ApiResponsePagination.success(response, users.getTotalElements(), users.getTotalPages(), users.isFirst(), users.isLast(), users.hasNext(), users.hasPrevious()));
	}
}
