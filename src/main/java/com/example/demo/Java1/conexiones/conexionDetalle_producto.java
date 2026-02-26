package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.detalle_producto;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
                        rs.getInt("id_producto"),
                        rs.getInt("cantidad")
                );
            }
        });
    }

    public void agregarDetalle_producto(detalle_producto detalle_producto) {
        String sql = "INSERT INTO detalle_producto (talla, id_producto, cantidad) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                detalle_producto.getTalla(),
                detalle_producto.getId_producto(),
                detalle_producto.getCantidad()
        );
    }

    @DeleteMapping("/detalle_producto/{id_detalle_producto}")
    public void eliminarDetalle_producto(int id_detalle_producto) {
        String sql = "DELETE FROM detalle_producto WHERE id_detalle_producto = ?";
        jdbcTemplate.update(sql, id_detalle_producto);
    }

    @PutMapping("/detalle_producto/{id_detalle_producto}")
    public void actualizarDetalle_producto(@PathVariable int id_detalle_producto, @RequestBody detalle_producto detalleProducto) {
        String sql = "UPDATE detalle_producto SET talla = ?, id_producto=?, cantidad=? WHERE id_detalle_producto = ?";
        jdbcTemplate.update(sql,

                detalleProducto.getTalla(),
                detalleProducto.getId_producto(),
                detalleProducto.getCantidad(),

                id_detalle_producto);

    }

// para la barra de busqueda
    public List<detalle_producto> buscarDetallePorIdProducto(int id_producto) {

        String sql = "SELECT * FROM detalle_producto WHERE id_producto = ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{id_producto},
                new BeanPropertyRowMapper<>(detalle_producto.class)
        );
    }




}
