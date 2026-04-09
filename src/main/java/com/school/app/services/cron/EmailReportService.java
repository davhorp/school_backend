package com.school.app.services.cron;

import com.school.app.entity.EmailQueue;
import com.school.app.repository.EmailQueueRepository;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailReportService {

    private final EmailQueueRepository emailQueueRepository;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    @Value("${app.admin.email}")
    private String adminEmail;

    /**
     * Envía un reporte de métricas cada Lunes a las 8:00 AM.
     */
    @Scheduled(cron = "0 0 8 * * MON")
    public void sendWeeklyStatsReport() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        long sent = emailQueueRepository.countByStatusAndCreatedAtAfter(EmailQueue.EmailStatus.SENT, sevenDaysAgo);
        long failed = emailQueueRepository.countByStatusAndCreatedAtAfter(EmailQueue.EmailStatus.FAILED, sevenDaysAgo);
        long pending = emailQueueRepository.countByStatusAndCreatedAtAfter(EmailQueue.EmailStatus.PENDING, sevenDaysAgo);
        long total = sent + failed + pending;
        double successRate = total > 0 ? (sent * 100.0 / total) : 0;
        log.info("REPORT_GEN action=weekly_stats sent={} failed={} success_rate={}%", sent, failed, successRate);
        try {
            sendEmailReport(sent, failed, pending, total, successRate);
        } catch (Exception e) {
            log.error("REPORT_ERROR reason='Could not send weekly report' error='{}'", e.getMessage());
        }
    }

    private void sendEmailReport(long sent, long failed, long pending, long total, double rate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(remitente);
        helper.setTo(adminEmail);
        helper.setSubject("📊 Reporte Semanal: Desempeño de Notificaciones");
        String colorRate = rate > 95 ? "#2e7d32" : "#d32f2f"; // Verde si es > 95%, rojo si es menor
        String body = """
            <div style="font-family: sans-serif; color: #333; max-width: 500px;">
                <h2 style="color: #1976d2;">Resumen de Actividad de Correo</h2>
                <p>Métricas de los últimos 7 días:</p>
                <table style="width: 100%%; border-collapse: collapse;">
                    <tr style="background: #f5f5f5;">
                        <td style="padding: 10px; border: 1px solid #ddd;">Total Procesados</td>
                        <td style="padding: 10px; border: 1px solid #ddd; font-weight: bold;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border: 1px solid #ddd;">Enviados con Éxito</td>
                        <td style="padding: 10px; border: 1px solid #ddd; color: #2e7d32;">%d</td>
                    </tr>
                    <tr>
                        <td style="padding: 10px; border: 1px solid #ddd;">Fallidos (Agotados)</td>
                        <td style="padding: 10px; border: 1px solid #ddd; color: #d32f2f;">%d</td>
                    </tr>
                    <tr style="background: #f5f5f5;">
                        <td style="padding: 10px; border: 1px solid #ddd;">Pendientes/Reintentando</td>
                        <td style="padding: 10px; border: 1px solid #ddd;">%d</td>
                    </tr>
                </table>
                <div style="margin-top: 20px; padding: 15px; background: %s; color: white; border-radius: 5px; text-align: center;">
                    <span style="font-size: 14px;">Tasa de Entrega Global:</span><br>
                    <strong style="font-size: 24px;">%.2f%%</strong>
                </div>
                <p style="font-size: 12px; color: #777; margin-top: 20px;">
                    Este reporte es generado automáticamente por el módulo de monitoreo de Auditoría.
                </p>
            </div>
            """.formatted(total, sent, failed, pending, colorRate, rate);

        helper.setText(body, true);
        mailSender.send(message);
    }

}
