package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.producto;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class conexionProducto {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 🔹 Productos para las cards
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
                        rs.getString("imagen"),
                        rs.getInt("id_vendedor"),
                        rs.getString("estado"),
                        rs.getDouble("precio"),
                        rs.getString("color"),
                        rs.getInt("id_categoria")
                );
            }
        });
    }

    // 🔹 Detalle de producto + tallas (MODAL)
    public List<Map<String, Object>> obtenerDetalleProducto(int idProducto) {

        String sql = """
        SELECT 
            p.nombre,
            p.descripcion,
            p.color,
            p.precio,
            p.imagen,
            dp.talla
        FROM producto p
        LEFT JOIN detalle_producto dp ON dp.id_producto = p.id_producto
        WHERE p.id_producto = ? """;

        return jdbcTemplate.queryForList(sql, idProducto);
    }


    public void agregarProducto(producto Producto) {
        String sql = """
            INSERT INTO producto (nombre, descripcion, cantidad, imagen, id_vendedor, estado, precio, color, id_categoria)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(sql,
                Producto.getNombre(),
                Producto.getDescripcion(),
                Producto.getCantidad(),
                Producto.getImagen(),
                Producto.getId_vendedor(),
                Producto.getEstado(),
                Producto.getPrecio(),
                Producto.getColor(),
                Producto.getId_categoria()
        );
    }

    public void eliminarProducto(int id_producto) {
        jdbcTemplate.update("DELETE FROM producto WHERE id_producto = ?", id_producto);
    }

    public void actualizarProducto(int id_producto, producto producto) {
        String sql = """
            UPDATE producto 
            SET nombre=?, descripcion=?, cantidad=?, imagen=?, id_vendedor=?, estado=?, precio=?, color=?, id_categoria=?
            WHERE id_producto=?
        """;

        jdbcTemplate.update(sql,
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getCantidad(),
                producto.getImagen(),
                producto.getId_vendedor(),
                producto.getEstado(),
                producto.getPrecio(),
                producto.getColor(),
                producto.getId_categoria(),
                id_producto
        );
    }
}
