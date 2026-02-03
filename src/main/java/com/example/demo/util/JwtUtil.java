package com.example.demo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	private final JwtProperties jwtProperties;
	
	public JwtUtil(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}
	
	public String generateToken(String email) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, email);
	}
	
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(generateSecretKey(jwtProperties.getSecretKey()), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private SecretKeySpec generateSecretKey(String key) {
		return new SecretKeySpec(jwtProperties.getSecretKey().getBytes(), SignatureAlgorithm.HS256.getJcaName());
	}
	
	public Claims extractClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(generateSecretKey(jwtProperties.getSecretKey()))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String extractEmail(String token) {
		return extractClaims(token).getSubject();
	}
	
	public boolean isTokenValid(String token, String email) {
		final String extractedUsername = extractEmail(token);
		return extractedUsername.equals(email) && !isTokenExpired(token);
	}
	
	private boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}
}
