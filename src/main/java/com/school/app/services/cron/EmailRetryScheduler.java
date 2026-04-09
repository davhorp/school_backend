package com.school.app.services.cron;

import com.school.app.entity.EmailQueue;
import com.school.app.repository.EmailQueueRepository;
import com.school.app.services.shipments.email.SendingEmailWithVerificationLinkService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailRetryScheduler {

    private final EmailQueueRepository emailQueueRepository;
    private final SendingEmailWithVerificationLinkService sendingEmailWithVerificationLinkService;

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remitente;
    @Value("${app.admin.email}") // Configura esto en application.properties
    private String adminEmail;


    @Scheduled(cron = "0 */5 * * * *") // Se ejecuta cada 5 minutos
    public void retryFailedEmails() {
        List<EmailQueue> pendingEmails = emailQueueRepository
                .findByStatusAndAttemptsLessThan(EmailQueue.EmailStatus.PENDING, 3);

        for (EmailQueue email : pendingEmails) {
            try {
                // Intento de envío (método simplificado para el ejemplo)
                sendActualEmail(email.getRecipient(), email.getSubject(), email.getContent());

                email.setStatus(EmailQueue.EmailStatus.SENT);
                log.info("RETRY_SUCCESS email_id={} recipient={}", email.getId(), email.getRecipient());
            } catch (Exception e) {
                email.setAttempts(email.getAttempts() + 1);
                email.setLastError(e.getMessage());
                email.setLastAttempt(LocalDateTime.now());

                if (email.getAttempts() >= 3) {
                    email.setStatus(EmailQueue.EmailStatus.FAILED);
                    // ACCIÓN DE ESCALAMIENTO
                    alertAdministrator(email);
                }
            }
            emailQueueRepository.save(email);
        }
    }

    /**
     * Purga correos enviados hace más de 30 días.
     * Se ejecuta todos los días a las 3:00 AM.
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void purgeOldEmails() {
        LocalDateTime limitDate = LocalDateTime.now().minusMonths(1);
        log.info("PURGE_START action=delete_old_emails status=SENT limit_date={}", limitDate);
        try {
            emailQueueRepository.deleteOldEmails(EmailQueue.EmailStatus.SENT, limitDate);
            log.info("PURGE_SUCCESS action=delete_old_emails status=completed");
        } catch (Exception e) {
            log.error("PURGE_ERROR action=delete_old_emails error='{}'", e.getMessage());
        }
    }

    private void alertAdministrator(EmailQueue failedEmail) {
        // 1. Log de Error Crítico (Para sistemas de monitoreo como ELK o Datadog)
        log.error("FATAL_EMAIL_ERROR status=ESCALATED recipient={} subject='{}' attempts=3 error='{}'",
                failedEmail.getRecipient(), failedEmail.getSubject(), failedEmail.getLastError());
        // 2. Notificación vía Email al Administrador
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(adminEmail);
            helper.setSubject("⚠️ ALERTA CRÍTICA: Fallo persistente en envío de correo");
            String body = String.format("""
                <h3>Alerta de Sistema - ERP Escolar</h3>
                <p>Un correo importante ha agotado todos los reintentos y no pudo ser entregado.</p>
                <ul>
                    <li><strong>Destinatario:</strong> %s</li>
                    <li><strong>Asunto:</strong> %s</li>
                    <li><strong>Último Error:</strong> %s</li>
                    <li><strong>Fecha de creación:</strong> %s</li>
                </ul>
                <p>Por favor, revise el estado del servidor SMTP o la tabla <code>email_queue</code>.</p>
                """, failedEmail.getRecipient(), failedEmail.getSubject(),
                    failedEmail.getLastError(), failedEmail.getCreatedAt());
            helper.setText(body, true);
            mailSender.send(message);
            log.info("ADMIN_NOTIFIED status=success admin={}", adminEmail);
        } catch (Exception e) {
            log.error("ADMIN_NOTIFY_FAIL reason='Could not send alert to admin' error='{}'", e.getMessage());
        }
    }

    private void sendActualEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(remitente);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }
}
