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
@Table(name = "sesiones_acceso")
public class SesionAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Long idSesion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;
    @Column(name = "token_acceso", unique = true, nullable = false, length = 500)
    private String tokenAcceso;
    @Column(name = "token_regresh", unique = true, nullable = false, length = 500)
    private String tokenRefresh;
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
    @Column(name = "is_revoked", nullable = false)
    private boolean revoked;
    @Column(name = "is_expired", nullable = false)
    private boolean expired;
    @Column(name = "sesion_active", nullable = false)
    private boolean sessionActive;

}
