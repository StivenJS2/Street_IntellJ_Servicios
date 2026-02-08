package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.producto;
import com.example.demo.Java1.conexiones.conexionProducto;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/producto/{id}/detalle")
    public Map<String, Object> obtenerDetalleProducto(@PathVariable int id) {
        List<Map<String, Object>> filas = Conexion.obtenerDetalleProducto(id);

        if (filas.isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Producto no encontrado");
            return error;
        }

        Map<String, Object> respuesta = new HashMap<>();

        // Datos básicos del producto (primera fila)
        respuesta.put("nombre", filas.get(0).get("nombre"));
        respuesta.put("descripcion", filas.get(0).get("descripcion"));
        respuesta.put("color", filas.get(0).get("color"));
        respuesta.put("precio", filas.get(0).get("precio"));
        respuesta.put("imagen", filas.get(0).get("imagen"));

        // Detalles con tallas e IDs
        List<Map<String, Object>> detalles = new ArrayList<>();
        for (Map<String, Object> fila : filas) {
            if (fila.get("talla") != null && fila.get("id_detalle_producto") != null) {
                Map<String, Object> detalle = new HashMap<>();
                detalle.put("talla", fila.get("talla"));
                detalle.put("id_detalle_producto", fila.get("id_detalle_producto"));
                detalles.add(detalle);
            }
        }

        respuesta.put("detalles", detalles);

        System.out.println("📦 Enviando detalles: " + respuesta);

        return respuesta;
    }

    @GetMapping("/producto/categoria/{idCategoria}")
    public List<Map<String, Object>> obtenerProductosPorCategoria(@PathVariable int idCategoria) {
        return Conexion.obtenerProductosPorCategoria(idCategoria);
    }


}

