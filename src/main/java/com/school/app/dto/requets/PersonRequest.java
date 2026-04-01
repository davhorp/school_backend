package com.school.app.dto.requets;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PersonRequest(
        @NotBlank(message = "El campo Nombre, no puede ser un valor nulo")
        String nombre,
        @NotBlank(message = "El campo Apellido paterno, no puede ser un valor nulo")
        String apellidoPaterno,
        @NotBlank(message = "El campo Apellido Materno, no puede ser un valor nulo")
        String apellidoMaterno,
        @NotBlank(message = "El campo Telefono, no puede ser un valor nulo")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalido, el formato del numero telefonico")
        String telefono,
        @NotBlank(message = "El campo Genero, no puede ser un valor nulo")
        String genero,
        @NotNull(message = "El campo Fecha de nacimiento, no puede ser un valor nulo")
        LocalDate fechaNacimiento,
        DireccionRequest direccion,
        @NotBlank(message = "El campo Email, no puede ser un valor nulo")
        @Email(message = "Invalido, formato del E-Mail")
        String email,
        @NotBlank(message = "El campo Nombre de usuario, no puede ser un valor nulo")
        String username,
        @NotBlank(message = "El campo Contraseña, no puede ser un valor nulo")
        String password,
        @NotBlank(message = "El campo Rol, no puede ser un valor nulo")
        String rol
    ) {
}
