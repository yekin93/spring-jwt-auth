package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final PasswordEncoder passwordEncoder;
	private final CustomUserDetailsService userDetailsService;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	
	public SecurityConfig(JwtAuthFilter jwtAuthFilter, PasswordEncoder passwordEncoder, CustomUserDetailsService userDetailsService,
			CustomAuthenticationEntryPoint authenticationEntryPoint) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf((csfr) -> csfr.disable())
		.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authorizeHttpRequests((req) -> req.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/api/roles").hasAuthority("ROLE_ADMIN")
				.anyRequest().authenticated())
		.exceptionHandling(t -> t.authenticationEntryPoint(authenticationEntryPoint))
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
	
	@Bean
	protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	protected AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder);
		return authProvider;
	}
}
