package com.example.demo.mapper;

import com.example.demo.dto.request.OrganizerProfileCreateDto;
import com.example.demo.dto.response.OrganizerProfileResponseDto;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.enums.OrganizerStatus;

public class OrganizerProfileMapper {

	public static OrganizerProfile organizerProfileCreateDtoToOrganizerProfile(OrganizerProfileCreateDto dto) {
		return new OrganizerProfile(null, dto.displayName(), OrganizerStatus.PENDING, null, null, dto.email(), dto.phone(), dto.bio(), dto.avatarUrl(),
				null, null, false, null, null);
	}
	
	public static OrganizerProfileResponseDto OrganizerProfileToResponse(OrganizerProfile profile) {
		return profile == null ? null : new OrganizerProfileResponseDto(
					profile.getId(),
					profile.getName(),
					profile.getEmail(),
					profile.getPhone(),
					profile.getAvatarUrl(),
					profile.getBio(),
					profile.getCreatedAt(), 
					null,//UserMapper.userToUserReponseDto(profile.getUser())
					profile.getStatus()
				);
	}
}
