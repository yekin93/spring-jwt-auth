# Security Architecture Documentation

## Overview

This document explains the security design decisions and configurations in this Spring Boot JWT authentication application.

## Authentication Architecture

### JWT-Based Stateless Authentication

**Decision:** Use JWT (JSON Web Tokens) for stateless authentication  
**Rationale:**
- Scalability: No server-side session storage required
- Microservices-friendly: Tokens can be validated independently
- Mobile-friendly: Tokens work well with mobile applications
- Performance: Reduces database lookups for authentication

**Implementation:**
- Access Token: Short-lived (15 minutes by default), stored in memory/localStorage
- Refresh Token: Long-lived (7 days), stored in HTTP-only cookie and database

### Token Storage Strategy

#### Access Token (Client-Side)
**Storage:** Returned in response body  
**Lifetime:** 15 minutes (configurable via `jwt.access-token-expiration-time-ms`)  
**Usage:** Sent in Authorization header as `Bearer <token>`  
**Security Considerations:**
- Short expiration minimizes window of opportunity if compromised
- Client must implement secure storage (avoid localStorage if possible)
- Not stored in cookies to allow easy usage in API clients

#### Refresh Token (HTTP-Only Cookie + Database)
**Storage:** 
- HTTP-only, Secure, SameSite cookie
- Database record with IP address and User-Agent tracking  

**Lifetime:** 7 days (configurable via `jwt.refresh-token-expiration-time-ms`)  
**Security Measures:**
```java
ResponseCookie.from("refresh_token", token)
    .httpOnly(true)      // Prevents JavaScript access (XSS protection)
    .secure(true)        // HTTPS only (MITM protection)
    .sameSite("Strict")  // CSRF protection
    .path("/auth")       // Limits cookie scope
    .maxAge(Duration.ofDays(7))
    .build();
```

**Why Database Storage?**
- Enables token revocation (logout functionality)
- Tracks token usage (IP, User-Agent, last used time)
- Allows detection of suspicious activity
- Enables "logout from all devices" functionality

## Security Configurations

### CSRF Protection

**Decision:** CSRF disabled for API  
**Configuration:** `SecurityConfig.java:48`
```java
.csrf((csrf) -> csrf.disable())
```

**Rationale:**
- Stateless JWT authentication doesn't use session cookies
- APIs consumed by non-browser clients (mobile apps, other services)
- SameSite cookie policy provides CSRF protection for browser clients
- Tokens require explicit Authorization header, not sent automatically

**When to Enable:** If adding traditional form-based authentication or session management

### CORS Configuration

**Decision:** Externalized CORS configuration  
**Configuration:** `application.properties` → `cors.allowed-origins`  
**Default:** `http://localhost:3000` (development)

**Rationale:**
- Different origins needed for dev, staging, production
- Avoids code changes during deployment
- Supports multiple origins via comma-separated values

**Production Example:**
```properties
cors.allowed-origins=https://app.example.com,https://admin.example.com
```

**Security Note:** 
- Never use `*` (wildcard) with `allowCredentials: true`
- Always specify explicit origins in production
- Consider environment-specific configuration

### Session Management

**Decision:** Stateless sessions  
**Configuration:**
```java
.sessionManagement((session) -> 
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```

**Rationale:**
- JWT provides authentication state
- No server-side session storage needed
- Improves horizontal scalability
- Reduces memory footprint

### Password Encoding

**Decision:** BCrypt with default strength (10 rounds)  
**Configuration:** `PasswordConfig.java`

**Rationale:**
- Industry-standard adaptive hashing algorithm
- Automatically includes salt
- Configurable work factor (10 rounds = 2^10 iterations)
- Resistant to rainbow table attacks

**Future Consideration:** Increase rounds as computing power increases

## Authentication Flow

### 1. User Registration (`POST /api/auth/signup`)
```
1. Validate input (email format, password strength, etc.)
2. Check for duplicate email
3. Hash password with BCrypt
4. Create user record
5. Generate confirmation token (full UUID)
6. Store confirmation record
7. Return user data (without password)
```

