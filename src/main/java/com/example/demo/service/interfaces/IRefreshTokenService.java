package com.example.demo.service.interfaces;

import com.example.demo.entity.RefreshToken;

public interface IRefreshTokenService {

	RefreshToken createRefreshToken(String userEmail, String ipAddress, String userAgent);
	
}
