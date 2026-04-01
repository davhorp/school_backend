package com.school.app.dto.requets;

import java.time.LocalDate;

public record PersonRequest(
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String telefono,
        String genero,
        LocalDate fechaNacimiento) {
}
