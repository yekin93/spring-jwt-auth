package com.example.demo.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class AuthSignupDto {

	@NotEmpty
	@NotNull
	private String username;
	
	@NotEmpty
	private String surname;
	
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String password;
	
	public AuthSignupDto() {}

	public AuthSignupDto(String username, String surname, String email,String password) {
		super();
		this.username = username;
		this.surname = surname;
		this.email = email;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
