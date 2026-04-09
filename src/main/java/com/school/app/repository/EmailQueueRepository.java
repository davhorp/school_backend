package com.school.app.repository;

import com.school.app.entity.AuditLog;
import com.school.app.entity.EmailQueue;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EmailQueueRepository extends JpaRepository<EmailQueue, Long> {

    /**
     * Busca correos filtrando por su estado y que no hayan superado
     * un número máximo de intentos.
     * * @param status El estado del correo (ej. PENDING)
     * @param attempts El límite de intentos (ej. 3)
     * @return Lista de correos pendientes de reintento
     */
    List<EmailQueue> findByStatusAndAttemptsLessThan(EmailQueue.EmailStatus status, int attempts);

    @Modifying
    @Transactional
    @Query("DELETE FROM EmailQueue e WHERE e.status = :status AND e.lastAttempt < :date")
    void deleteOldEmails(EmailQueue.EmailStatus status, LocalDateTime date);

    long countByStatusAndCreatedAtAfter(EmailQueue.EmailStatus status, LocalDateTime date);

    long countByAttemptsGreaterThanEqualAndStatus(int attempts, EmailQueue.EmailStatus status);

}
