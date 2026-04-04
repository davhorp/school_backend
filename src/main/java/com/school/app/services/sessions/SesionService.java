package com.school.app.services.sessions;

import com.school.app.services.auth.JwtService;
import com.school.app.entity.SesionAcceso;
import com.school.app.entity.Usuario;
import com.school.app.repository.SesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final JwtService jwtService;
    private final SesionRepository sesionRepository;

    /**
     * M&eacute;todo para la persistencia de la entidad {@link SesionAcceso}
     *
     * @param user {@link Usuario} entidad, como par&aacute;metro de entrada.
     */
    public void saveSession(Usuario user) {
        final SesionAcceso session = SesionAcceso.builder()
                .expired(false)
                .fechaCreacion(LocalDateTime.now())
                .sessionActive(false)
                .fechaExpiracion(LocalDateTime.now())
                .revoked(false)
                .tokenAcceso(jwtService.generateTokenByUser(user))
                .tokenRefresh(jwtService.generateRefreshTokenByUser(user))
                .usuario(user)
                .build();
        sesionRepository.save(session);
    }
}
