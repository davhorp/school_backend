package com.school.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alumno")
    private Long idAlumno;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_persona",
            referencedColumnName = "id_persona",
            nullable = false,
            unique = true
    )
    private Persona persona;
    @Column(name = "matricula", unique = true, length = 50)
    private String matricula;
    @Column(name = "activo", columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean activo = false;
    @OneToOne(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private SaludAlumno salud;
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RedFamiliar> familiares = new ArrayList<>();

}
