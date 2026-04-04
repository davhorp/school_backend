package com.school.app.services.config;

import com.school.app.dto.response.CommonsResponse;
import com.school.app.dto.response.LoginResponse;
import com.school.app.dto.response.ProfileDetailsResponse;
import com.school.app.dto.response.RequestPasswordChangeResponse;
import com.school.app.entity.Usuario;
import com.school.app.entity.VerificacionUsuario;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.UsuarioRepository;
import com.school.app.services.shipments.email.SendingEmailRequestPasswordChangeService;
import com.school.app.utils.UtilsMethodsSchool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestPasswordChangeService {

    private final UsuarioRepository usuarioRepository;
    private final UtilsMethodsSchool utilsMethodsSchool;
    private final SendingEmailRequestPasswordChangeService sendingEmailRequestPasswordChangeService;

    public RequestPasswordChangeResponse requestPasswordChangeServiceMethod(String email){
        Usuario usr = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("El usuario con E-Mail: %s no existe en el sistema", email)));
        String code = utilsMethodsSchool.generarCodigoVerificacion();
        usr.setResetPasswordCode(code);
        usr.setResetPasswordExpire(LocalDateTime.now().plusMinutes(10));
        usuarioRepository.save(usr);
        sendingEmailRequestPasswordChangeService.sendingEmailRequestPasswordChangeMethod(usr, code);
        return new RequestPasswordChangeResponse(
                new CommonsResponse(
                        "200",
                        "Envio de codigo exitoso"
                ));
    }

}
