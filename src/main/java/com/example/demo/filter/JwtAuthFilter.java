package com.example.demo.filter;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jboss.logging.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.custom.CustomUserDetails;
import com.example.demo.exception.NotFoundException;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(0)
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService userDetailsService;
	
	public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			setupMDC(request);
			final String authHeader = request.getHeader("Authorization");
			
			String jwt = null;
			CustomUserDetails userDetails = null;
			String email = null;
			
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				jwt = authHeader.substring(7);
				email = jwtUtil.extractEmail(jwt);
			} 
			
			if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				try {
					userDetails = userDetailsService.loadUserByUsername(email);
					if(userDetails != null && jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				} catch (NotFoundException e) {
					log.warn("User not found during JWT authentication: {}", email);
					handleNotFoundException(response, "User not found");
					return;
				}
				
			}
			if(userDetails != null) {
				updateMDCWithUser(userDetails);
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
            log.warn("JWT token expired for request: {}", request.getRequestURI());
            handleExpiredJwtException(response);
            return;
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature for request: {}", request.getRequestURI());
            handleInvalidJwtException(response, "Invalid JWT signature");
            return;
            
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token for request: {}", request.getRequestURI());
            handleInvalidJwtException(response, "Malformed JWT token");
            return;
            
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token for request: {}", request.getRequestURI());
            handleInvalidJwtException(response, "Unsupported JWT token");
            return;
            
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty for request: {}", request.getRequestURI());
            handleInvalidJwtException(response, "JWT token is empty");
            return;
        } finally {
			MDC.clear();
		}
	}
	
	private void handleNotFoundException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.setContentType("application/json;charset=UTF-8");
        
        String jsonResponse = String.format("""
            {
                "status": 404,
                "error": "Not Found",
                "message": "%s",
                "timestamp": "%s"
            }
            """, message, Instant.now());
        
        response.getWriter().write(jsonResponse);
    }
	
	private void handleInvalidJwtException(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        String jsonResponse = """
            {
                "status": 401,
                "error": "Unauthorized",
                "message": "%s",
                "timestamp": "%s"
            }
            """.formatted(message, java.time.LocalDateTime.now());
        
        response.getWriter().write(jsonResponse);
    }
	
	private void handleExpiredJwtException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        
        String jsonResponse = """
            {
                "status": 401,
                "error": "Unauthorized",
                "message": "JWT token expired. Please login again.",
                "timestamp": "%s"
            }
            """.formatted(java.time.LocalDateTime.now());
        
        response.getWriter().write(jsonResponse);
    }
	
	private void setupMDC(HttpServletRequest request) {
		String requestId = UUID.randomUUID().toString().substring(0, 8);
		MDC.put("requestId", requestId);
		
		String ip = getClientIP(request);
		MDC.put("ip", ip);
		
		MDC.put("method", request.getMethod());
		MDC.put("uri", request.getRequestURI());
		MDC.put("user", "anonymous");
	}
	
	private void updateMDCWithUser(CustomUserDetails userDetails) {
        MDC.put("user", userDetails.getUsername());

        if (userDetails instanceof CustomUserDetails) {
            CustomUserDetails customUser = (CustomUserDetails) userDetails;
            MDC.put("email", customUser.getEmail());
            MDC.put("userId", String.valueOf(customUser.getId()));
        }
        
        // Roller
        String roles = userDetails.getAuthorities().stream()
            .map(authority -> authority.getAuthority())
            .collect(Collectors.joining(","));
        MDC.put("roles", roles);
    }
	
	private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

}
