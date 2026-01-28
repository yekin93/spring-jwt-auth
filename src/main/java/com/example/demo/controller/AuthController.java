package com.example.demo.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.request.AuthLoginDto;
import com.example.demo.dto.request.AuthSignupDto;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.service.interfaces.IRefreshTokenService;
import com.example.demo.service.interfaces.IUserService;
import com.example.demo.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	
	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;
	private final IUserService userService;
	private final IRefreshTokenService refreshTokenService;
	
	public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, IUserService userService, IRefreshTokenService refreshTokenService) {
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
		this.refreshTokenService = refreshTokenService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Map<String, Object>> signup(@RequestBody AuthSignupDto authSignupDto) {
		User created = userService.saveUser(authSignupDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", created.getId(), "username", created.getUsername()));
	}
	
	@GetMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody AuthLoginDto authLoginDto, HttpServletRequest req) {
		Authentication authentication = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authLoginDto.getEmail(), authLoginDto.getPassword()));
		
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String token = jwtUtil.generateToken(userDetails.getUsername());
		
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername(), req.getRemoteAddr(), req.getHeader("User-Agent"));
		
		ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken.getToken())
									.httpOnly(true)
									.secure(false)
									.sameSite("Strict")
									.path("/auth")
									.maxAge(Duration.ofDays(7))
									.build();
		
		return ResponseEntity.status(HttpStatus.OK)
				.header(HttpHeaders.SET_COOKIE, cookie.toString())
				.body(Map.of("token", token));
		
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<Map<String, String>> refreshAccessToken(@CookieValue(name = "refresh_token", required = true) String refreshToken) {
		return ResponseEntity.status(HttpStatus.OK.value()).body(Map.of("refresh_token", refreshToken));
	}
}
