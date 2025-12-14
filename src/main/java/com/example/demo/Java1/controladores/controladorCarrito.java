package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.carrito;
import com.example.demo.Java1.conexiones.conexionCarrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;

@RestController
public class controladorCarrito {

    @Autowired
    private conexionCarrito Conexioncarrito;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/carrito")
    @Operation(summary = "Obtener carritos",
            description = "Devuelve una lista con todos los carritos existentes")

    public List<carrito> obtenerCarrito() {
        return Conexioncarrito.obtenerCarrito();
    }

    @GetMapping("/carrito/cliente/{id_cliente}")
    @Operation(summary = "Obtener carritos por cliente",
            description = "Devuelve una lista con el carrito especificado por id")
    public List<carrito> obtenerCarritoPorCliente(@PathVariable int id_cliente) {
        return Conexioncarrito.obtenerCarritoPorCliente(id_cliente);
    }

    @PostMapping("/carrito")
    @Operation(summary = "Agregar Carritos",
            description = "Crea un nuevo carrito")
    public String agregarCarrito(@RequestBody carrito Carrito) {
        Conexioncarrito.agregarCarrito(Carrito);
        return "carrito creado correctamente.";

    }

    @DeleteMapping("/carrito/{id_carrito}")
    @Operation(summary = "Eliminar Carritos",
            description = "Elimina un carrito")
    public String eliminarCarrito(@PathVariable int id_carrito) {
        Conexioncarrito.eliminarCarrito(id_carrito);
        return "carrito eliminado correctamente.";
    }


    @PutMapping("/carrito/{id_carrito}")
    @Operation(summary = "Actualizar Carritos",
            description = "Actualiza un carrito")
    public String actualizarCarrito(@PathVariable int id_carrito, @RequestBody carrito Carrito) {
        Conexioncarrito.actualizarCarrito(id_carrito,Carrito);
        return "carrito actualizado con exito";
    }
}
