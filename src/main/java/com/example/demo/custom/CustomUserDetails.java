package com.example.demo.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.User;

public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private User user;
	
	public CustomUserDetails(User user) {
		this.user = user;
	}
	
	public Long getId() {
		return user.getId();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList();
	}

	@Override
	public @Nullable String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

}
