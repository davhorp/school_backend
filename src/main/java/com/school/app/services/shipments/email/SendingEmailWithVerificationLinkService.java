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
public class SendingEmailWithVerificationLinkService {

    private final JavaMailSender mailSender;
    private final UtilsMethodsSchool utilsMethodsSchool;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String remitente;

    @Async
    public void sendVerificationLink(Usuario usr, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(remitente);
            helper.setTo(usr.getEmail());
            helper.setSubject("Activa tu cuenta - Sistema Escolar");
            // Construimos el enlace que apunta a tu componente de Angular
            String urlVerificacion = frontendUrl + "/verify-account-user?token=" + token;
            String contenidoHtml = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #eee; padding: 20px;">
                    <h2 style="color: #1976d2; text-align: center;">¡Bienvenido al ERP Escolar!</h2>
                    <p>Hola %s,</p>
                    <p>Has sido registrado en nuestra plataforma. Para poder acceder a tus calificaciones y trámites, es necesario que confirmes tu cuenta haciendo clic en el siguiente botón:</p>
                    <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="background-color: #1976d2; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                            Confirmar mi cuenta
                        </a>
                    </div>
                    <p>Este enlace expirará en 24 horas.</p>
                    <hr style="border: 0; border-top: 1px solid #eee;">
                    <p style="font-size: 12px; color: #888; text-align: center;">Si no solicitaste este registro, por favor ignora este correo.</p>
                </div>
                """.formatted(utilsMethodsSchool.getNameFullUser(usr), urlVerificacion);
            helper.setText(contenidoHtml, true); // true indica que es HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            // Loguear el error para monitoreo
            throw new com.school.app.exceptions.MessagingException("Error al enviar email: " + e.getMessage());
        }
    }

}
