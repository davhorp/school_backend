package com.school.app.services.auth;

import com.school.app.entity.User;
import com.school.app.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    private static final long JWT_TIME_VALIDITY = 1000 * 60  * 15;
    private static final long JWT_TIME_REFRESH_VALIDATE = 1000 * 60  * 60 * 24;

    @Value("${app.school.properties.role}")
    private String role;

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String generateToken(final User user) {
        return buildToken(user);
    }

    public String generateTokenByUser(final Usuario user) {
        return buildTokenByUser(user);
    }

    public String generateRefreshTokenByUser(final Usuario user) {
        return refreshTokenByUser(user);
    }

    public String generateRefreshToken(final User user) {
        return refreshToken(user);
    }

    public String refreshToken(final User user) {
        return Jwts
                .builder()
                .claims(Map.of(role, "administrador"))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TIME_REFRESH_VALIDATE))
                .signWith(getSignInKey())
                .compact();
    }

    public String refreshTokenByUser(final Usuario user) {
        return Jwts
                .builder()
                .claims(Map.of(role, user.getRol().getNombreRol()))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TIME_REFRESH_VALIDATE))
                .signWith(getSignInKey())
                .compact();
    }

    private String buildToken(final User user) {
        return Jwts
                .builder()
                .claims(Map.of(role, "administrador"))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TIME_VALIDITY))
                .signWith(getSignInKey())
                .compact();
    }

    private String buildTokenByUser(final Usuario user) {
        return Jwts
                .builder()
                .claims(Map.of(role, user.getRol().getNombreRol()))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TIME_VALIDITY))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    public boolean isTokenValidByUser(String token, Usuario user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

}