**Security Measures:**
- Email uniqueness enforced at database level
- Password never stored in plain text
- Confirmation token is full UUID (32 chars, not 8)

### 2. User Login (`POST /api/auth/login`)
```
1. Authenticate credentials via Spring Security
2. Generate access token (JWT)
3. Create refresh token (UUID)
4. Store refresh token in database with metadata:
   - IP address
   - User-Agent
   - Expiration time
5. Set HTTP-only cookie with refresh token
6. Return access token in response body
```

**Security Measures:**
- Uses Spring Security's authentication manager
- Tracks device/location via IP and User-Agent
- Refresh token only sent via secure cookie, not response body

### 3. API Access (Protected Endpoints)
```
1. Client sends access token in Authorization header
2. JwtAuthFilter intercepts request
3. Extract and validate JWT token:
   - Signature verification
   - Expiration check
   - User existence check
4. Load user details and authorities
5. Set authentication in SecurityContext
6. Proceed with request
```

**Security Measures:**
- Token signature prevents tampering
- Expiration enforced at JWT level
- User existence verified (handles deleted users)
- Role-based authorization via `@PreAuthorize`

### 4. Token Refresh (`POST /api/auth/refresh`)
```
1. Extract refresh token from HTTP-only cookie
2. Validate token exists in database
3. Check expiration
4. Verify token not revoked
5. Generate new access token
6. Return new access token
```

**Future Enhancement:** Add IP/User-Agent validation to detect token theft

### 5. Logout (`POST /api/auth/logout`)
```
1. Extract refresh token from cookie
2. Delete/revoke token from database
3. Clear cookie (client should also discard access token)
```

## Exception Handling

### Global Exception Handler
**Location:** `GlobalExceptionHandler.java`

**Handled Exceptions:**
- `NotFoundException` → 404 with actual error message
- `DuplicateEntryException` → 409 Conflict
- `InvalidRefreshTokenException` → 401 Unauthorized
- `MethodArgumentNotValidException` → 400 with field errors
- `AuthorizationDeniedException` → 403 Forbidden
- `DataIntegrityViolationException` → 409 Conflict
- Generic `Exception` → 500 Internal Server Error

**Security Considerations:**
- Avoids leaking sensitive information in error messages
- Logs errors server-side for debugging
- Returns consistent error format
- Never exposes stack traces to clients

### JWT Filter Exception Handling
**Location:** `JwtAuthFilter.java`

**Handled JWT Exceptions:**
- `ExpiredJwtException` → 401 with "token expired" message
- `SignatureException` → 401 with "invalid signature"
- `MalformedJwtException` → 401 with "malformed token"
- `UnsupportedJwtException` → 401 with "unsupported token"
- `IllegalArgumentException` → 401 with "empty token"

**Security Benefit:** Prevents application crashes from malicious tokens

## Logging and Monitoring

### Request Logging
**Implementation:** `RequestLoggingFilter.java`

**Logged Information:**
- Request ID (correlation)
- HTTP method and URI
- Client IP address
- User email (if authenticated)
- User roles

**Security Features:**
- Password masking in request bodies
- MDC (Mapped Diagnostic Context) for correlation
- IP address extraction (X-Forwarded-For aware)

### Authentication Logging
**Key Events Logged:**
- Login attempts (success/failure)
- Token validation failures
- User not found during authentication
- Authorization denials

**Log Levels:**
- `INFO`: Successful authentications
- `WARN`: Failed attempts, suspicious activity
- `ERROR`: System errors, unexpected exceptions

## Role-Based Authorization

**Implementation:** Method-level security with `@PreAuthorize`

**Example:**
```java
@PreAuthorize("hasRole('ADMIN')")
public void adminOnlyMethod() { }

@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public void userOrAdminMethod() { }
```

