package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.interfaces.IRoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
	
	private final IRoleService roleService;
	
	public RoleController(IRoleService roleService) {
		this.roleService = roleService;
	}

	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getAllRoles() {
		return ResponseEntity.status(HttpStatus.OK.value()).body(Map.of("status", HttpStatus.OK.value(), "roles", roleService.getAll()));
	}
	
}
