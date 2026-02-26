package com.example.demo.Java1.controladores;

import com.example.demo.Java1.servicios.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/verificacion")
@CrossOrigin(origins = "*")
public class VerificacionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    // =============================================
    // Enviar código de verificación al correo
    // =============================================
    @PostMapping("/enviar")
    public ResponseEntity<?> enviarCodigo(@RequestBody Map<String, String> body) {
        String correo = body.get("correo_electronico");

        try {
            // Verificar que el cliente existe y no está verificado aún
            Map<String, Object> cliente = jdbcTemplate.queryForMap(
                    "SELECT id_cliente, verificado FROM cliente WHERE correo_electronico = ?", correo
            );

            int verificado = ((Number) cliente.get("verificado")).intValue();
            if (verificado == 1) {
                return ResponseEntity.badRequest().body(Map.of("error", "Este correo ya está verificado."));
            }

            // Generar código de 6 dígitos
            String codigo = String.format("%06d", new Random().nextInt(999999));
            LocalDateTime expira = LocalDateTime.now().plusMinutes(10);

            // Guardar código en BD
            jdbcTemplate.update(
                    "UPDATE cliente SET codigo_verificacion = ?, codigo_verificacion_expira = ? WHERE correo_electronico = ?",
                    codigo, expira, correo
            );

            // Enviar email con el código
            emailService.enviarEmailVerificacion(correo, codigo);

            return ResponseEntity.ok(Map.of("mensaje", "Código enviado correctamente."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Correo no encontrado."));
        }
    }

    // =============================================
    // Verificar el código ingresado por el usuario
    // =============================================
    @PostMapping("/validar")
    public ResponseEntity<?> validarCodigo(@RequestBody Map<String, String> body) {
        String correo = body.get("correo_electronico");
        String codigo = body.get("codigo");

        try {
            Map<String, Object> cliente = jdbcTemplate.queryForMap(
                    "SELECT codigo_verificacion, codigo_verificacion_expira FROM cliente WHERE correo_electronico = ?",
                    correo
            );

            String codigoGuardado = (String) cliente.get("codigo_verificacion");
            LocalDateTime expira = (LocalDateTime) cliente.get("codigo_verificacion_expira");

            // Verificar que el código no sea nulo
            if (codigoGuardado == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "No hay un código activo para este correo."));
            }

            // Verificar expiración
            if (expira.isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body(Map.of("error", "El código ha expirado."));
            }

            // Verificar que el código coincida
            if (!codigoGuardado.equals(codigo)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Código incorrecto."));
            }

            // Activar cuenta y limpiar código
            jdbcTemplate.update(
                    "UPDATE cliente SET verificado = 1, codigo_verificacion = NULL, codigo_verificacion_expira = NULL WHERE correo_electronico = ?",
                    correo
            );

            return ResponseEntity.ok(Map.of("mensaje", "Correo verificado correctamente."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Correo no encontrado."));
        }
    }

    // =============================================
    // Reenviar código (por si expiró)
    // =============================================
    @PostMapping("/reenviar")
    public ResponseEntity<?> reenviarCodigo(@RequestBody Map<String, String> body) {
        return enviarCodigo(body);
    }
}