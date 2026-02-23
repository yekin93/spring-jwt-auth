package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.example.demo.enums.CurrencyType;
import com.example.demo.enums.EventStatus;
import com.example.demo.enums.EventType;
import com.example.demo.enums.PricingType;
import com.example.demo.enums.VisibilityStatus;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organizer_id", nullable = false)
	private OrganizerProfile organizer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "venue_id", nullable = false)
	private EventVenue venue;
	
	@Column(nullable = false)
	private String title;
	
	@Column(nullable = false)
	private String description;
	
	@Column(name = "start_at", nullable = false)
	private Instant startAt;
	
	@Column(name = "end_at", nullable = false)
	private Instant endAt;
	
	private String timezone;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EventStatus status;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VisibilityStatus visibility;
	
	@Column(name = "event_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventType eventType;
	
	@Column(nullable = false, unique = true)
	private String slug;
	
	@Column(name = "pricing_type", nullable = true)
	private PricingType pricingType;
	
	@Column(name = "price_amount", precision = 15, scale = 2, nullable = false)
	private BigDecimal priceAmount;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 3)
	private CurrencyType currency;
	
	@Column(name = "modified_at")
	private Instant modifiedAt;
	
	@Column(name = "created_at")
	private Instant createdAt;
	
	@Column(name = "published_at")
	private Instant publishedAt;
	
	@OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
	private List<EventMedia> images;
	
	@PrePersist
	public void prePersist() {
		if(createdAt == null) createdAt = Instant.now();
		if(modifiedAt == null) modifiedAt = this.createdAt;
		if(status == null) status = EventStatus.DRAFTED;
		if(visibility == null) visibility = VisibilityStatus.PUBLIC;
		if(pricingType == null) pricingType = PricingType.FREE;
		if(eventType == null) eventType = EventType.ONSITE;
		if(currency == null) currency = CurrencyType.EUR;
		if(priceAmount == null) priceAmount = BigDecimal.ZERO;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.modifiedAt = Instant.now();
	}
}
