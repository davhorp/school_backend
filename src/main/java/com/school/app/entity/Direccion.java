package com.school.app.entity;

import com.school.app.enums.DireccionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direcciones")
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Integer idDireccion;
    @Column(nullable = false, length = 150)
    private String calle;
    @Column(name = "num_ext", length = 20)
    private String numExt;
    @Column(name = "num_int", length = 20)
    private String numInt;
    @Column(length = 100)
    private String colonia;
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;
    @Column(columnDefinition = "TEXT")
    private String referencia;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 15)
    private DireccionType tipo;
    // Relación Muchos a Uno con la entidad Persona
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;
    @ManyToOne(fetch = FetchType.LAZY) // Generalmente queremos saber el estado de inmediato
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
}
