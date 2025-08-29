package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.Tablas.producto;
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
                        rs.getInt("cantidad"),
                        rs.getInt("id_vendedor"),
                        rs.getString("estado")
                );
            }
        });
    }

    public void agregarProducto(producto Producto) {
        String sql = "INSERT INTO producto (nombre, descripcion, cantidad, id_vendedor, estado)" +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                Producto.getNombre(),
                Producto.getDescripcion(),
                Producto.getCantidad(),
                Producto.getId_vendedor(),
                Producto.getEstado()
        );
    }

    @DeleteMapping("/producto/{id_producto}")
    public void eliminarProducto(int id_producto) {
        String sql = "DELETE FROM producto WHERE id_producto = ?";
        jdbcTemplate.update(sql, id_producto);
    }

    @PutMapping("/producto/{id_producto}")
    public void actualizarProducto(@PathVariable int id_producto, @RequestBody producto  producto) {
        String sql = "UPDATE producto SET nombre = ?, descripcion = ?,cantidad=?,id_vendedor=?, estado=? WHERE id_producto = ?";
        jdbcTemplate.update(sql,
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCantidad(),
                producto.getId_vendedor(),
                producto.getEstado(),
                id_producto);

    }
}
