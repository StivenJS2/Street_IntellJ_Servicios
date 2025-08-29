package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.pedido;
import com.example.demo.Java1.conexiones.conexionPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class controladorPedido {

    @Autowired
    private conexionPedido  Conexionpedido;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/pedido")
    public List<pedido> obtenerPedido() {
        return Conexionpedido.obtenerPedido();
    }

    @PostMapping("/pedido")
    public String agregarPedido(@RequestBody pedido Pedido) {
        Conexionpedido.agregarPedido(Pedido);
        return "pedido registrado correctamente.";

    }

    @DeleteMapping("/pedido/{id_pedido}")
    public String eliminarPedido(@PathVariable int id_pedido) {
        Conexionpedido.eliminarPedido(id_pedido);
        return "pedido eliminado correctamente.";
    }


    @PutMapping("/pedido/{id_pedido}")
    public String actualizarPedido(@PathVariable int id_pedido, @RequestBody pedido Pedido) {
        Conexionpedido.actualizarPedido(id_pedido, Pedido);
        return "pedido actualizado con exito";
    }
}
