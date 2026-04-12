package com.school.app.entity;

import com.school.app.enums.GeneroType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    @Enumerated(EnumType.STRING)
    private GeneroType genero;
    private String telefono;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @OneToOne(mappedBy = "persona")
    private Usuario usuario;
    @Column(unique = true, length = 18)
    private String curp;
    @Column(name = "lugar_nacimiento", length = 100)
    private String lugarNacimiento;
    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;

}
