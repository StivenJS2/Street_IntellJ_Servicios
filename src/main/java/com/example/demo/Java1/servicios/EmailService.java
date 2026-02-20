package com.example.demo.Java1.servicios;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailRecuperacion(String emailDestino, String token, String tipoUsuario) throws MessagingException {
        String enlace = "http://localhost:8000/restablecer-contrasena?token=" + token;

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setTo(emailDestino);
        helper.setSubject("Recuperación de contraseña - Urban Street");
        helper.setText(construirHtml(enlace, tipoUsuario), true); // true = es HTML

        mailSender.send(mensaje);
    }

    private String construirHtml(String enlace, String tipoUsuario) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head><meta charset='UTF-8'><meta name='viewport' content='width=device-width, initial-scale=1.0'></head>" +
                "<body style='margin:0;padding:0;background-color:#0c1218;font-family:Segoe UI,Tahoma,Geneva,Verdana,sans-serif;'>" +

                "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#0c1218;padding:40px 0;'>" +
                "<tr><td align='center'>" +

                // Card principal
                "<table width='500' cellpadding='0' cellspacing='0' style='background-color:#1a2332;border-radius:16px;overflow:hidden;box-shadow:0 20px 60px rgba(0,0,0,0.5);'>" +

                // Header
                "<tr><td align='center' style='padding:40px 40px 30px 40px;border-bottom:1px solid #2a3a4a;'>" +
                "<div style='background-color:#0c1218;border-radius:50%;width:70px;height:70px;display:inline-block;text-align:center;line-height:70px;margin-bottom:16px;'>" +
                "<span style='font-size:32px;'>&#128272;</span>" +
                "</div>" +
                "<h1 style='color:#ffffff;font-size:24px;font-weight:700;margin:0 0 8px 0;'>Recuperar Contrase&ntilde;a</h1>" +
                "<p style='color:#8a9ab0;font-size:14px;margin:0;'>Urban Street &mdash; Cuenta " + tipoUsuario + "</p>" +
                "</td></tr>" +

                // Cuerpo
                "<tr><td style='padding:36px 40px;'>" +
                "<p style='color:#c8d6e5;font-size:15px;line-height:1.7;margin:0 0 20px 0;'>" +
                "Hola, recibimos una solicitud para restablecer la contrase&ntilde;a de tu cuenta. " +
                "Haz clic en el bot&oacute;n de abajo para crear una nueva contrase&ntilde;a." +
                "</p>" +

                // Botón
                "<table width='100%' cellpadding='0' cellspacing='0'>" +
                "<tr><td align='center' style='padding:10px 0 24px 0;'>" +
                "<a href='" + enlace + "' style='display:inline-block;background-color:#3b82f6;color:#ffffff;text-decoration:none;font-size:15px;font-weight:600;padding:14px 36px;border-radius:8px;letter-spacing:0.5px;'>" +
                "Restablecer Contrase&ntilde;a" +
                "</a>" +
                "</td></tr></table>" +

                // Separador
                "<hr style='border:none;border-top:1px solid #2a3a4a;margin:4px 0 24px 0;'>" +

                // Advertencia
                "<table width='100%' cellpadding='0' cellspacing='0' style='background-color:#0c1218;border-radius:8px;border-left:3px solid #3b82f6;'>" +
                "<tr><td style='padding:16px;'>" +
                "<p style='color:#8a9ab0;font-size:13px;margin:0;line-height:1.6;'>" +
                "&#9201; Este enlace expirar&aacute; en <strong style='color:#c8d6e5;'>1 hora</strong>.<br>" +
                "Si no solicitaste este cambio, puedes ignorar este correo con seguridad." +
                "</p>" +
                "</td></tr></table>" +

                // Enlace alternativo
                "<p style='color:#8a9ab0;font-size:12px;margin:24px 0 0 0;line-height:1.6;'>" +
                "Si el bot&oacute;n no funciona, copia y pega este enlace en tu navegador:<br>" +
                "<a href='" + enlace + "' style='color:#3b82f6;word-break:break-all;'>" + enlace + "</a>" +
                "</p>" +
                "</td></tr>" +

                // Footer
                "<tr><td align='center' style='padding:24px 40px;border-top:1px solid #2a3a4a;background-color:#0f1923;'>" +
                "<p style='color:#4a5a6a;font-size:12px;margin:0;'>&copy; 2025 Urban Street &middot; Todos los derechos reservados</p>" +
                "</td></tr>" +

                "</table>" +
                "</td></tr></table>" +
                "</body></html>";
    }
}