package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.categoria;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;

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
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                );
            }
        });
    }


    public void agregarCategoria(categoria cat) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
        jdbcTemplate.update(sql,
                cat.getNombre(),
                cat.getDescripcion()
        );
    }


    public void eliminarCategoria(int id_categoria) {
        String sql = "DELETE FROM categoria WHERE id_categoria = ?";
        jdbcTemplate.update(sql, id_categoria);
    }


    public void actualizarCategoria(int id_categoria, categoria cat) {
        String sql = "UPDATE categoria SET nombre=?, descripcion=? WHERE id_categoria=?";
        jdbcTemplate.update(sql,
                cat.getNombre(),
                cat.getDescripcion(),
                id_categoria
        );
    }
}
