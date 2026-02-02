package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Confirmation;

public interface ConfirmationRepo extends JpaRepository<Confirmation, Long> {

	Optional<Confirmation> findByToken(String token);
}
