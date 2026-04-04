package com.school.app.repository;

import com.school.app.entity.SesionAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionRepository extends JpaRepository<SesionAcceso, Integer> {

    Optional<SesionAcceso> findByTokenAcceso(String tokenAcceso);

    @Query(value = """
      select s from SesionAcceso s inner join Usuario u on s.usuario.id = u.idUsuario where u.idUsuario = :id and (s.expired = false or s.revoked = false)
      """)
    List<SesionAcceso> findAllValidTokenByUser(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE SesionAcceso s SET s.sessionActive = true, s.tokenAcceso = :tokenAccess, s.tokenRefresh = :tokenAccessRefresh WHERE s.usuario.idUsuario = :idUsuario")
    void updateSessionActiveAndTokens(Long idUsuario, String tokenAccess, String tokenAccessRefresh);
}
