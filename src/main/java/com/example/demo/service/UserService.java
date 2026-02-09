package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.request.SignupDto;
import com.example.demo.dto.request.UserSearchDto;
import com.example.demo.entity.Confirmation;
import com.example.demo.entity.User;
import com.example.demo.exception.DuplicateEntryException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.ConfirmationRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.interfaces.IUserService;
import com.example.demo.specification.UserSpec;

@Service
public class UserService implements IUserService {
	
	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final ConfirmationRepo confirmationRepo;
	
	public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, ConfirmationRepo confirmationRepo) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.confirmationRepo = confirmationRepo;
	}

	@Override
	@Transactional
	public User saveUser(SignupDto authSignupDto) {
		User existsUser = userRepo.findByEmail(authSignupDto.email()).orElse(null);
		if(existsUser != null) {
			throw new DuplicateEntryException("Already exists user with email: " + authSignupDto.email());
		}
		User user = UserMapper.authSignupToUser(authSignupDto);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User createdUser = userRepo.save(user);
		String confirmToken = UUID.randomUUID().toString().substring(0, 8);
		Confirmation confirm = new Confirmation(null, confirmToken, createdUser, null, null);
		confirmationRepo.save(confirm);
		return createdUser;
	}

	@Override
	public User findByUsername(String username) {
		return userRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User not found by username: " + username));
	}

	@Override
	public User findById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
	}

	@Override
	public Page<User> search(UserSearchDto searchDto, Pageable pageable) {
		Specification<User> spec = Specification.where(UserSpec.hasName(searchDto.username()))
				.or(UserSpec.hasSurname(searchDto.surname()));
		return userRepo.findAll(spec, pageable);
	}

}
