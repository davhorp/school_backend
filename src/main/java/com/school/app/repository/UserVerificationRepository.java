package com.school.app.repository;

import com.school.app.entity.VerificacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserVerificationRepository extends JpaRepository<VerificacionUsuario, Long> {

    Optional<VerificacionUsuario> findByTokenVerificacion(String tokenVerificacion);
}
