package com.example.demo.mapper;

import com.example.demo.dto.request.SignupDto;
import com.example.demo.entity.User;

public class UserMapper {

	public static User authSignupToUser(SignupDto dto) {
		return new User(null, dto.username(), dto.surname(), dto.email(), dto.password(), null, null, null);
	}
}
