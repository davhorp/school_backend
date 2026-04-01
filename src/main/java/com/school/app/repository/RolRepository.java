package com.school.app.repository;

import com.school.app.entity.Role;
import com.school.app.enums.RolType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre exacto (útil para asignar roles en el registro).
     * El tipo 'RolType' es el Enum que creamos anteriormente.
     */
    Optional<Role> findByNombreRol(RolType nombreRol);

    /**
     * Verifica si un rol existe por su nombre.
     */
    boolean existsByNombreRol(RolType nombreRol);
}
