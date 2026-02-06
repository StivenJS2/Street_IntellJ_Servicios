package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.carrito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class conexionCarrito {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<carrito> obtenerCarrito() {
        String sql = "SELECT * FROM carrito";
        return jdbcTemplate.query(sql, new RowMapper<carrito>() {

            @Override
            public carrito mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new carrito(
                        rs.getInt("id_carrito"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_detalle_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal")
                );
            }
        });
    }

    public List<carrito> obtenerCarritoPorCliente(int id_cliente) {
        String sql = "SELECT * FROM carrito WHERE id_cliente = ?";
        return jdbcTemplate.query(sql, new Object[]{id_cliente}, (rs, rowNum) -> {
            carrito c = new carrito();
            c.setId_carrito(rs.getInt("id_carrito"));
            c.setId_cliente(rs.getInt("id_cliente"));
            c.setId_detalle_producto(rs.getInt("id_detalle_producto"));
            c.setCantidad(rs.getInt("cantidad"));
            c.setPrecio_untario(rs.getDouble("precio_unitario"));
            c.setSubtotal(rs.getDouble("subtotal"));
            return c;
        });
    }

    // 🆕 Obtener carrito con detalles de productos (para mostrar en la vista)
    public List<Map<String, Object>> obtenerCarritoConDetalles(int id_cliente) {
        String sql = """
            SELECT 
                c.id_carrito,
                c.cantidad,
                c.precio_unitario,
                c.subtotal,
                p.id_producto,
                p.nombre,
                p.imagen,
                p.color,
                p.precio,
                dp.talla,
                dp.id_detalle_producto
            FROM carrito c
            INNER JOIN detalle_producto dp ON c.id_detalle_producto = dp.id_detalle_producto
            INNER JOIN producto p ON dp.id_producto = p.id_producto
            WHERE c.id_cliente = ?
            ORDER BY c.id_carrito DESC
        """;

        return jdbcTemplate.queryForList(sql, id_cliente);
    }

    // 🆕 Verificar si un producto ya está en el carrito
    public carrito verificarProductoEnCarrito(int id_cliente, int id_detalle_producto) {
        String sql = "SELECT * FROM carrito WHERE id_cliente = ? AND id_detalle_producto = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id_cliente, id_detalle_producto}, (rs, rowNum) -> {
                carrito c = new carrito();
                c.setId_carrito(rs.getInt("id_carrito"));
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setId_detalle_producto(rs.getInt("id_detalle_producto"));
                c.setCantidad(rs.getInt("cantidad"));
                c.setPrecio_untario(rs.getDouble("precio_unitario"));
                c.setSubtotal(rs.getDouble("subtotal"));
                return c;
            });
        } catch (Exception e) {
            return null; // No existe
        }
    }

    // 🆕 Agregar o actualizar cantidad si ya existe
    public int agregarOActualizarCarrito(int id_cliente, int id_detalle_producto, int cantidad, double precio_unitario) {
        carrito existente = verificarProductoEnCarrito(id_cliente, id_detalle_producto);

        if (existente != null) {
            // Ya existe, actualizar cantidad
            int nuevaCantidad = existente.getCantidad() + cantidad;
            double nuevoSubtotal = nuevaCantidad * precio_unitario;

            String sqlActualizar = """
                UPDATE carrito 
                SET cantidad = ?, subtotal = ?
                WHERE id_carrito = ?
            """;

            return jdbcTemplate.update(sqlActualizar, nuevaCantidad, nuevoSubtotal, existente.getId_carrito());
        } else {
            // No existe, insertar nuevo
            double subtotal = cantidad * precio_unitario;

            String sqlInsertar = """
                INSERT INTO carrito (id_cliente, id_detalle_producto, cantidad, precio_unitario, subtotal)
                VALUES (?, ?, ?, ?, ?)
            """;

            return jdbcTemplate.update(sqlInsertar, id_cliente, id_detalle_producto, cantidad, precio_unitario, subtotal);
        }
    }

    // 🆕 Obtener ID de detalle_producto por id_producto y talla
    public Integer obtenerIdDetalleProducto(int id_producto, String talla) {
        String sql = """
            SELECT id_detalle_producto 
            FROM detalle_producto 
            WHERE id_producto = ? AND talla = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, id_producto, talla);
        } catch (Exception e) {
            return null;
        }
    }

    // 🆕 Contar items en el carrito
    public int contarItemsCarrito(int id_cliente) {
        String sql = "SELECT COALESCE(SUM(cantidad), 0) FROM carrito WHERE id_cliente = ?";
        Integer resultado = jdbcTemplate.queryForObject(sql, Integer.class, id_cliente);
        return resultado != null ? resultado : 0;
    }

    // 🆕 Calcular total del carrito
    public double calcularTotalCarrito(int id_cliente) {
        String sql = "SELECT COALESCE(SUM(subtotal), 0) FROM carrito WHERE id_cliente = ?";
        Double resultado = jdbcTemplate.queryForObject(sql, Double.class, id_cliente);
        return resultado != null ? resultado : 0.0;
    }

    // 🆕 Actualizar solo la cantidad de un item
    public int actualizarCantidadItem(int id_carrito, int cantidad) {
        String sql = """
            UPDATE carrito 
            SET cantidad = ?,
                subtotal = cantidad * precio_unitario
            WHERE id_carrito = ?
        """;

        return jdbcTemplate.update(sql, cantidad, id_carrito);
    }

    // 🆕 Vaciar carrito de un cliente
    public int vaciarCarritoCliente(int id_cliente) {
        String sql = "DELETE FROM carrito WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, id_cliente);
    }

    // Métodos originales sin cambios
    public void agregarCarrito(carrito Carrito) {
        String sql = "INSERT INTO carrito (id_cliente,id_detalle_producto,cantidad,precio_unitario,subtotal) " +
                "VALUES (?, ?, ?, ?,?)";
        jdbcTemplate.update(sql,
                Carrito.getId_cliente(),
                Carrito.getId_detalle_producto(),
                Carrito.getCantidad(),
                Carrito.getPrecio_unitario(),
                Carrito.getSubtotal()
        );
    }

    @DeleteMapping("/carrito/{id_carrito}")
    public void eliminarCarrito(int id_carrito) {
        String sql = "DELETE FROM carrito WHERE id_carrito = ?";
        jdbcTemplate.update(sql, id_carrito);
    }

    @PutMapping("/carrito/{id_carrito}")
    public void actualizarCarrito(@PathVariable int id_carrito, @RequestBody carrito Carrito) {
        String sql = "UPDATE carrito SET id_cliente = ?,id_detalle_producto=?,cantidad=?,precio_unitario=?,subtotal=? WHERE id_carrito = ?";
        jdbcTemplate.update(sql,
                Carrito.getId_cliente(),
                Carrito.getId_detalle_producto(),
                Carrito.getCantidad(),
                Carrito.getPrecio_unitario(),
                Carrito.getSubtotal(),
                id_carrito);
    }
}