package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.valoracion;
import com.example.demo.Java1.conexiones.conexionValoracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class controladorValoracion {
    @Autowired
    private conexionValoracion ConexionValoracion;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/valoracion")
    public List<valoracion> obtenerValoracion() {
        return ConexionValoracion.obtenerValoraciones();
    }

    @PostMapping("/valoracion")
    public String agregarValoracion(@RequestBody valoracion Valoracion) {
        ConexionValoracion.agregarValoracion(Valoracion);
        return "Valoracion agregada correctamente.";

    }

    @DeleteMapping("/valoracion/{id_valoracion}")
    public String eliminarValoracion(@PathVariable int id_valoracion) {
        ConexionValoracion.eliminarValoracion(id_valoracion);
        return "Valoracion eliminada correctamente.";
    }


    @PutMapping("/valoracion/{id_valoracion}")
    public String actualizarValoracion(@PathVariable int id_valoracion, @RequestBody valoracion Valoracion) {
        ConexionValoracion.actualizarValoracion(id_valoracion, Valoracion);
        return "Valoracion actualizada con exito";
    }
}
