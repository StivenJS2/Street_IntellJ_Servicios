package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.vendedor;
import com.example.demo.Java1.conexiones.conexionVendedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
public class controladorVendedor {


    @Autowired
    private conexionVendedor Conexionvendedor;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/vendedor")
    @Operation(summary = "Obtener vendedores",
            description = "Devuelve una lista con todos los vendedores existentes")
    public List<vendedor> obtenerVendedor() {
        return Conexionvendedor.obtenerVendedor();
    }

    @PostMapping("/vendedor")
    @Operation(summary = "Agregar vendedores",
            description = "Crea un nuevo vendedor")
    public String agregarVendedor(@RequestBody vendedor Vendedor) {
        Conexionvendedor.agregarVendedor(Vendedor);
        return "Vendedor registrado correctamente.";

    }

    @DeleteMapping("/vendedor/{id_vendedor}")
    @Operation(summary = "Eliminar vendedores",
            description = "Elimina un vendedor")
    public String eliminarVendedor(@PathVariable int id_vendedor) {
        Conexionvendedor.eliminarVendedor(id_vendedor);
        return "Vendedor eliminado correctamente.";
    }


    @PutMapping("/vendedor/{id_vendedor}")
    @Operation(summary = "Actualizar vendedores",
            description = "Actualiza un vendedor")
    public String actualizarVendedor(@PathVariable int id_vendedor, @RequestBody vendedor Vendedor) {
        Conexionvendedor.actualizarVendedor(id_vendedor,Vendedor);
        return "Vendedor actualizado con exito";
    }


}
