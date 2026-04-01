package com.school.app.entity;

import com.school.app.enums.RolType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para manejar el SERIAL de Postgres
    @Column(name = "id_rol")
    private Long idRol;
    @Enumerated(EnumType.STRING) // Mapea el enum como String en la base de datos
    @Column(name = "nombre_rol", nullable = false)
    private RolType nombreRol;
    @Column(name = "descripcion", length = 255)
    private String descripcion;

}
