package com.example.demo.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name="refresh_token")
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String token;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	@Column(name = "expiry_date")
	private Instant expiryDate;
	
	@Column(name = "created_at")
	private Instant createdAt;
	
	@Column(name = "ip_address")
	private String ipAddress;
	
	@Column(name = "user_agent")
	private String userAgent;
	
	private boolean revoked;
	
	@PrePersist
	public void prePersist() {
		this.revoked = false;
		if(this.createdAt == null) this.createdAt = Instant.now();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Instant getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public boolean isRevoked() {
		return revoked;
	}
	public void setRevoked(boolean revoked) {
		this.revoked = revoked;
	}
}
