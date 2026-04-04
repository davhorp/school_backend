package com.school.app.services.auth;

import com.school.app.dto.requets.ValidateAndChangePasswordRequest;
import com.school.app.dto.response.CommonsResponse;
import com.school.app.dto.response.RequestPasswordChangeResponse;
import com.school.app.dto.response.ValidateAndChangePasswordResponse;
import com.school.app.entity.Usuario;
import com.school.app.exceptions.CodeVerificationExpiredException;
import com.school.app.exceptions.CodeVerificationIncorrectException;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ValidateAndChangePasswordRequestService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public ValidateAndChangePasswordResponse validateAndChangePasswordMethod(ValidateAndChangePasswordRequest request){
        Usuario usr = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("El usuario con E-Mail: %s no existe en el sistema", request.email())));
        if (usr.getResetPasswordCode() == null || !usr.getResetPasswordCode().equals(request.code())) {
            throw new CodeVerificationIncorrectException("El código de verificación es incorrecto.");
        }
        if (usr.getResetPasswordExpire().isBefore(LocalDateTime.now())) {
            throw new CodeVerificationExpiredException("El código ha expirado. Por favor, solicita uno nuevo.");
        }
        usr.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        usr.setResetPasswordCode(null);
        usr.setResetPasswordExpire(null);
        usuarioRepository.save(usr);
        return new ValidateAndChangePasswordResponse(
                new CommonsResponse(
                        "200",
                        "Cambio de contraseña exitoso"
                ));
    }
}
