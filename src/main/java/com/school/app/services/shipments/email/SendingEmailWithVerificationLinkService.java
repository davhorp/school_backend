package com.school.app.services.shipments.email;

import com.school.app.entity.EmailQueue;
import com.school.app.entity.Usuario;
import com.school.app.repository.EmailQueueRepository;
import com.school.app.utils.UtilsMethodsSchool;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendingEmailWithVerificationLinkService {

    private final JavaMailSender mailSender;
    private final EmailQueueRepository emailQueueRepository;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String remitente;

    @Async("auditExecutor")
    public void sendVerificationLink(Usuario usr, String token) {
        String recipient = usr.getEmail();
        String subject = "Activa tu cuenta - Sistema Escolar";
        String content = buildHtmlTemplate(usr.getNombreCompleto(), token);

        try {
            sendEmail(recipient, subject, content);
            log.info("EMAIL_SENT action=send_verification_link recipient={}", recipient);
        } catch (Exception e) {
            log.error("EMAIL_INITIAL_FAIL action=send_verification_link recipient={} reason='{}'", recipient, e.getMessage());
            // Guardar en la cola para reintento automático
            emailQueueRepository.save(EmailQueue.builder()
                    .recipient(recipient)
                    .subject(subject)
                    .content(content)
                    .attempts(1)
                    .status(EmailQueue.EmailStatus.PENDING)
                    .lastError(e.getMessage())
                    .lastAttempt(LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build());
        }
    }

    private void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(remitente);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(message);
    }

    private String buildHtmlTemplate(String name, String token) {
        String urlVerificacion = String.format("%s/verify-account-user?token=%s", frontendUrl, token);
        return """
            <div style="font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; max-width: 600px; margin: auto; border: 1px solid #f0f0f0; padding: 40px; border-radius: 10px;">
                <div style="text-align: center; margin-bottom: 30px;">
                    <h2 style="color: #1976d2; margin: 0;">¡Bienvenido al ERP Escolar!</h2>
                </div>
                <p style="color: #333; font-size: 16px;">Hola <strong>%s</strong>,</p>
                <p style="color: #555; line-height: 1.6;">Gracias por registrarte. Para activar todas las funciones de tu cuenta (calificaciones, trámites y perfil), confirma tu identidad haciendo clic en el siguiente botón:</p>
                <div style="text-align: center; margin: 40px 0;">
                    <a href="%s" style="background-color: #1976d2; color: #ffffff; padding: 15px 35px; text-decoration: none; border-radius: 5px; font-weight: bold; font-size: 16px; display: inline-block; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
                        Confirmar Cuenta Ahora
                    </a>
                </div>
                <p style="font-size: 13px; color: #777; background: #f9f9f9; padding: 15px; border-radius: 5px;">
                    <strong>Nota:</strong> Este enlace tiene una validez de 24 horas por motivos de seguridad.
                </p>
                <hr style="border: 0; border-top: 1px solid #eee; margin: 30px 0;">
                <p style="font-size: 11px; color: #aaa; text-align: center; line-height: 1.4;">
                    Este es un mensaje automático del Sistema de Gestión Escolar.<br>
                    Si no reconoces esta actividad, por favor ignora este mensaje.
                </p>
            </div>
            """.formatted(name, urlVerificacion);
    }

}
