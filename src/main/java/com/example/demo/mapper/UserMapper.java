package com.example.demo.mapper;

import com.example.demo.dto.request.AuthSignupDto;
import com.example.demo.entity.User;

public class UserMapper {

	public static User authSignupToUser(AuthSignupDto dto) {
		return new User(null, dto.getUsername(), dto.getSurname(), dto.getEmail(), dto.getPassword(), null, null, null);
	}
}
