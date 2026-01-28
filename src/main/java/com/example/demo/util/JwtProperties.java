package com.example.demo.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

	private Long refreshTokenExpirationTimeMs;
	private Long accessTokenExpirationTimeMs;
	private String secretKey;
	
	
	public Long getRefreshTokenExpirationTimeMs() {
		return refreshTokenExpirationTimeMs;
	}
	public void setRefreshTokenExpirationTimeMs(Long refreshTokenExpirationTimeMs) {
		this.refreshTokenExpirationTimeMs = refreshTokenExpirationTimeMs;
	}
	public Long getAccessTokenExpirationTimeMs() {
		return accessTokenExpirationTimeMs;
	}
	public void setAccessTokenExpirationTimeMs(Long accessTokenExpirationTimeMs) {
		this.accessTokenExpirationTimeMs = accessTokenExpirationTimeMs;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
