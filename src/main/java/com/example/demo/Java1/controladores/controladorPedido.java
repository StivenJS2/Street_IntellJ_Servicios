package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.pedido;
import com.example.demo.Java1.conexiones.conexionPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
public class controladorPedido {

    @Autowired
    private conexionPedido  Conexionpedido;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/pedido")
    @Operation(summary = "Obtener pedidos",
            description = "Devuelve una lista con todos los pedidos existentes")
    public List<pedido> obtenerPedido() {
        return Conexionpedido.obtenerPedido();
    }

    @PostMapping("/pedido")
    @Operation(summary = "Agregar pedidos",
            description = "Crea un nuevo pedido")
    public String agregarPedido(@RequestBody pedido Pedido) {
        Conexionpedido.agregarPedido(Pedido);
        return "pedido registrado correctamente.";

    }

    @DeleteMapping("/pedido/{id_pedido}")
    @Operation(summary = "Eliminar pedidos",
            description = "Elimina un pedido")
    public String eliminarPedido(@PathVariable int id_pedido) {
        Conexionpedido.eliminarPedido(id_pedido);
        return "pedido eliminado correctamente.";
    }


    @PutMapping("/pedido/{id_pedido}")
    @Operation(summary = "Actualizar pedidos",
            description = "Actualiza un pedido")
    public String actualizarPedido(@PathVariable int id_pedido, @RequestBody pedido Pedido) {
        Conexionpedido.actualizarPedido(id_pedido, Pedido);
        return "pedido actualizado con exito";
    }
}
