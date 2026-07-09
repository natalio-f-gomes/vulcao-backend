package dev.hope.vulcaobackend.Authentication;


import dev.hope.vulcaobackend.Configuration.SSMParameterStore.ParameterStoreService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.builder;


@Service
public class JWTService {


    private final String secret;
    private final SecretKey signingKey;
    private static final Logger log = LoggerFactory.getLogger(JWTService.class);

    @Autowired
    public JWTService(ParameterStoreService parameterStoreService) {
        this.secret = parameterStoreService.getParameter("/vulcao/jwt/secret");
        this.signingKey = Keys.hmacShaKeyFor(this.secret.getBytes(StandardCharsets.UTF_8));
    }

    private static final long expirationTime = 1000 * 60 * 60 * 24; // 1 day

    private SecretKey getSigningKey() {
        return this.signingKey;
    }

    public String generateJWTToken(String username, String role) {
        log.info("Generating JWT for user='{}', role='{}'", username, role);
        String token = Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
        log.debug("Generated token expires at {}", new Date(System.currentTimeMillis() + expirationTime));
        return token;
    }

    public String extractUsername(String token) {
        try {
            String username = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            log.debug("Extracted username='{}' from token", username);
            return username;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Failed to extract username: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            log.warn("isTokenValid called with null/blank token");
            return false;
        }
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())   // <-- was hmacShaKeyFor(secret.getBytes()) — key mismatch bug
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            log.debug("Token valid. subject='{}', expires={}", claims.getSubject(), claims.getExpiration());
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token EXPIRED for subject='{}', expired at {}", e.getClaims().getSubject(), e.getClaims().getExpiration());
            return false;
        } catch (SignatureException e) {
            log.error("Token SIGNATURE invalid — signing key mismatch between generation and validation?");
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Token MALFORMED (not a JWT / corrupted cookie value): {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token invalid: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }
}