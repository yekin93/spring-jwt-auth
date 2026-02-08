package com.example.demo.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false)
	private String username;
	
	@Column(name = "surname", nullable = false)
	private String surname;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "modified_at", nullable = false)
	private Instant modifiedAt;
	
	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
	
	@Column(name = "confirmed", nullable = false)
	private boolean confirmed;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		name = "user_role",
		joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
		inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
	)
	private Set<Role> roles = new HashSet<>();
	
	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
	private OrganizerProfile organizerProfile;
	

	@PrePersist
	public void prePersist() {
		if(this.createdAt == null) this.createdAt = Instant.now();
		if(this.modifiedAt == null) this.modifiedAt = this.createdAt;
	}
	
	@PreUpdate
	public void preUpdate() {
		if(this.modifiedAt == null) this.modifiedAt = Instant.now();
	}
}
