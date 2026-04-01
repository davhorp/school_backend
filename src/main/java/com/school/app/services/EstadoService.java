package com.school.app.services;

import com.school.app.entity.Estado;
import com.school.app.exceptions.ResourceNotFoundException;
import com.school.app.repository.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public Estado getEdoByCodeISO(String codeISO) {
        return estadoRepository.findByCodigoIso(codeISO)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("El estado con Codigo ISO: %s no existe en el sistema", codeISO)));
    }
}
