package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Role;
import com.example.demo.service.interfaces.IRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
	
	private final IRoleService roleService;
	
	public RoleController(IRoleService roleService) {
		this.roleService = roleService;
	}

	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<ApiResponse<List<Role>>> getAllRoles() {
		List<Role> roles = roleService.getAll();
		return ResponseEntity.ok(ApiResponse.success(roles));
	}
	
}
