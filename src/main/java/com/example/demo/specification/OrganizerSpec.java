package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.OrganizerProfile;
import com.example.demo.enums.OrganizerStatus;

public class OrganizerSpec {

	public static Specification<OrganizerProfile> hasName(String name){
		return (root, query, cb) -> {
			if(name == null || name.isEmpty()) {
				return null;
			}
			return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
		};
	}
	
	public static Specification<OrganizerProfile> hasStatus(OrganizerStatus status) {
		return (root, query, cb) -> {
			if(status == null) {
				return null;
			}
			return cb.equal(root.get("status"), status);
		};
	}
	
	public static Specification<OrganizerProfile> isApproved(){
		return (root, query, cb) -> {
			return cb.equal(root.get("status"), OrganizerStatus.APPROVED);
		};
	}
	
	public static Specification<OrganizerProfile> hasSlug(String slug) {
		return (r, q, c) -> {
			if(slug == null || slug.isEmpty()) {
				return c.disjunction();
			}
			return c.equal(r.get("slug"), slug);
		};
	}
	
	public static Specification<OrganizerProfile> isVerified(boolean verified){
		return (r, q, c) -> {
			return c.equal(r.get("verified"), verified);
		};
	}
}
