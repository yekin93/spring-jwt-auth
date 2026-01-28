package com.example.demo.service.interfaces;

import com.example.demo.dto.request.AuthSignupDto;
import com.example.demo.entity.User;

public interface IUserService {
	User saveUser(AuthSignupDto authSignupDto);
	User findByUsername(String username);
}
