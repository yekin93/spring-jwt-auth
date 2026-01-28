package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.RefreshToken;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {

}
