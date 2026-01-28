package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.AuthSignupDto;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEntryException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.interfaces.IUserService;

@Service
public class UserService implements IUserService {
	
	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User saveUser(AuthSignupDto authSignupDto) {
		User existsUser = userRepo.findByEmail(authSignupDto.getEmail()).orElse(null);
		if(existsUser != null) {
			throw new DuplicateEntryException("Already exists user with email: " + authSignupDto.getEmail());
		}
		User user = UserMapper.authSignupToUser(authSignupDto);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepo.save(user);
	}

	@Override
	public User findByUsername(String username) {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User not found by username: " + username));
	}

}
