package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.promocion;
import com.example.demo.Java1.conexiones.conexionPromocion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
@RestController
public class controladorPromocion {
    @Autowired
    private conexionPromocion ConexioncPromocion;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/promocion")
    @Operation(summary = "Obtener promociones",
            description = "Devuelve una lista con todos las promociones existentes")
    public List<promocion> obtenerPromocion() {
        return ConexioncPromocion.obtenerPromociones();
    }

    @PostMapping("/promocion")
    @Operation(summary = "Agregar promociones",
            description = "Crea una nueva promocion")
    public String agregarPromocion(@RequestBody promocion Promocion) {
        ConexioncPromocion.agregarPromocion(Promocion);
        return "Promocion agregada correctamente.";

    }

    @DeleteMapping("/promocion/{id_promocion}")
    @Operation(summary = "Eliminar promociones",
            description = "Elimina una promocion")
    public String eliminarPromocion(@PathVariable int id_promocion) {
        ConexioncPromocion.eliminarPromocion(id_promocion);
        return "Promocion eliminada correctamente.";
    }


    @PutMapping("/promocion/{id_promocion}")
    @Operation(summary = "Actualizar promociones",
            description = "Actualiza una promocion")
    public String actualizarPromocion(@PathVariable int id_promocion, @RequestBody promocion Promocion) {
        ConexioncPromocion.actualizarPromocion(id_promocion, Promocion);
        return "Promocion actualizada con exito";
    }
}
