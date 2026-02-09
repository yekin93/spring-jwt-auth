package com.example.demo.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.projection.IOrganizerProfileAdminView;
import com.example.demo.dto.request.OrganizerSearchDto;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.entity.User;
import com.example.demo.enums.OrganizerStatus;
import com.example.demo.exception.DuplicateEntryException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.OrganizerProfileRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.interfaces.IOrganizerProfileService;
import com.example.demo.specification.OrganizerSpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrganizerProfileService implements IOrganizerProfileService {
	
	private final OrganizerProfileRepo organizerRepo;
	private final UserRepo userRepo;
	
	public OrganizerProfileService(OrganizerProfileRepo organizerRepo, UserRepo userRepo) {
		this.organizerRepo = organizerRepo;
		this.userRepo = userRepo;
	}

	@Override
	@Transactional
	public OrganizerProfile createOrganizerProfile(Long userId, OrganizerProfile profile) {
		User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
		profile.setUser(user);
		profile.setSlug(UUID.randomUUID().toString());
		OrganizerProfile createdProfile = organizerRepo.save(profile);
		return createdProfile;
	}

	@Override
	public List<IOrganizerProfileAdminView> getOrganizerProfileListByStatus(OrganizerStatus status) {
		return organizerRepo.findAllByStatus(status);
	}

	@Override
	@Transactional
	public void verifyOrganizerProfile(Long apprvoverId, String organizerSlug) {
		User approver = userRepo.findById(apprvoverId).orElseThrow(() -> new NotFoundException("Approver not found..."));
		OrganizerProfile organizer = organizerRepo.findBySlug(organizerSlug).orElseThrow(() -> new NotFoundException("Organizer not foun..."));
		
		if(organizer.isVerified()) {
			throw new DuplicateEntryException("Organizer already approved");
		}
		
		organizer.setVerified(true);
		organizer.setStatus(OrganizerStatus.APPROVED);
		organizer.setVerifiedBy(approver);
		organizer.setVerifiedAt(Instant.now());
		organizerRepo.save(organizer);
		log.info("{} organizer profile is approved by {}", organizer.getName(), approver.getUsername() + " " + approver.getSurname());
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrganizerProfile> searchByName(String name) {
		Specification<OrganizerProfile> spec = OrganizerSpec.hasName(name);
		return organizerRepo.findAll(spec);
	}

	@Override
	public Page<OrganizerProfile> search(OrganizerSearchDto search, Pageable pageable) {
		Specification<OrganizerProfile> specs = Specification
				.where(OrganizerSpec.hasName(search.q()))
				.and(OrganizerSpec.isVerified(search.verified()))
				.and(OrganizerSpec.createdBetween(search.startDate(), search.endDate()))
				.and(OrganizerSpec.hasEmail(search.email()));
		return organizerRepo.findAll(specs, pageable);
	}

}
