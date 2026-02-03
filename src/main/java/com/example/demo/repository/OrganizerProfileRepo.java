package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.projection.IOrganizerProfileAdminView;
import com.example.demo.entity.OrganizerProfile;
import com.example.demo.enums.OrganizerStatus;

public interface OrganizerProfileRepo extends JpaRepository<OrganizerProfile, Long>, JpaSpecificationExecutor<OrganizerProfile> {

	boolean existsByUserId(Long userId);
	
	@Query("""
			select 
			p.id as id,
			p.name as name,
			p.email as email,
			u.email as ownerEmail,
			p.status profileStatus,
			p.slug slug
			from OrganizerProfile p
			join p.user u
			WHERE p.status = :status
			""")
	List<IOrganizerProfileAdminView> findAllByStatus(@Param("status") OrganizerStatus status);
	
	
	Optional<OrganizerProfile> findBySlug(String slug);
	
}
