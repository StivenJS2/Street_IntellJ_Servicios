package com.example.demo.Java1.controladores;

import com.example.demo.Java1.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
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

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Permite al usuario iniciar sesión")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo_electronico");
        String contrasena = credenciales.get("contrasena");

        // Buscar en clientes
        Map<String, Object> usuario = buscarCliente(correo, contrasena);
        if (usuario != null) {
            // 👇 Ahora incluimos el id_cliente en el token
            int idCliente = (int) usuario.get("id_cliente");
            String token = jwtUtil.generarToken(correo, "ROLE_CLIENTE", idCliente);
            return ResponseEntity.ok(respuesta(token, "cliente", usuario));
        }

        // Buscar en vendedores
        usuario = buscarVendedor(correo, contrasena);
        if (usuario != null) {
            // 👇 Ahora incluimos el id_vendedor en el token
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

    private Map<String, Object> buscarCliente(String correo, String contrasena) {
        String sql = "SELECT id_cliente, nombre, apellido, correo_electronico FROM cliente WHERE correo_electronico = ? AND contrasena = ?";
        try {
            return jdbcTemplate.queryForMap(sql, correo, contrasena);
        } catch (Exception e) {
            return null;
        }
    }

    private Map<String, Object> buscarVendedor(String correo, String contrasena) {
        String sql = "SELECT id_vendedor, nombre, apellido, correo_electronico FROM vendedor WHERE correo_electronico = ? AND contrasena = ?";
        try {
            return jdbcTemplate.queryForMap(sql, correo, contrasena);
        } catch (Exception e) {
            return null;
        }
    }
}