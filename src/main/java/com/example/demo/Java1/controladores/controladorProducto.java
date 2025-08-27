package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.producto;
import com.example.demo.Java1.conexiones.conexionProducto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class controladorProducto {

    @Autowired
    private conexionProducto Conexion;

    @GetMapping("/producto")
    public List<producto> obtenerProducto() {
        return Conexion.obtenerProducto();
    }

    @PostMapping("/producto")
    public String agregarProducto(@RequestBody producto Producto) {
        Conexion.agregarProducto(Producto);
        return "Producto registrado correctamente.";
    }

    @DeleteMapping("/producto/{id_producto}")
    public String eliminarProducto(@PathVariable int id_producto) {
        Conexion.eliminarProducto(id_producto);
        return "Producto eliminado correctamente.";
    }
}

