package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.conexiones.conexionCliente;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class controladorCliente {

    @Autowired
    private conexionCliente Conexion;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/cliente")
    @Operation(summary = "Obtener usuarios",
            description = "Devuelve una lista con todos los usuarios existentes")
    public List<cliente> obtenerUsuarios() {
        return Conexion.obtenerUsuarios();
    }

    @GetMapping("/cliente/{id_cliente}")
    @Operation(summary = "Obtener cliente por id",
            description = "Devuelve el cliente especificado por id")
    public cliente obtenerClientePorId(@PathVariable int id_cliente) {
        return Conexion.obtenerClientePorId(id_cliente);
    }

    @PostMapping("/cliente")
    @Operation(summary = "Agregar usuarios",
            description = "Crea un nuevo usuario")
    public String agregarUsuario(@RequestBody cliente Cliente) {
        Conexion.agregarUsuario(Cliente);
        return "Cliente registrado correctamente.";

    }

    @DeleteMapping("/cliente/{id_cliente}")
    @Operation(summary = "Eliminar usuarios",
            description = "Elimina un usuario")
    public String eliminarCliente(@PathVariable int id_cliente) {
        Conexion.eliminarCliente(id_cliente);
        return "Cliente eliminado correctamente.";
    }


    @PutMapping("/cliente/{id_cliente}")
    @Operation(summary = "Actualizar usuarios",
            description = "Actualiza un usuario")
    public String actualizarCliente(@PathVariable int id_cliente, @RequestBody cliente cliente) {
        Conexion.actualizarCliente(id_cliente, cliente);
        return "Cliente actualizado con exito";
    }



}


