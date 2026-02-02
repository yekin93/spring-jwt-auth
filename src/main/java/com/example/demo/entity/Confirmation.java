package com.example.demo.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirmation")
public class Confirmation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "token", unique = true, nullable = false)
	private String token;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, unique = false)
	private User user;
	
	@Column(name = "confirmed_at", nullable = true)
	private Instant confirmedAt;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
	@PrePersist
	private void prePersist() {
		if(this.getCreatedAt() == null) this.createdAt = Instant.now();
	}
	
}
