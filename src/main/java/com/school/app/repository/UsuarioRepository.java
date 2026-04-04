package com.school.app.repository;

import com.school.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>  {

    // Devuelve true si el username ya está en la tabla
    boolean existsByUsername(String username);

    Optional<Usuario> findByEmail(String email);

//    @Query("SELECT u FROM Usuario u " +
//            "JOIN FETCH u.rol r " +
//            "LEFT JOIN FETCH r.permisos " +
//            "WHERE u.username = :username")
//    Optional<Usuario> findByUsernameWithPermissions(@Param("username") String username);

}
