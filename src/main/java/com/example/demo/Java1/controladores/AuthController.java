package com.example.demo.Java1.controladores;

import com.example.demo.Java1.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Permite al usuario iniciar sesión")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo_electronico");
        String contrasena = credenciales.get("contrasena");

        // Buscar en clientes
        Map<String, Object> usuario = buscarCliente(correo, contrasena);
        if (usuario != null) {
            // 👇 Si la cuenta no está verificada, retornar error especial
            if (usuario.containsKey("error")) {
                return ResponseEntity.status(403).body(Map.of(
                        "error", "cuenta_no_verificada",
                        "mensaje", "Debes verificar tu correo antes de iniciar sesión.",
                        "correo", correo
                ));
            }
            int idCliente = (int) usuario.get("id_cliente");
            String token = jwtUtil.generarToken(correo, "ROLE_CLIENTE", idCliente);
            return ResponseEntity.ok(respuesta(token, "cliente", usuario));
        }

        // Buscar en vendedores
        usuario = buscarVendedor(correo, contrasena);
        if (usuario != null) {
            int idVendedor = (int) usuario.get("id_vendedor");
            String token = jwtUtil.generarToken(correo, "ROLE_ADMIN", idVendedor);
            return ResponseEntity.ok(respuesta(token, "administrador", usuario));
        }

        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    }

    private Map<String, Object> respuesta(String token, String tipo, Map<String, Object> usuario) {
        Map<String, Object> r = new HashMap<>();
        r.put("token", token);
        r.put("tipo", tipo);
        r.put("usuario", usuario);
        return r;
    }

    private Map<String, Object> buscarCliente(String correo, String contrasenaPlana) {
        String sql = "SELECT id_cliente, nombre, apellido, correo_electronico, contrasena, verificado FROM cliente WHERE correo_electronico = ?";
        try {
            Map<String, Object> usuario = jdbcTemplate.queryForMap(sql, correo);
            String hashGuardado = (String) usuario.get("contrasena");

            System.out.println("Hash BD: " + hashGuardado);
            System.out.println("Contraseña recibida: " + contrasenaPlana);
            System.out.println("Coincide: " + passwordEncoder.matches(contrasenaPlana, hashGuardado));

            if (passwordEncoder.matches(contrasenaPlana, hashGuardado)) {
                boolean verificado = (Boolean) usuario.get("verificado");
                if (!verificado) {
                    return Map.of("error", "cuenta_no_verificada");
                }
                usuario.remove("contrasena");
                usuario.remove("verificado");
                return usuario;
            }
            return null;
        } catch (Exception e) {
            System.out.println("Error en buscarCliente: " + e.getMessage());
            return null;
        }
    }

    private Map<String, Object> buscarVendedor(String correo, String contrasenaPlana) {
        String sql = "SELECT id_vendedor, nombre, apellido, correo_electronico, contrasena FROM vendedor WHERE correo_electronico = ?";
        try {
            Map<String, Object> usuario = jdbcTemplate.queryForMap(sql, correo);
            String hashGuardado = (String) usuario.get("contrasena");

            if (passwordEncoder.matches(contrasenaPlana, hashGuardado)) {
                usuario.remove("contrasena");
                return usuario;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

}