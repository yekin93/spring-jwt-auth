package com.example.demo.entity;

import java.time.Instant;

import com.example.demo.enums.MediaType;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_media")
public class EventMedia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", nullable = false)
	private Event event;
	
	@Column(name = "file_name", nullable = false)
	private String fileName;
	
	@Column(name = "file_ext", nullable = false, length = 10)
	private String fileExt;
	
	@Column(name = "media_type", nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private MediaType mediaType;
	
	@Column(name = "is_deleted")
	private boolean isDeleted;
	
	@Column(name = "modified_at")
	private Instant modifiedAt;
	
	@Column(name = "created_at")
	private Instant createdAt;
}
