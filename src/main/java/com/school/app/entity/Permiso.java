package com.school.app.entity;

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
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Long idPermiso;
    @Column(name = "nombre_permiso", unique = true, nullable = false, length = 100)
    private String nombrePermiso;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    @Column(length = 50)
    private String modulo;

}
