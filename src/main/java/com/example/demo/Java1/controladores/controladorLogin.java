package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.login;
import com.example.demo.Java1.conexiones.conexionLogin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class controladorLogin {

    @Autowired
    private conexionLogin Conexion;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/login")
    public Object login(@RequestBody login datos) {
        Object usuario = Conexion.autenticar(datos.getCorreo_electronico(), datos.getContrasena());

        if (usuario == null) {
            return "Credenciales incorrectas";
        }
        return usuario;
    }
}