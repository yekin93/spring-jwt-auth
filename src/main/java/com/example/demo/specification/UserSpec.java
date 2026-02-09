package com.example.demo.specification;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.User;

public class UserSpec {

	public static Specification<User> hasName(String name){
		return (r, q, c) -> {
			if(name == null || name.isEmpty()) {
				return null;
			}
			
			return c.like(c.lower(r.get("username")), "%" + name.toLowerCase() + "%");
		};
	}
	
	public static Specification<User> hasSurname(String surname) {
		return (r, q, c) -> {
			if(surname == null || surname.isEmpty()) {
				return null;
			}
			
			return c.like(c.lower(r.get("surname")), "%" + surname.toLowerCase() + "%");
		};
	}
	
}
