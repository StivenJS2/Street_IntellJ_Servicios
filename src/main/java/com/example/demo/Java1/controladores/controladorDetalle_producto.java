package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.Tablas.detalle_producto;
import com.example.demo.Java1.conexiones.conexionCliente;
import com.example.demo.Java1.conexiones.conexionDetalle_producto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class controladorDetalle_producto {

    @Autowired
    private conexionDetalle_producto Conexion;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/detalle_producto")
    public List<detalle_producto> obtenerDetalles_producto() {
        return Conexion.obtenerDetalles_producto();
    }

    @PostMapping("/detalle_producto")
    public String agregarDetalle_producto(@RequestBody detalle_producto Detalle_producto) {
        Conexion.agregarDetalle_producto(Detalle_producto);
        return "Detalles agregados correctamente.";

    }

    @DeleteMapping("/detalle_producto/{id_detalle_producto}")
    public String eliminarDetalle_producto(@PathVariable int id_detalle_producto) {
        Conexion.eliminarDetalle_producto(id_detalle_producto);
        return "Datos del producto eliminados correctamente.";
    }


    @PutMapping("/detalle_producto/{id_detalle_producto}")
    public String actualizarDetalle_producto(@PathVariable int id_detalle_producto, @RequestBody detalle_producto detalleProducto) {
        Conexion.actualizarDetalle_producto(id_detalle_producto, detalleProducto);
        return "Detalles actualizados correctamente";
    }

}
