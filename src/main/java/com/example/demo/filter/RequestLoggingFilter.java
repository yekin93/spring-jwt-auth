package com.example.demo.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.example.demo.custom.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(1)
public class RequestLoggingFilter extends OncePerRequestFilter {
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) throws ServletException, IOException {
        
 
        ContentCachingRequestWrapper wrappedRequest = 
            (request instanceof ContentCachingRequestWrapper) 
                ? (ContentCachingRequestWrapper) request 
                : new ContentCachingRequestWrapper(request, 0);
                
        ContentCachingResponseWrapper wrappedResponse = 
            (response instanceof ContentCachingResponseWrapper) 
                ? (ContentCachingResponseWrapper) response 
                : new ContentCachingResponseWrapper(response);
        
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString().substring(0, 8);
        
        // MDC
        MDC.put("requestId", requestId);
        MDC.put("ip", getClientIP(wrappedRequest));
        
        try {
            // ========== REQUEST LOG (filter chain ÖNCESI) ==========
            String method = wrappedRequest.getMethod();
            String uri = wrappedRequest.getRequestURI();
            String query = wrappedRequest.getQueryString();
            
            String requestBody = getRequestBody(wrappedRequest);
            log.info("==> REQUEST [{}] {} {} | Query: {} | Body: {}", 
                requestId, method, uri, query != null ? query : "N/A", requestBody != null ? requestBody : "N/A");
            
            // Filter chain
            filterChain.doFilter(wrappedRequest, wrappedResponse);
            
        } finally {
            long duration = System.currentTimeMillis() - startTime;            String user = getUsername();
            int status = wrappedResponse.getStatus();
            
            String requestBody = getRequestBody(wrappedRequest);
            
            // Response body
            String responseBody = getResponseBody(wrappedResponse);
            
            log.info("<== RESPONSE [{}] User: {} | Status: {} | Duration: {}ms | ReqBody: {} | ResBody: {}", 
                requestId, user, status, duration, requestBody, responseBody);
            wrappedResponse.copyBodyToResponse();
            MDC.clear();
        }
    }
    
    /**
     * Request body'yi al
     */
    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content == null || content.length == 0) {
            return "N/A";
        }
        
        try {
            String body = new String(content, request.getCharacterEncoding());

            if (body.contains("password")) {
                body = body.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"***\"");
            }
            
            return body;
            
        } catch (Exception e) {
            return "[Error]";
        }
    }
    
    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content == null || content.length == 0) {
            return "N/A";
        }
        
        try {
            String body = new String(content, response.getCharacterEncoding());
           
            return body;
            
        } catch (Exception e) {
            return "[Error]";
        }
    }

    private String getUsername() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            
            if (auth == null || !auth.isAuthenticated()) {
                return "anonymous";
            }
            
            Object principal = auth.getPrincipal();
            
            if (principal instanceof String) {
                String username = (String) principal;
                return "anonymousUser".equals(username) ? "anonymous" : username;
            }
            
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getUsername();
            }
            
            return principal != null ? principal.toString() : "anonymous";
            
        } catch (Exception e) {
            return "anonymous";
        }
    }
    
    /**
     * Client IP
     */
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        log.info("YKN not filter {}", path);
        return path.endsWith(".css") || 
               path.endsWith(".js") || 
               path.endsWith(".png") || 
               path.endsWith(".ico") ||
               path.startsWith("/actuator") ||
               path.startsWith("/swagger") ||
               path.startsWith("/v3/api-docs");
    }
}