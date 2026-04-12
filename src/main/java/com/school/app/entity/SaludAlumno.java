package com.school.app.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "salud_alumnos")
public class SaludAlumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_salud")
    private Long idSalud;
    /* * RELACIÓN 1 A 1 CON ALUMNO
     * FetchType.LAZY: Mejora el rendimiento, no carga el alumno de BD a menos que lo pidas.
     * @ToString.Exclude: Evita un ciclo infinito (StackOverflow) si Alumno también hace referencia a SaludAlumno.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", referencedColumnName = "id_alumno", nullable = false, unique = true)
    @ToString.Exclude
    private Alumno alumno;
    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;
    @Column(name = "necesidades_especiales", columnDefinition = "TEXT")
    private String necesidadesEspeciales;
    @Column(name = "discapacidades", columnDefinition = "TEXT")
    private String discapacidades;
    @Column(name = "notas_medicas", columnDefinition = "TEXT")
    private String notasMedicas;
}
