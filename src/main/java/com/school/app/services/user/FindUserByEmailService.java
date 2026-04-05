package com.school.app.services.user;

import com.school.app.entity.Usuario;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindUserByEmailService {

    private final UsuarioRepository usuarioRepository;

    public Usuario findUserByEmailMethod(String email){
        return usuarioRepository.findByEmail(
                        email)
                .orElseThrow(
                        () ->
                                new ResourceNotFoundException(String.format("El usuario con correo: %s no existe en el sistema", email)));
    }
}
