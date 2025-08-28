package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.categoria;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class conexionCategoria {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<categoria> obtenerCategorias() {
        String sql = "SELECT * FROM categoria";
        return jdbcTemplate.query(sql, new RowMapper<categoria>() {
            @Override
            public categoria mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre")

                );
            }
        });
    }


    public void agregarCategoria(categoria cat) {
        String sql = "INSERT INTO categoria (nombre) VALUES (?)";
        jdbcTemplate.update(sql,
                cat.getNombre()

        );
    }


    @DeleteMapping("/categoria/{id_categoria}")
    public void eliminarCategoria(int id_categoria) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        jdbcTemplate.update(sql, id_categoria);
    }


    @PutMapping("/categoria/{id_categoria}")
    public void actualizarCategoria(@PathVariable int id_categoria, @RequestBody categoria Categoria) {
        String sql = "UPDATE categoria SET nombre = ? WHERE id_categoria = ?";
        jdbcTemplate.update(sql,

                Categoria.getNombre(),

                id_categoria);

    }
}