**Security Benefit:**
- Fine-grained access control
- Declarative and easy to audit
- Enforced at method invocation (can't bypass by URL manipulation)

## Threat Model and Mitigations

| Threat | Mitigation |
|--------|-----------|
| **XSS (Cross-Site Scripting)** | HTTP-only cookies, input validation |
| **CSRF (Cross-Site Request Forgery)** | SameSite cookies, stateless auth |
| **MITM (Man-in-the-Middle)** | Secure cookie flag (HTTPS only) |
| **Token Theft** | Short-lived access tokens, refresh token rotation |
| **SQL Injection** | JPA with specifications, parameterized queries |
| **Brute Force** | BCrypt (slow hashing), TODO: rate limiting |
| **Session Fixation** | Stateless auth, no server sessions |
| **Clickjacking** | TODO: X-Frame-Options header |
| **Information Disclosure** | Sanitized error messages, password masking in logs |

## Configuration Security

### Secrets Management

**Recommendation:** Use environment variables or secret management service

**Example (`application.properties`):**
```properties
# DO NOT commit actual secrets!
jwt.secret-key=${JWT_SECRET_KEY}
spring.datasource.password=${DB_PASSWORD}
```

**Production Secrets Management:**
- AWS Secrets Manager
- HashiCorp Vault
- Kubernetes Secrets
- Azure Key Vault

### Property Files

**Files:**
- `application.properties` → Ignored by git (contains secrets)
- `application.properties.example` → Committed (template only)

**Security:**
- `.gitignore` includes `application.properties`
- Example file has dummy values only
- All secrets configurable via environment variables

## Known Limitations and Future Improvements

### Current Limitations

1. **No Rate Limiting**
   - Vulnerable to brute-force attacks
   - No protection against DoS on auth endpoints

2. **Refresh Token Validation**
   - IP/User-Agent stored but not validated
   - Token theft may go undetected

3. **No Account Lockout**
   - Multiple failed login attempts not tracked
   - No temporary account suspension

4. **Token Revocation**
   - Only refresh tokens can be revoked
   - Access tokens valid until expiration (no blacklist)

### Planned Improvements

1. **Rate Limiting**
   - Implement Bucket4j or Spring Security rate limiter
   - Limit login attempts per IP/email
   - Limit refresh token usage

2. **Enhanced Token Validation**
   - Validate IP/User-Agent on token refresh
   - Alert user on suspicious activity
   - Implement token fingerprinting

3. **Security Headers**
   - Content-Security-Policy
   - X-Frame-Options
   - X-Content-Type-Options
   - Referrer-Policy

4. **Two-Factor Authentication**
   - TOTP (Time-based OTP) support
   - SMS/Email verification
   - Backup codes

5. **Audit Logging**
   - Detailed security event logging
   - Compliance audit trails
   - Integration with SIEM systems

## Compliance Considerations

### GDPR (EU)
- User data collected: email, password hash, IP address, User-Agent
- Purpose: Authentication and security
- Retention: Until account deletion
- User rights: Right to access, deletion, portability

### CCPA (California)
- Disclose data collection in privacy policy
- Provide opt-out mechanism
- Allow data deletion upon request

### HIPAA (Healthcare) / PCI DSS (Payment)
- Additional controls required
- Encryption at rest needed
- Regular security assessments mandatory
- Detailed audit logging required

## Security Maintenance

### Regular Tasks

**Weekly:**
- Review authentication failure logs
- Monitor for suspicious patterns

**Monthly:**
- Dependency vulnerability scanning
- Review and rotate JWT secrets

**Quarterly:**
- Security audit
- Penetration testing
- Update dependencies

**Annually:**
- Architecture review
- Threat model update
- Compliance assessment

### Dependency Updates

**Check for vulnerabilities:**
```bash
./mvnw dependency-check:check
```

**Update dependencies:**
```bash
./mvnw versions:display-dependency-updates
```

---

**Document Version:** 1.0  
**Last Updated:** 2026-02-11  
**Next Review:** 2026-05-11
