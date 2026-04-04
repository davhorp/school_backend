package com.school.app.services.auth;

import com.school.app.entity.VerificacionUsuario;
import com.school.app.repository.UserVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MarkTokenAsConsumedService {

    private final UserVerificationRepository userVerificationRepository;

    public void markTokenAsConsumedMethod(VerificacionUsuario data){
        data.setConsumido(true);
        data.setFechaVerificacion(LocalDateTime.now());
        userVerificationRepository.save(data);
    }
}
