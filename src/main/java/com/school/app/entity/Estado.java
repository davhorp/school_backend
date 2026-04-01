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
@Table(name = "estados")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;
    @Column(name = "nombre_estado", unique = true, nullable = false, length = 100)
    private String nombreEstado;
    @Column(nullable = false, length = 10)
    private String abreviatura;
    @Column(name = "codigo_iso", unique = true, nullable = false, length = 10)
    private String codigoIso;
    private Boolean activo = true;
}
