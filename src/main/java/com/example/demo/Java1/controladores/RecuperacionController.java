package com.example.demo.Java1.controladores;

import com.example.demo.Java1.servicios.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/recuperacion")
@CrossOrigin(origins = "*")
public class RecuperacionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // =============================================
    // PASO 1: Solicitar recuperación (genera token y envía email)
    // =============================================
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitar(@RequestBody Map<String, String> body) {
        String correo = body.get("correo_electronico");

        // Buscar primero en clientes
        try {
            Map<String, Object> cliente = jdbcTemplate.queryForMap(
                    "SELECT id_cliente FROM cliente WHERE correo_electronico = ?", correo
            );
            enviarToken(correo, "cliente");
            return ResponseEntity.ok(Map.of("mensaje", "Si el correo existe, recibirás un enlace."));
        } catch (Exception ignored) {}

        // Buscar en vendedores
        try {
            Map<String, Object> vendedor = jdbcTemplate.queryForMap(
                    "SELECT id_vendedor FROM vendedor WHERE correo_electronico = ?", correo
            );
            enviarToken(correo, "vendedor");
            return ResponseEntity.ok(Map.of("mensaje", "Si el correo existe, recibirás un enlace."));
        } catch (Exception ignored) {}

        // Siempre respondemos lo mismo por seguridad (no confirmar si el correo existe)
        return ResponseEntity.ok(Map.of("mensaje", "Si el correo existe, recibirás un enlace."));
    }

    // =============================================
    // PASO 2: Restablecer contraseña con el token
    // =============================================
    @PostMapping("/restablecer")
    public ResponseEntity<?> restablecer(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String nuevaContrasena = body.get("contrasena");

        System.out.println("Token recibido: " + token); // 👈

        try {
            Map<String, Object> cliente = jdbcTemplate.queryForMap(
                    "SELECT id_cliente, reset_token_expira FROM cliente WHERE reset_token = ?", token
            );

            System.out.println("Cliente encontrado: " + cliente); // 👈

            LocalDateTime expira = (LocalDateTime) cliente.get("reset_token_expira");
            System.out.println("Expira: " + expira); // 👈
            System.out.println("Ahora: " + LocalDateTime.now()); // 👈

            if (expira.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token expirado."));
            }

            // Actualizar contraseña y limpiar token
            jdbcTemplate.update(
                    "UPDATE cliente SET contrasena = ?, reset_token = NULL, reset_token_expira = NULL WHERE reset_token = ?",
                    passwordEncoder.encode(nuevaContrasena), token
            );

            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente."));

        } catch (Exception e) {
            System.out.println("Error buscando cliente: " + e.getMessage()); // 👈
        }

        // Buscar token en vendedores
        try {
            Map<String, Object> vendedor = jdbcTemplate.queryForMap(
                    "SELECT id_vendedor, reset_token_expira FROM vendedor WHERE reset_token = ?", token
            );

            // Verificar expiración
            LocalDateTime expira = (LocalDateTime) vendedor.get("reset_token_expira");
            if (expira.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body(Map.of("error", "Token expirado."));
            }

            // Actualizar contraseña y limpiar token
            jdbcTemplate.update(
                    "UPDATE vendedor SET contrasena = ?, reset_token = NULL, reset_token_expira = NULL WHERE reset_token = ?",
                    passwordEncoder.encode(nuevaContrasena), token
            );

            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada correctamente."));

        } catch (Exception ignored) {}

        return ResponseEntity.badRequest().body(Map.of("error", "Token inválido o expirado."));
    }

    // =============================================
    // MÉTODO PRIVADO: Generar token y guardar en BD
    // =============================================
    private void enviarToken(String correo, String tabla) throws MessagingException {
        String token = UUID.randomUUID().toString();
        LocalDateTime expira = LocalDateTime.now().plusHours(1);

        String idColumna = tabla.equals("cliente") ? "id_cliente" : "id_vendedor";
        String tipoUsuario = tabla.equals("cliente") ? "Cliente" : "Administrador";

        jdbcTemplate.update(
                "UPDATE " + tabla + " SET reset_token = ?, reset_token_expira = ? WHERE correo_electronico = ?",
                token, expira, correo
        );

        emailService.enviarEmailRecuperacion(correo, token, tipoUsuario);
    }
}