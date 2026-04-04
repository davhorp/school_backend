package com.school.app.controllers.auth;

import com.school.app.entity.VerificacionUsuario;
import com.school.app.services.auth.MarkTokenAsConsumedService;
import com.school.app.services.auth.SearchVerificationTokenService;
import com.school.app.services.user.ActivateUserByTokenVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class VerificacionController {

    private final SearchVerificationTokenService searchVerificationTokenService;
    private final ActivateUserByTokenVerificationService activateUserByTokenVerificationService;
    private final MarkTokenAsConsumedService markTokenAsConsumedService;

    @GetMapping("/activate-account")
    @Transactional
    public ResponseEntity<?> confirmarCuenta(@RequestParam("token") String token) {
        VerificacionUsuario verificacion = searchVerificationTokenService.findTokenVerificationByUser(token);
        if (verificacion.isConsumido())
            return ResponseEntity.badRequest().body("Este token ya ha sido utilizado.");
        if (verificacion.estaExpirado())
            return ResponseEntity.badRequest().body("El enlace de verificación ha expirado.");
        activateUserByTokenVerificationService.activateUserByTokenVerificationMethod(verificacion.getUsuario());
        markTokenAsConsumedService.markTokenAsConsumedMethod(verificacion);
        return ResponseEntity.ok("¡Cuenta activada con éxito! Ya puedes iniciar sesión.");
    }

}
