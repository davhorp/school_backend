package com.school.app.services.auth;

import com.school.app.entity.Usuario;
import com.school.app.entity.VerificacionUsuario;
import com.school.app.repository.UserVerificationRepository;
import com.school.app.services.shipments.email.SendingEmailWithVerificationLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVerificationService {

    private final UserVerificationRepository userVerificationRepository;
    private final SendingEmailWithVerificationLinkService sendingEmailWithVerificationLinkService;

    public void userVerificationAdd(Usuario usr){
        String token = UUID.randomUUID().toString();
        VerificacionUsuario verificacion = VerificacionUsuario.builder()
                .usuario(usr)
                .fechaCreacion(LocalDateTime.now())
                .tokenVerificacion(token)
                .fechaExpiracion(LocalDateTime.now().plusHours(24))
                .tipoVerificacion("EMAIL")
                .build();
        userVerificationRepository.save(verificacion);
        sendingEmailWithVerificationLinkService.sendVerificationLink(usr, token);
    }
}
