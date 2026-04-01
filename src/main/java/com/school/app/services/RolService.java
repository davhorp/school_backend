package com.school.app.services;

import com.school.app.entity.Estado;
import com.school.app.entity.Role;
import com.school.app.enums.RolType;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    public Role getRolByName(RolType rol) {
        return rolRepository.findByNombreRol(rol)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("El Rol: %s no existe en el sistema", rol.toString())));
    }
}
