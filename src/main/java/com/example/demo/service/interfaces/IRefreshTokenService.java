package com.example.demo.service.interfaces;

import java.util.Optional;

import com.example.demo.entity.RefreshToken;

public interface IRefreshTokenService {

	RefreshToken createRefreshToken(String userEmail, String ipAddress, String userAgent);
	Optional<RefreshToken> findByToken(String token);
	RefreshToken verifyExpiration(RefreshToken token);
	void revokeRefreshToken(String token);
}
