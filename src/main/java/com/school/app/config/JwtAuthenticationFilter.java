package com.school.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.app.dto.response.ExceptionResponse;
import com.school.app.entity.Usuario;
import com.school.app.repository.SesionRepository;
import com.school.app.services.auth.JwtService;
import com.school.app.repository.UsuarioRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j // Agregamos la anotación de Lombok para el logger
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
        log.debug("Procesando petición en JwtAuthenticationFilter para la URI: {}", request.getServletPath());
        if (request.getServletPath().contains("/api/v1/auth")) {
            log.debug("Omitiendo validación JWT para la ruta pública de autenticación.");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Cabecera Authorization ausente o no tiene el prefijo 'Bearer '. Dejando pasar la petición sin autenticar.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        try {
            final String userEmail = jwtService.extractUsername(jwt);
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            log.debug("Email extraído del JWT: {}", userEmail);

            if (userEmail == null || authentication != null) {
                log.debug("Email es nulo o el usuario ya está autenticado en el SecurityContext.");
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
                        log.info("Usuario '{}' autenticado exitosamente mediante JWT.", userEmail);
                    } else {
                        log.warn("El token JWT es inválido para el usuario '{}'.", userEmail);
                    }
                } else {
                    log.warn("Usuario '{}' no encontrado en la base de datos.", userEmail);
                }
            } else {
                log.warn("El token JWT ha expirado o ha sido revocado en la base de datos de sesiones.");
            }
        } catch (ExpiredJwtException ex) {
            log.error("Se capturó ExpiredJwtException: El token JWT ha expirado - {}", ex.getMessage());
            handleException(response, ex);
            return; // Retornamos para evitar continuar con el filtro
        } catch (Exception ex) {
            log.error("Error inesperado procesando el filtro JWT", ex);
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        log.debug("Construyendo respuesta JSON de error no autorizado (401)");
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
