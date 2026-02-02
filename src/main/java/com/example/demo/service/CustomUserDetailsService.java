package com.example.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.demo.custom.CustomUserDetails;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepo userRepo;
	
	public CustomUserDetailsService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}
	
	@Override
	public CustomUserDetails loadUserByUsername(String email) {
		User user = userRepo.findByEmailAndConfirmedTrue(email).orElseThrow(() -> new NotFoundException("User not found: " + email));
		CustomUserDetails cud = new CustomUserDetails(user);
		return cud;
		
	}
}
