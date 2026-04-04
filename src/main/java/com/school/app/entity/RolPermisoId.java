package com.school.app.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class RolPermisoId {

    private Integer idRol;
    private Integer idPermiso;
    private Integer idUsuario;

}
