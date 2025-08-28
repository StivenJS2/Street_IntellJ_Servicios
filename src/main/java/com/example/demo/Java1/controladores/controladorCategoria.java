package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.categoria;
import com.example.demo.Java1.conexiones.conexionCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class controladorCategoria {
    @Autowired
    private conexionCategoria Conexioncategoria;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/categoria")
    public List<categoria> obtenerCategorias() {
        return Conexioncategoria.obtenerCategorias();
    }

    @PostMapping("/categoria")
    public String agregarCategoria(@RequestBody categoria Categoria) {
        Conexioncategoria.agregarCategoria(Categoria);
        return "Categoria agregada correctamente.";

    }

    @DeleteMapping("/categoria/{id_categoria}")
    public String eliminarCategoria(@PathVariable int id_categoria) {
        Conexioncategoria.eliminarCategoria(id_categoria);
        return "Categoria eliminada correctamente.";
    }


    @PutMapping("/categoria/{id_categoria}")
    public String actualizarCategoria(@PathVariable int id_categoria, @RequestBody categoria Categoria) {
        Conexioncategoria.actualizarCategoria(id_categoria, Categoria);
        return "Categoria actualizada con exito";
    }

}
