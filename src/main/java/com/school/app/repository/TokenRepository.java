package com.school.app.repository;

import com.school.app.entity.Tokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Tokens, Integer> {

  @Query(value = """
      select t from Tokens t inner join User u on t.user.id = u.id where u.id = :id and (t.isExpired = false or t.isRevoked = false)
      """)
  List<Tokens> findAllValidTokenByUser(Integer id);

  Optional<Tokens> findByToken(String token);
}
