package com.school.app.repository;

import com.school.app.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    /**
     * Recupera solo los estados que están marcados como activos.
     * Útil para llenar dropdowns en Angular sin mostrar estados obsoletos.
     */
    List<Estado> findByActivoTrueOrderByNombreEstadoAsc();

    /**
     * Busca un estado por su código ISO (ej. 'MX-MEX').
     */
    Optional<Estado> findByCodigoIso(String codigoIso);

    /**
     * Busca por nombre exacto.
     */
    Optional<Estado> findByNombreEstado(String nombreEstado);

    /**
     * Verifica si existe un estado con una abreviatura específica.
     */
    boolean existsByAbreviatura(String abreviatura);
}
