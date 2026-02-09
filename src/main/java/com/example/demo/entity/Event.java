package com.example.demo.entity;

import java.time.Instant;

import com.example.demo.enums.EventStatus;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String subject;
	
	@Column(name = "start_date", nullable = false)
	private Instant startDate;
	
	@Column(name = "end_date", nullable = false)
	private Instant endDate;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EventStatus status;
	
	@Column(nullable = false, unique = true)
	private String slug;
	
	@Column(name = "modified_at")
	private Instant modifiedAt;
	
	@Column(name = "created_at")
	private Instant createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizer_id", nullable = false)
	private OrganizerProfile organizer;
	
	@PrePersist
	public void prePersist() {
		if(createdAt == null) this.createdAt = Instant.now();
		if(modifiedAt == null) this.modifiedAt = this.createdAt;
	}
	
	@PreUpdate
	public void preUpdate() {
		if(this.modifiedAt == null) this.modifiedAt = Instant.now();
	}
}
