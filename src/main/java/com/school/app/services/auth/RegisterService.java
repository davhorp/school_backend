package com.school.app.services.auth;

import com.school.app.dto.DireccionDTO;
import com.school.app.dto.PersonaDTO;
import com.school.app.dto.requets.PersonRequest;
import com.school.app.dto.response.RegisterPersonResponse;
import com.school.app.entity.Persona;
import com.school.app.entity.Usuario;
import com.school.app.enums.RolType;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.DireccionRepository;
import com.school.app.repository.PersonaRepository;
import com.school.app.repository.UsuarioRepository;
import com.school.app.services.EstadoService;
import com.school.app.services.sessions.SesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final PersonaRepository personaRepository;
    private final DireccionRepository direccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EstadoService estadoService;
    private final RolService rolService;
    private final SesionService sesionService;

    @Transactional(rollbackFor = {InvocationTargetException.class, ResourceNotFoundException.class})
    public RegisterPersonResponse registerUser(PersonRequest request){
        //throw new ResourceAlreadyExistsException("DEMO", "DEMO");
        try {
            PersonaDTO personaDTO = new PersonaDTO();
            Persona person = personaRepository.save(personaDTO.toEntity(request));
            direccionRepository.save(new DireccionDTO().toEntity(
                            request.direccion(),
                            person,
                            estadoService.getEdoByCodeISO(request.direccion().codeEdo())));
            Usuario usr = personaDTO.toEntityUser(
                    request,
                    person,
                    rolService.getRolByName(RolType.valueOf(request.rol())),
                    passwordEncoder.encode(request.password())
            );
            usuarioRepository.save(usr);
            sesionService.saveSession(usr);
        }catch (IllegalArgumentException illegalArgumentException){
            throw new IllegalArgumentException(String.format("Error: %s", illegalArgumentException.getMessage()));
        }
        return new RegisterPersonResponse("00", "Success");
    }

}
