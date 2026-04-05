package com.school.app.dto;

import com.school.app.dto.requets.PersonRequest;
import com.school.app.entity.Persona;
import com.school.app.entity.Role;
import com.school.app.entity.Usuario;
import com.school.app.enums.GeneroType;
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

    /**
     * Metodo para setear los datos a la entidad.
     *
     * @param dto {@link PersonRequest} objeto, comp par&aacute;metro de entrada.
     * @param person {@link Persona} entidad, comp par&aacute;metro de entrada.
     * @param rol {@link Role} entidad, comp par&aacute;metro de entrada.
     * @param pass Contraseña, comp par&aacute;metro de entrada.
     *
     * @return {@link Usuario} entidad recuperada, en este caso el campo activo esta como false ya que cuando el usuario,
     * verifique su cuenta se pondra en true
     */
    public Usuario toEntityUser(PersonRequest dto, Persona person, Role rol, String pass){
        Usuario usr = new Usuario();
        usr.setPersona(person);
        usr.setActivo(false);
        usr.setEmail(dto.email());
        usr.setUsername(dto.username());
        usr.setPasswordHash(pass);
        usr.setRol(rol);
        usr.setAceptoTerminos(true);
        usr.setBloqueado(false);
        usr.setFotoPerfil(null);
        usr.setIntentosFallidos(0);
        return usr;
    }
}
