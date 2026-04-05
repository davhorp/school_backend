package com.school.app.services.auth;

import com.school.app.dto.requets.AuthRequest;
import com.school.app.dto.response.LoginResponse;
import com.school.app.dto.response.ProfileDetailsResponse;
import com.school.app.entity.*;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.exceptions.UserNotActiveInTheSystemException;
import com.school.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final SesionRepository sesionRepository;
    private final RolPermisoRepository rolPermisoRepository;

    public LoginResponse authenticateUser(final AuthRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Usuario usr = usuarioRepository.findByEmail(
                request.email())
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(String.format("El usuario con correo: %s no existe en el sistema", request.email())));
        if (!usr.getActivo())
            throw new UserNotActiveInTheSystemException(
                    String.format("El usuario: %s no se encuentra activo debido a que la cuenta no ha sido verificada.", usr.getUsername()));
        String accessToken = jwtService.generateTokenByUser(usr);
        String accessTokenRefresh = jwtService.generateRefreshTokenByUser(usr);
        revokeAllUserTokens(usr);
        sesionRepository.updateSessionActiveAndTokens(usr.getIdUsuario(), accessToken, accessTokenRefresh);
        return new LoginResponse(
                accessToken,
                accessTokenRefresh,
                usr.getPersona().getNombre().concat(" ").concat(usr.getPersona().getApellidoPaterno().concat(" ").concat(usr.getPersona().getApellidoMaterno())),
                new ProfileDetailsResponse(
                        usr.getRol().getNombreRol().name().toLowerCase(),
                        rolPermisoRepository.findNombresPermisosByUsuario(usr.getIdUsuario().intValue())
                ),
                usr.getUsername(),
                usr.getActivo(),
                usr.getIdUsuario());
    }

    private void revokeAllUserTokens(final Usuario user) {
        final List<SesionAcceso> validUserTokens = sesionRepository.findAllValidTokenByUser(user.getIdUsuario().intValue());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setRevoked(false);//validar esto
                token.setExpired(false);//validar esto
            });
            sesionRepository.saveAll(validUserTokens);
        }
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
