package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.producto;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class conexionProducto {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<producto> obtenerProducto() {
        String sql = "SELECT * FROM producto";
        return jdbcTemplate.query(sql, new RowMapper<producto>() {

            @Override
            public producto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new producto(

                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("estado"),
                        rs.getInt("stock"),
                        rs.getDouble("precio")
                );
            }
        });
    }

    public void agregarProducto(producto Producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, estado, stock, precio)" +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                Producto.getNombre(),
                Producto.getDescripcion(),
                Producto.getEstado(),
                Producto.getStock(),
                Producto.getPrecio()
        );
    }

    @DeleteMapping("/producto/{id_producto}")
    public void eliminarProducto(int id_producto) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        jdbcTemplate.update(sql, id_producto);
    }
}
