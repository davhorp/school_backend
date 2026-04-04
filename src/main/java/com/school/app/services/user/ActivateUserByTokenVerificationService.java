package com.school.app.services.user;

import com.school.app.entity.Usuario;
import com.school.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivateUserByTokenVerificationService {

    private final UsuarioRepository usuarioRepository;

    public void activateUserByTokenVerificationMethod(Usuario usr){
        usr.setActivo(true);
        usuarioRepository.save(usr);
    }
}
