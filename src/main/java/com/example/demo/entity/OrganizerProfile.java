package com.example.demo.entity;

import java.time.Instant;

import com.example.demo.enums.OrganizerStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "verifiedBy"})
@Entity
@Table(name = "organizer_profile")
public class OrganizerProfile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrganizerStatus status;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	
	@Column(name = "slug", nullable = false, unique = true)
	private String slug;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "phone", nullable = true, unique = false)
	private String phone;
	
	@Column(name = "bio", nullable = true, unique = false)
	private String bio;
	
	@Column(name = "avatar_url", nullable = true, unique = false)
	private String avatarUrl;
	
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@Column(name = "verified", nullable = false)
	private boolean verified;
	
	@Column(name = "verified_at", updatable = true)
	private Instant verifiedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verified_user_id")
	private User verifiedBy;
	
	@PrePersist
	public void prePersist() {
		if(this.status == null) this.status = OrganizerStatus.PENDING;
		if(this.createdAt == null) this.createdAt = Instant.now();
		if(this.updatedAt == null) this.updatedAt = this.createdAt;
		this.verified = false;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = Instant.now();
	}
}
