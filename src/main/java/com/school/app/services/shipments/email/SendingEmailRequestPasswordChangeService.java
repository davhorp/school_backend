package com.school.app.services.shipments.email;

import com.school.app.entity.Usuario;
import com.school.app.utils.UtilsMethodsSchool;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendingEmailRequestPasswordChangeService {

    private final JavaMailSender mailSender;
    private final UtilsMethodsSchool utilsMethodsSchool;

    @Value("${app.frontend.url}")
    private String frontendUrl;
    @Value("${spring.mail.username}")
    private String remitente;

    //@Async
    public void sendingEmailRequestPasswordChangeMethod(Usuario usr, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(usr.getEmail());
            helper.setSubject("Código de recuperación de contraseña - Sistema Escolar");
            String contenidoHtml = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #eee; padding: 20px;">
                    <h2 style="color: #1976d2; text-align: center;">Cambio de Contraseña en el sistema ERP Escolar</h2>
                    <p>Hola %s,</p>
                    <p>Has solicitado cambio de contraseña en nuestra plataforma. Tu código de verificación para cambiar tu contraseña es: </p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a style="background-color: #1976d2; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            %s
                        </a>
                    </div>
                    <p>Este código expirará en 10 minutos por tu seguridad.</p>
                    <hr style="border: 0; border-top: 1px solid #eee;">
                    <p style="font-size: 12px; color: #888; text-align: center;">Si no solicitaste este proceso, por favor ignora este correo.</p>
                </div>
                """.formatted(utilsMethodsSchool.getNameFullUser(usr),code);
            helper.setText(contenidoHtml, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new com.school.app.exceptions.MessagingException("Error al enviar email: " + e.getMessage());
        }
    }

}
