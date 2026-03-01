package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_venue")
public class EventVenue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy = "venue", fetch = FetchType.LAZY)
	private List<Event> events;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizer_id", nullable = false)
	private OrganizerProfile organizer;
	
	@Column(nullable = false, length = 200)
	private String name;
	
	@Column(name = "address_line1", nullable = false, length = 200)
	private String addressLine1;
	
	@Column(name = "address_line2", length = 200)
	private String addressLine2;
	
	@Column(nullable = false, length = 120)
	private String city;
	
	@Column(nullable = false, length = 20)
	private String postalCode;
	
	@Column(precision = 10, scale = 7)
	private BigDecimal lat;
	
	@Column(precision = 10, scale = 7)
	private BigDecimal lng;
	
	@Column(name = "modified_at")
	private Instant modifiedAt;
	
	@Column(name = "created_at")
	private Instant createdAt;
}
