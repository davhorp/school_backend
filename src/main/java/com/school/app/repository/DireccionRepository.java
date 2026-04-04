package com.school.app.repository;

import com.school.app.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Long> {

    /**
     * Opción A: Por nombre de método (Query Method).
     * Navega: Direccion -> Persona -> Usuario (buscando por id_usuario).
     */
    List<Direccion> findByPersona_Usuario_IdUsuario(Integer idUsuario);

    /**
     * Opción B: JPQL con JOIN FETCH.
     * Esta es la más recomendada para tu sistema escolar porque trae
     * toda la información de la dirección en un solo viaje a Postgres.
     */
    @Query("SELECT d FROM Direccion d " +
            "JOIN d.persona p " +
            "JOIN p.usuario u " +
            "WHERE u.idUsuario = :idUsuario")
    List<Direccion> findAllByUsuarioId(@Param("idUsuario") Integer idUsuario);

    @Query("SELECT d FROM Direccion d JOIN d.persona p JOIN p.usuario u " +
            "WHERE u.idUsuario = :idUsuario AND d.tipo = 'Hogar'")
    Optional<Direccion> findHomeAddressByUsuarioId(@Param("idUsuario") Integer idUsuario);

    /**
     * Opción C: Buscar por el Username (muy útil para el perfil del usuario logueado).
     */
    @Query("SELECT d FROM Direccion d " +
            "JOIN d.persona p " +
            "JOIN p.usuario u " +
            "WHERE u.username = :username")
    List<Direccion> findAllByUsername(@Param("username") String username);

}
