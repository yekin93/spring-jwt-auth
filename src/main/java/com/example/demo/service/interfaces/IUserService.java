package com.example.demo.service.interfaces;

import com.example.demo.dto.request.SignupDto;
import com.example.demo.entity.User;

public interface IUserService {
	User saveUser(SignupDto authSignupDto);
	User findByUsername(String username);
}
