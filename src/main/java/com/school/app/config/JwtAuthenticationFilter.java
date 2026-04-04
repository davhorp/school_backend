package com.school.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.app.dto.response.ExceptionResponse;
import com.school.app.entity.Usuario;
import com.school.app.repository.SesionRepository;
import com.school.app.repository.TokenRepository;
import com.school.app.auth.service.JwtService;
import com.school.app.repository.UserRepository;
import com.school.app.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final SesionRepository sesionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwt = authHeader.substring(7);
        try {
            final String userEmail = jwtService.extractUsername(jwt);
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userEmail == null || authentication != null) {
                filterChain.doFilter(request, response);
                return;
            }
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            final boolean isTokenExpiredOrRevoked = sesionRepository.findByTokenAcceso(jwt)
                    .map(token -> !token.isExpired() && !token.isRevoked())
                    .orElse(false);
            if (isTokenExpiredOrRevoked) {
                final Optional<Usuario> user = usuarioRepository.findByEmail(userEmail);
                if (user.isPresent()) {
                    final boolean isTokenValid = jwtService.isTokenValidByUser(jwt, user.get());
                    if (isTokenValid) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
        }catch (ExpiredJwtException ex) {
            handleException(response, ex);
        }

//        final boolean isTokenExpiredOrRevoked = tokenRepository.findByToken(jwt)
//                .map(token -> !token.getIsExpired() && !token.getIsRevoked())
//                .orElse(false);
        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name(),
                "El token de la peticion esta expirado.",
                "ExpiredJwtException"
        );
        new ObjectMapper().writeValue(response.getWriter(), exceptionResponse);
    }
}
