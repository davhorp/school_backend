package com.school.app.services.auth;

import com.school.app.audit.Auditable;
import com.school.app.dto.requets.AuthRequest;
import com.school.app.dto.response.LoginResponse;
import com.school.app.entity.Usuario;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.exceptions.UserBadCredentialsException;
import com.school.app.exceptions.UserBlockedException;
import com.school.app.exceptions.UserNotActiveInTheSystemException;
import com.school.app.mapper.LoginMapper;
import com.school.app.repository.RolPermisoRepository;
import com.school.app.repository.SesionRepository;
import com.school.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final SesionRepository sesionRepository;
    private final RolPermisoRepository rolPermisoRepository;
    private final LoginMapper loginMapper;

    @Auditable(accion = "INICIAR_SESION")
    public LoginResponse authenticateUser(final String username, final AuthRequest request){
        log.info("AUTH_START action=login email={} status=processing", request.email());
        Usuario usr = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    log.warn("AUTH_FAIL action=login email={} reason='User not found'", request.email());
                    return new ResourceNotFoundException("Usuario no encontrado en el sistema");
                });
        validarEstadoUsuario(usr);
        try {
            log.debug("AUTH_STEP action=login email={} step='Spring Security authentication'", request.email());
            // 2. Autenticar (Spring Security valida el password aquí)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
            // Si tiene éxito, reiniciamos intentos
            usr.setIntentosFallidos(0);
            usuarioRepository.save(usr);
            log.info("AUTH_SUCCESS action=login email={} userId={} status=authenticated", request.email(), usr.getIdUsuario());
        } catch (BadCredentialsException e) {
            log.warn("AUTH_REJECT action=login email={} reason='Invalid credentials'", request.email());
            procesarIntentoFallido(usr);
            throw new UserBadCredentialsException("Credenciales incorrectas");
        }
        // 3. Gestión de Tokens y Sesión
        log.debug("AUTH_STEP action=login email={} step='Generating tokens'", request.email());
        String accessToken = jwtService.generateTokenByUser(usr);
        String refreshToken = jwtService.generateRefreshTokenByUser(usr);
        actualizarSesion(usr, accessToken, refreshToken);
        log.info("AUTH_COMPLETE action=login email={} status=session_created", request.email());
        // 4. Mapeo de Respuesta (Delegado al Mapper)
        return loginMapper.toLoginResponse(usr, accessToken, refreshToken, rolPermisoRepository.findNombresPermisosByUsuario(usr.getIdUsuario().intValue()));
    }

    private void actualizarSesion(Usuario usr, String access, String refresh) {
        log.info("SESSION_UPDATE action=revoke_old_tokens userId={}", usr.getIdUsuario());
        sesionRepository.findAllValidTokenByUser(usr.getIdUsuario().intValue())
                .forEach(token -> {
                    token.setRevoked(true);
                    token.setExpired(true);
                });
        sesionRepository.updateSessionActiveAndTokens(usr.getIdUsuario(), access, refresh);
        log.info("SESSION_UPDATE action=tokens_updated userId={}", usr.getIdUsuario());
    }

    private void validarEstadoUsuario(Usuario usr) {
        if (usr.getBloqueado()) {
            log.warn("AUTH_BLOCK action=login email={} status=blocked", usr.getEmail());
            throw new UserBlockedException("Acceso restringido por seguridad. Contacte al administrador.");
        }
        if (!usr.getActivo()) {
            log.warn("AUTH_BLOCK action=login email={} status=not_active", usr.getEmail());
            throw new UserNotActiveInTheSystemException("Cuenta no verificada.");
        }
    }

    private void procesarIntentoFallido(Usuario usuario) {
        usuario.registrarFallo();
        usuarioRepository.save(usuario);
        log.warn("AUTH_RETRY action=failed_attempt email={} current_attempts={} blocked={}",
                usuario.getEmail(), usuario.getIntentosFallidos(), usuario.getBloqueado());
    }

//    public TokenResponse refreshToken(@NotNull final String authentication) {
//
//        if (authentication == null || !authentication.startsWith("Bearer ")) {
//            throw new IllegalArgumentException("Invalid auth header");
//        }
//        final String refreshToken = authentication.substring(7);
//        final String userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail == null) {
//            return null;
//        }
//
//        final User user = this.repository.findByEmail(userEmail).orElseThrow();
//        final boolean isTokenValid = jwtService.isTokenValid(refreshToken, user);
//        if (!isTokenValid) {
//            return null;
//        }
//
//        final String accessToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, accessToken);
//
//        return new TokenResponse(accessToken, refreshToken);
//    }
}
