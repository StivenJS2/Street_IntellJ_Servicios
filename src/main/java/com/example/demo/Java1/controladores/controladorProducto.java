package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.producto;
import com.example.demo.Java1.conexiones.conexionProducto;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class controladorProducto {

    @Autowired
    private conexionProducto Conexion;

    @GetMapping("/producto")
    @Operation(summary = "Obtener productos",
            description = "Devuelve una lista con todos los productos existentes")
    public List<producto> obtenerProducto() {
        return Conexion.obtenerProducto();
    }

    @PostMapping("/producto")
    @Operation(summary = "Agregar productos",
            description = "Crea un nuevo producto")
    public String agregarProducto(@RequestBody producto Producto) {
        Conexion.agregarProducto(Producto);
        return "Producto registrado correctamente.";
    }

    @DeleteMapping("/producto/{id_producto}")
    @Operation(summary = "Eliminar productos",
            description = "Elimina un producto")
    public String eliminarProducto(@PathVariable int id_producto) {
        Conexion.eliminarProducto(id_producto);
        return "Producto eliminado correctamente.";
    }

    @PutMapping("/producto/{id_producto}")
    @Operation(summary = "Actualizar productos",
            description = "Actualiza un producto")
    public String actualizarProducto(@PathVariable int id_producto, @RequestBody producto producto) {
        Conexion.actualizarProducto(id_producto, producto);
        return "Producto actualizado con exito";
    }
}

