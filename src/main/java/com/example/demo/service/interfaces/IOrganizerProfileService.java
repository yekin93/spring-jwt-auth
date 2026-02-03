package com.example.demo.service.interfaces;


import java.util.List;

import com.example.demo.dto.projection.IOrganizerProfileAdminView;
import com.example.demo.dto.request.OrganizerSearchDto;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.enums.OrganizerStatus;

public interface IOrganizerProfileService {

	OrganizerProfile createOrganizerProfile(Long userId, OrganizerProfile profile);
	List<IOrganizerProfileAdminView> getOrganizerProfileListByStatus(OrganizerStatus status);
	void verifyOrganizerProfile(Long approverId, String organizerSlug);
	List<OrganizerProfile> searchByName(String name);
	List<OrganizerProfile> search(OrganizerSearchDto saerch);
}
