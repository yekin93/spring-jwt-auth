package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.entity.User;

public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

	Optional<User> findByEmailAndConfirmedTrue(String email);
	Optional<User> findByEmail(String email);
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
}
