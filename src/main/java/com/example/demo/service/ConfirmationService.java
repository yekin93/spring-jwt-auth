package com.example.demo.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Confirmation;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.ConfirmationRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.interfaces.IConfirmationService;


@Service
public class ConfirmationService implements IConfirmationService {

	private final ConfirmationRepo confirmationRepo;
	private final UserRepo userRepo;
	
	public ConfirmationService(ConfirmationRepo confirmationRepo, UserRepo userRepo) {
		this.confirmationRepo = confirmationRepo;
		this.userRepo = userRepo;
	}
	
	@Transactional
	@Override
	public void confirmUser(String token) throws Exception {
		Confirmation confirm = confirmationRepo.findByToken(token).orElseThrow(() -> new NotFoundException("token not valid!"));
		if(confirm.getConfirmedAt() != null) {
			throw new Exception("User already confirmed!");
		}
		confirm.setConfirmedAt(Instant.now());
		User user = confirm.getUser();
		user.setConfirmed(true);
		confirmationRepo.save(confirm);
		userRepo.save(user);
	}

}
