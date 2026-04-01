package com.school.app.repository;

import com.school.app.entity.Persona;
import com.school.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, Integer> {
}
