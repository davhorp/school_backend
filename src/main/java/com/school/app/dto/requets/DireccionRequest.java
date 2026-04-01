package com.school.app.dto.requets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DireccionRequest(
        @NotBlank(message = "El campo Calle, no puede ser un valor nulo")
        String calle,
        @NotBlank(message = "El campo Numero exterior, no puede ser un valor nulo")
        String numExt,
        String numInt,
        @NotBlank(message = "El campo Colonia, no puede ser un valor nulo")
        String colonia,
        @NotBlank(message = "El campo Codigo postal, no puede ser un valor nulo")
        @Pattern(regexp = "^\\+?[0-9]{5,10}$", message = "Invalido, el formato del Codigo postal")
        String cp,
        String referencia,
        @NotBlank(message = "El campo Tipo direccion, no puede ser un valor nulo")
        String tipoDireccion,
        @NotBlank(message = "El campo Codigo de Estado, no puede ser un valor nulo")
        String codeEdo
) {
}
