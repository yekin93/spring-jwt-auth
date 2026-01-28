package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.custom.CustomUserDetails;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@GetMapping("/profile")
	public ResponseEntity<Map<String, Object>> getUserProfile(Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("user", user));
	}
}
