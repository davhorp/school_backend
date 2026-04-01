package com.school.app.dto;

import com.school.app.dto.requets.PersonRequest;
import com.school.app.entity.Persona;
import com.school.app.entity.Role;
import com.school.app.entity.Usuario;
import com.school.app.enums.GeneroType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PersonaDTO {

    public Persona toEntity(PersonRequest dto) {
        if (dto == null) return null;
        Persona persona = new Persona();
        persona.setFechaNacimiento(dto.fechaNacimiento());
        persona.setTelefono(dto.telefono());
        // Conversión manual de String a Enum
        if (dto.genero() != null) {
            persona.setGenero(GeneroType.valueOf(dto.genero().toUpperCase()));
        }
        // Lógica de división de nombre completo
//        String[] partes = dto.nombreCompleto().split(" ");
//        if (partes.length > 0) persona.setNombre(partes[0]);
//        if (partes.length > 1) persona.setApellidoPaterno(partes[1]);
//        if (partes.length > 2) {
//            // Unir el resto si hay más de 3 partes como apellido materno
//            persona.setApellidoMaterno(partes[2]);
//        }
        persona.setNombre(dto.nombre());
        persona.setApellidoMaterno(dto.apellidoMaterno());
        persona.setApellidoPaterno(dto.apellidoPaterno());
        persona.setCreatedAt(LocalDateTime.now());
        return persona;
    }

    public Usuario toEntityUser(PersonRequest dto, Persona person, Role rol, String pass){
        Usuario usr = new Usuario();
        usr.setPersona(person);
        usr.setActivo(true);
        usr.setEmail(dto.email());
        usr.setUsername(dto.username());
        usr.setPasswordHash(pass);
        usr.setRol(rol);
        usr.setAceptoTerminos(true);
        return usr;
    }
}
