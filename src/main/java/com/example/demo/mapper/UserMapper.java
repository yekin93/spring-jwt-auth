package com.example.demo.mapper;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.dto.request.SignupDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;

public class UserMapper {

	public static User authSignupToUser(SignupDto dto) {
		return new User(null, dto.username(), dto.surname(), dto.email(), dto.password(), null, null, false, null, null);
	}
	
	public static UserResponseDto userToUserReponseDto(User user) {
		if(user == null) return null;
		
			Set<String> roles = new HashSet<>();
			for(Role role : user.getRoles()) {
				roles.add(role.getName());	
			}
		
			UserResponseDto dto = new UserResponseDto(user.getId(),
					user.getUsername(),
					user.getSurname(),
					user.getEmail(),
					roles,
					OrganizerProfileMapper.OrganizerProfileToResponse(user.getOrganizerProfile()),
					user.getCreatedAt());
		
		return dto;
	}
}
