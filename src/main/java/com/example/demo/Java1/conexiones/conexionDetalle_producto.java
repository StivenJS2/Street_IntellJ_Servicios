package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.detalle_producto;
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
public class conexionDetalle_producto {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<detalle_producto> obtenerDetalles_producto() {
        String sql = "SELECT * FROM detalle_producto";
        return jdbcTemplate.query(sql, new RowMapper<detalle_producto>() {

            @Override
            public detalle_producto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new detalle_producto(

                        rs.getInt("id_detalle_producto"),
                        rs.getString("talla"),
                        rs.getString("color"),
                        rs.getInt("id_producto"),
                        rs.getInt("id_categoria"),
                        rs.getDouble("precio")
                );
            }
        });
    }

    public void agregarDetalle_producto(detalle_producto detalle_producto) {
        String sql = "INSERT INTO detalle_producto (talla, color, id_producto, id_categoria, precio) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                detalle_producto.getTalla(),
                detalle_producto.getColor(),
                detalle_producto.getId_producto(),
                detalle_producto.getId_categoria(),
                detalle_producto.getPrecio()
        );
    }

    @DeleteMapping("/detalle_producto/{id_detalle_producto}")
    public void eliminarDetalle_producto(int id_detalle_producto) {
        String sql = "DELETE FROM detalle_producto WHERE id_detalle_producto = ?";
        jdbcTemplate.update(sql, id_detalle_producto);
    }

    @PutMapping("/detalle_producto/{id_detalle_producto}")
    public void actualizarDetalle_producto(@PathVariable int id_detalle_producto, @RequestBody detalle_producto detalleProducto) {
        String sql = "UPDATE detalle_producto SET talla = ?, color = ?, id_producto=?, id_categoria=?, precio=? WHERE id_detalle_producto = ?";
        jdbcTemplate.update(sql,

                detalleProducto.getTalla(),
                detalleProducto.getColor(),
                detalleProducto.getId_producto(),
                detalleProducto.getId_categoria(),
                detalleProducto.getPrecio(),

                id_detalle_producto);

    }

}
