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
@Table(name = "red_familiar")
public class RedFamiliar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_relacion")
    private Long idRelacion;
    /* * Relación con el Alumno.
     * Usamos LAZY para que Hibernate no traiga todos los datos del alumno
     * automáticamente a menos que los pidamos explícitamente usando .getAlumno()
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
    /* * Relación con la Persona (El familiar/tutor).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona_familiar", nullable = false)
    private Persona familiar;
    @Column(nullable = false, length = 50)
    private String parentesco;
    // Inicializamos en false para que coincida con el DEFAULT FALSE de tu SQL
    @Column(name = "es_tutor_legal", nullable = false)
    private Boolean esTutorLegal = false;
    @Column(name = "es_contacto_emergencia", nullable = false)
    private Boolean esContactoEmergencia = false;
}
