package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.conexiones.conexionCliente;
import com.example.demo.Java1.servicios.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/cliente")
public class controladorCliente {

    @Autowired
    private conexionCliente conexion;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* ===== ADMIN ===== */

    @GetMapping
    @Operation(summary = "Obtener clientes")
    public List<cliente> obtenerUsuarios() {
        return conexion.obtenerUsuarios();
    }

    @GetMapping("/{id_cliente}")
    @Operation(summary = "Obtener cliente por ID")
    public cliente obtenerClientePorId(@PathVariable int id_cliente) {
        return conexion.obtenerClientePorId(id_cliente);
    }

    @PostMapping
    @Operation(summary = "Agregar cliente")
    public ResponseEntity<?> agregarUsuario(@RequestBody cliente cliente) {
        try {
            // 1. Guardar el cliente con verificado = 0
            conexion.agregarUsuario(cliente);

            // 2. Generar código de 6 dígitos
            String codigo = String.format("%06d", new Random().nextInt(999999));
            LocalDateTime expira = LocalDateTime.now().plusMinutes(10);

            // 3. Guardar el código en la BD
            jdbcTemplate.update(
                    "UPDATE cliente SET codigo_verificacion = ?, codigo_verificacion_expira = ? WHERE correo_electronico = ?",
                    codigo, expira, cliente.getCorreo_electronico()
            );

            // 4. Enviar email con el código
            emailService.enviarEmailVerificacion(cliente.getCorreo_electronico(), codigo);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Cliente registrado. Revisa tu correo para verificar tu cuenta.",
                    "correo", cliente.getCorreo_electronico()
            ));

        } catch (MessagingException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Cliente registrado pero no se pudo enviar el email."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error al registrar el cliente: " + e.getMessage()));
        }
    }

    @PutMapping("/{id_cliente}")
    @Operation(summary = "Actualizar cliente (admin)")
    public String actualizarCliente(
            @PathVariable int id_cliente,
            @RequestBody cliente cliente
    ) {
        conexion.actualizarCliente(id_cliente, cliente);
        return "Cliente actualizado con éxito.";
    }

    @DeleteMapping("/{id_cliente}")
    @Operation(summary = "Eliminar cliente")
    public String eliminarCliente(@PathVariable int id_cliente) {
        conexion.eliminarCliente(id_cliente);
        return "Cliente eliminado correctamente.";
    }

    /* ===== PERFIL CLIENTE ===== */

    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del cliente autenticado")
    public Map<String, Object> obtenerPerfil(Authentication authentication) {
        String correo = authentication.getName();
        return conexion.obtenerPerfilPorCorreo(correo);
    }

    @PutMapping("/perfil")
    @Operation(summary = "Actualizar perfil del cliente autenticado")
    public String actualizarPerfil(
            Authentication authentication,
            @RequestBody Map<String, String> datos
    ) {
        String correo = authentication.getName();
        conexion.actualizarPerfilPorCorreo(correo, datos);
        return "Perfil actualizado correctamente.";
    }

    /* ===== BARRA DE BÚSQUEDA ===== */

    @GetMapping("/cliente/buscar")
    public List<cliente> buscarCliente(@RequestParam String dato) {
        return conexion.buscarPorDatos(dato);
    }
}