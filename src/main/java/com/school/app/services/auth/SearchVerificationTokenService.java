package com.school.app.services.auth;

import com.school.app.entity.VerificacionUsuario;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.UserVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchVerificationTokenService {

    private final UserVerificationRepository userVerificationRepository;

    public VerificacionUsuario findTokenVerificationByUser(String token){
        return userVerificationRepository.findByTokenVerificacion(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token de verificación no válido."));
    }
}
