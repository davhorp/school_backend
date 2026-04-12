package com.school.app.entity;

import com.school.app.enums.EstadoInscripcion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inscripcion")
    private Long idInscripcion;
    // Relación con la tabla Alumnos (FK id_alumno)
    // Usamos LAZY para no cargar al alumno entero de la BD a menos que se pida explícitamente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
    // Relación con la tabla Personas (FK id_tutor_responsable)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tutor_responsable", nullable = false)
    private Persona tutorResponsable;
    @Column(name = "ciclo_escolar", nullable = false, length = 20)
    private String cicloEscolar;
    @Column(name = "nivel_educativo", length = 50)
    private String nivelEducativo;
    @Column(length = 20)
    private String grado;
    @Column(name = "cct_procedencia", length = 20)
    private String cctProcedencia;
    /* * EL ESTADO CLAVE (Manejo avanzado de ENUM en Postgres)
     * @JdbcTypeCode(SqlTypes.NAMED_ENUM) es la forma nativa de Hibernate 6
     * para mapear un tipo CREATE TYPE enum de Postgres directamente a un Enum de Java.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "estado_inscripcion DEFAULT 'Borrador'")
    @Builder.Default // Le dice a Lombok que respete este valor por defecto si usamos el Builder
    private EstadoInscripcion estado = EstadoInscripcion.Borrador;
    // DECIMAL(5,2) en BD -> BigDecimal en Java para no perder precisión
    @Column(name = "porcentaje_completado", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal porcentajeCompletado = BigDecimal.ZERO;
    // @CreationTimestamp hace que Hibernate llene este campo automáticamente al hacer el INSERT
    @CreationTimestamp
    @Column(name = "fecha_inicio", updatable = false)
    private LocalDateTime fechaInicio;
    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;
    @Column(name = "observaciones_admin", columnDefinition = "TEXT")
    private String observacionesAdmin;
    // Relación con los documentos (Bidireccional para facilitar guardado en cascada)
    @OneToMany(mappedBy = "inscripcion", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DocumentoInscripcion> documentos = new ArrayList<>();

    // Helper para añadir documentos fácilmente y mantener sincronizada la relación bidireccional
    public void agregarDocumento(DocumentoInscripcion documento) {
        documentos.add(documento);
        documento.setInscripcion(this);
    }

}
