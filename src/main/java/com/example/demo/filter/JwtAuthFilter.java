package com.example.demo.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.custom.CustomUserDetails;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	
	public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		System.out.println("URI=" + request.getRequestURI() + " AUTH=" + authHeader);

		String email = null;
		String jwt = null;
		CustomUserDetails userDetails = null;
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			jwt = authHeader.substring(7);
			email = jwtUtil.extractEmail(jwt);
		} 
		
		if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			userDetails = userDetailsService.loadUserByUsername(email);
			if(jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authToken);
				System.out.println("AUTH SET: " + SecurityContextHolder.getContext().getAuthentication());
			}
		}
		if(userDetails != null) {
			System.out.println("Request: " + userDetails.getEmail());
		}
		filterChain.doFilter(request, response);
	}

}
