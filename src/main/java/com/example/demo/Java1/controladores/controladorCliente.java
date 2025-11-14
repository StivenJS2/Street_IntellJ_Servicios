package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.conexiones.conexionCliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class controladorCliente {

    @Autowired
    private conexionCliente Conexion;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/cliente")
    public List<cliente> obtenerUsuarios() {
        return Conexion.obtenerUsuarios();
    }

    @PostMapping("/cliente")
    public String agregarUsuario(@RequestBody cliente Cliente) {
        Conexion.agregarUsuario(Cliente);
        return "Cliente registrado correctamente.";

    }

    @DeleteMapping("/cliente/{id_cliente}")
    public String eliminarCliente(@PathVariable int id_cliente) {
        Conexion.eliminarCliente(id_cliente);
        return "Cliente eliminado correctamente.";
    }


    @PutMapping("/cliente/{id_cliente}")
    public String actualizarCliente(@PathVariable int id_cliente, @RequestBody cliente cliente) {
        Conexion.actualizarCliente(id_cliente, cliente);
        return "Cliente actualizado con exito";
    }



}


