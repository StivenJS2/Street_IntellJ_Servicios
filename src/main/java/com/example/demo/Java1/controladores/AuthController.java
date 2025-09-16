package com.example.demo.Java1.controladores;

import com.example.demo.Java1.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo_electronico");
        String contrasena = credenciales.get("contrasena");

        // Buscar en tabla cliente
        Map<String, Object> usuario = buscarCliente(correo, contrasena);
        if (usuario != null) {
            String token = jwtUtil.generarToken(correo);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("token", token);
            respuesta.put("tipo", "cliente");
            respuesta.put("usuario", usuario);
            return ResponseEntity.ok(respuesta);
        }

        // Buscar en tabla vendedor (administrador)
        usuario = buscarVendedor(correo, contrasena);
        if (usuario != null) {
            String token = jwtUtil.generarToken(correo);
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("token", token);
            respuesta.put("tipo", "administrador");
            respuesta.put("usuario", usuario);
            return ResponseEntity.ok(respuesta);
        }

        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
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