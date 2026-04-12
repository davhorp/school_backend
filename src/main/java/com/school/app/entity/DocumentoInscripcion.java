package com.school.app.entity;

import com.school.app.enums.EstadoDocumento;
import com.school.app.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "documentos_inscripcion")
public class DocumentoInscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inscripcion", nullable = false)
    private Inscripcion inscripcion;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipo;
    @Column(name = "nombre_archivo")
    private String nombreArchivo;
    @Column(name = "ruta_archivo", length = 500)
    private String rutaArchivo;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoDocumento estado = EstadoDocumento.PENDIENTE;
    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
    @Column(name = "subido_at")
    private LocalDateTime subidoAt;

    // Helper para actualizar el estado y la fecha al subir el archivo
    public void registrarSubida(String nombre, String ruta) {
        this.nombreArchivo = nombre;
        this.rutaArchivo = ruta;
        this.estado = EstadoDocumento.SUBIDO;
        this.subidoAt = LocalDateTime.now();
    }

}