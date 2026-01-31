package com.example.demo.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.RefreshTokenRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.interfaces.IRefreshTokenService;
import com.example.demo.util.JwtProperties;

@Service
public class RefreshTokenService implements IRefreshTokenService {

	private final UserRepo userRepo;
	private final RefreshTokenRepo refreshTokenRepo;
	private final JwtProperties jwtProperties;
	
	public RefreshTokenService(RefreshTokenRepo refreshTokenRepo, UserRepo userRepo, JwtProperties jwtProperties) {
		this.refreshTokenRepo = refreshTokenRepo;
		this.userRepo = userRepo;
		this.jwtProperties = jwtProperties;
	}
	
	@Override
	public RefreshToken createRefreshToken(String userEmail, String ipAddress, String userAgent) {
		User user = userRepo.findByEmail(userEmail).orElseThrow(() -> new NotFoundException("User not found:" + userEmail));
		RefreshToken token = new RefreshToken();
		token.setUser(user);
		token.setIpAddress(ipAddress);
		token.setUserAgent(userAgent);
		token.setExpiryDate(Instant.now().plusMillis(jwtProperties.getRefreshTokenExpirationTimeMs()));
		token.setToken(UUID.randomUUID().toString());
		return refreshTokenRepo.save(token);
	}

	@Override
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepo.findByTokenAndRevokedFalse(token);
	}

	@Override
	public RefreshToken verifyExpiration(RefreshToken token) {
		if(token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepo.delete(token);
			throw new RuntimeException("Refresh token invalid or expired...");
		}
		return token;
	}

	@Override
	public void revokeRefreshToken(String token) {
		RefreshToken refreshToken = findByToken(token).orElseThrow(() -> new RuntimeException("Invalide refresh token"));
		refreshToken.setRevoked(true);
		refreshTokenRepo.save(refreshToken);
	}

}
