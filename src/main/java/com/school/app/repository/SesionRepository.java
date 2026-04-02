package com.school.app.repository;

import com.school.app.entity.SesionAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SesionRepository extends JpaRepository<SesionAcceso, Integer> {
}
