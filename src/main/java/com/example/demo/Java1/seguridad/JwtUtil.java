package com.example.demo.Java1.seguridad;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            "clave-super-secreta-urban-street-2025-jwt";

    private final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // 🆕 Método mejorado que incluye el ID del usuario
    public String generarToken(String usuario, String rol, int idUsuario) {
        return Jwts.builder()
                .setSubject(usuario)
                .claim("rol", rol)
                .claim("id_usuario", idUsuario)  // 👈 Agregamos el ID
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Método original (por compatibilidad, pero mejor usar el nuevo)
    public String generarToken(String usuario, String rol) {
        return Jwts.builder()
                .setSubject(usuario)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String obtenerUsuario(String token) {
        return getClaims(token).getSubject();
    }

    public String obtenerRol(String token) {
        return getClaims(token).get("rol", String.class);
    }

    // 🆕 Obtener ID del usuario desde el token
    public int obtenerIdUsuarioDeToken(String token) {
        Claims claims = getClaims(token);
        Object idUsuario = claims.get("id_usuario");

        if (idUsuario instanceof Integer) {
            return (Integer) idUsuario;
        } else if (idUsuario instanceof String) {
            return Integer.parseInt((String) idUsuario);
        }

        throw new RuntimeException("ID de usuario no encontrado en el token");
    }
}