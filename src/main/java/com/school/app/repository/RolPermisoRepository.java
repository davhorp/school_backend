package com.school.app.repository;

import com.school.app.entity.RolPermiso;
import com.school.app.entity.RolPermisoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RolPermisoRepository extends JpaRepository<RolPermiso, RolPermisoId> {

    /**
     * 1. BUSCAR: Obtiene todos los registros de la tabla intermedia para un usuario.
     * Usamos JOIN FETCH para traer la entidad 'Permiso' completa en una sola consulta SQL.
     */
    @Query("SELECT rp FROM RolPermiso rp " +
            "JOIN FETCH rp.permiso " +
            "WHERE rp.id.idUsuario = :idUsuario")
    List<RolPermiso> findAllByUsuarioId(@Param("idUsuario") Integer idUsuario);

    /**
     * 2. BORRAR: Elimina todas las asignaciones de permisos de un usuario específico.
     * Requiere @Modifying porque es una operación de escritura (DELETE).
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RolPermiso rp WHERE rp.id.idUsuario = :idUsuario")
    void deleteAllByUsuarioId(@Param("idUsuario") Integer idUsuario);

    /**
     * Consulta todos los registros de la tabla intermedia para un usuario,
     * cargando de inmediato la información del Permiso asociado.
     */
    @Query("SELECT rp FROM RolPermiso rp " +
            "JOIN FETCH rp.permiso " +
            "WHERE rp.usuario .idUsuario = :idUsuario")
    List<RolPermiso> findAllPermisosByUsuario(@Param("idUsuario") Integer idUsuario);

    /**
     * Si solo necesitas los nombres de los permisos (String) para Spring Security:
     */
    @Query("SELECT rp.permiso.nombrePermiso FROM RolPermiso rp " +
            "WHERE rp.usuario.idUsuario = :idUsuario")
    List<String> findNombresPermisosByUsuario(@Param("idUsuario") Integer idUsuario);

}
