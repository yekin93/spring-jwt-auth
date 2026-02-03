package com.example.demo.mapper;

import com.example.demo.dto.request.SignupDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.User;

public class UserMapper {

	public static User authSignupToUser(SignupDto dto) {
		return new User(null, dto.username(), dto.surname(), dto.email(), dto.password(), null, null, false, null, null);
	}
	
	public static UserResponseDto userToUserReponseDto(User user) {
		return user == null ? null : 
			new UserResponseDto(user.getId(),
					user.getUsername(),
					user.getSurname(),
					user.getEmail(),
					user.getRoles(),
					OrganizerProfileMapper.OrganizerProfileToResponse(user.getOrganizerProfile()),
					user.getCreatedAt());
	}
}
