package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Hangi endpoint'lere izin verilecek
                .allowedOrigins("http://localhost:3000") // Frontend URL'i
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true) // Cookie gönderilecekse
                .maxAge(3600); // Preflight cache süresi (1 saat)
    }
}
