package com.school.app.entity;

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
@Table(name = "verificaciones_usuario")
public class VerificacionUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVerificacion;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    @Column(nullable = false, unique = true)
    private String tokenVerificacion;
    private String tipoVerificacion;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;
    private boolean consumido = false;
    private LocalDateTime fechaVerificacion;

    // Método de utilidad para saber si el token expiró
    public boolean estaExpirado() {
        return LocalDateTime.now().isAfter(this.fechaExpiracion);
    }
}
