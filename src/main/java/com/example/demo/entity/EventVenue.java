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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
	
	@Column(nullable = false, length = 200)
	private String name;
	
	@Column(nullable = false, length = 255)
	private String street;
	
	@Column(name = "house_number", nullable = false, length = 50)
	private String houseNumber;
	
	@Column(length = 50)
	private String unit;
	
	@Column(name = "country_code", nullable = false, length = 3)
	private String countryCode;
	
	@Column(nullable = false, length = 120)
	private String city;
	
	@Column(nullable = false, length = 20)
	private String postalCode;
	
	@Column(precision = 10, scale = 7)
	@DecimalMax(value = "90.0", inclusive = true)
	@DecimalMin(value = "-90.0", inclusive = true)
	private BigDecimal lat;
	
	@Column(precision = 10, scale = 7)
	@DecimalMax(value = "180.0", inclusive = true)
	@DecimalMin(value = "-180.0", inclusive = true)
	private BigDecimal lng;
	
	@Column(name = "modified_at")
	private Instant modifiedAt;
	
	@Column(name = "created_at")
	private Instant createdAt;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = Instant.now();
		this.modifiedAt = this.createdAt;
	}
}
