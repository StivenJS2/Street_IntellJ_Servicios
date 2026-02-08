package com.example.demo.Java1.conexiones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class conexionDetalle_carrito {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private conexionCarrito conexionCarrito;

    public int agregarProducto(int idCliente, int idDetalleProducto, int cantidad, double precioUnitario) {
        System.out.println("🔹 conexionDetalleCarrito.agregarProducto INICIO");
        System.out.println("   - idCliente: " + idCliente);
        System.out.println("   - idDetalleProducto: " + idDetalleProducto);
        System.out.println("   - cantidad: " + cantidad);
        System.out.println("   - precioUnitario: " + precioUnitario);

        // 1. Obtener o crear carrito del cliente
        System.out.println("🔹 Obteniendo o creando carrito...");
        int idCarrito = conexionCarrito.obtenerOCrearCarrito(idCliente);
        System.out.println("🔹 ID Carrito obtenido: " + idCarrito);

        // 2. Verificar si el producto ya está en el carrito
        String sqlVerificar = """
        SELECT id_detalle_carrito, cantidad 
        FROM detalle_carrito 
        WHERE id_carrito = ? AND id_detalle_producto = ?
    """;

        System.out.println("🔹 Verificando si producto ya existe en carrito...");
        System.out.println("   SQL: " + sqlVerificar);
        System.out.println("   Params: idCarrito=" + idCarrito + ", idDetalleProducto=" + idDetalleProducto);

        try {
            // Ya existe, actualizar cantidad
            Map<String, Object> existente = jdbcTemplate.queryForMap(sqlVerificar, idCarrito, idDetalleProducto);

            System.out.println("🔹 Producto YA EXISTE en carrito: " + existente);

            int idDetalleCarrito = (int) existente.get("id_detalle_carrito");
            int cantidadActual = (int) existente.get("cantidad");
            int nuevaCantidad = cantidadActual + cantidad;
            double nuevoSubtotal = nuevaCantidad * precioUnitario;

            System.out.println("🔹 Actualizando cantidad:");
            System.out.println("   - Cantidad actual: " + cantidadActual);
            System.out.println("   - Nueva cantidad: " + nuevaCantidad);
            System.out.println("   - Nuevo subtotal: " + nuevoSubtotal);

            String sqlActualizar = """
            UPDATE detalle_carrito 
            SET cantidad = ?, subtotal = ?
            WHERE id_detalle_carrito = ?
        """;

            int resultado = jdbcTemplate.update(sqlActualizar, nuevaCantidad, nuevoSubtotal, idDetalleCarrito);
            System.out.println("🔹 Resultado UPDATE: " + resultado);

            return resultado;

        } catch (Exception e) {
            // No existe, insertar nuevo
            System.out.println("🔹 Producto NO EXISTE en carrito, insertando nuevo...");
            System.out.println("🔹 Excepción al verificar (normal): " + e.getMessage());

            double subtotal = cantidad * precioUnitario;

            String sqlInsertar = """
            INSERT INTO detalle_carrito (id_carrito, id_detalle_producto, cantidad, precio_unitario, subtotal)
            VALUES (?, ?, ?, ?, ?)
        """;

            System.out.println("🔹 Insertando producto:");
            System.out.println("   SQL: " + sqlInsertar);
            System.out.println("   Params:");
            System.out.println("     - id_carrito: " + idCarrito);
            System.out.println("     - id_detalle_producto: " + idDetalleProducto);
            System.out.println("     - cantidad: " + cantidad);
            System.out.println("     - precio_unitario: " + precioUnitario);
            System.out.println("     - subtotal: " + subtotal);

            try {
                int resultado = jdbcTemplate.update(sqlInsertar, idCarrito, idDetalleProducto, cantidad, precioUnitario, subtotal);
                System.out.println("🔹 Resultado INSERT: " + resultado);
                return resultado;
            } catch (Exception insertEx) {
                System.err.println("❌ ERROR AL INSERTAR:");
                System.err.println("   Mensaje: " + insertEx.getMessage());
                insertEx.printStackTrace();
                return 0;
            }
        }
    }

    // 🆕 Obtener carrito completo con productos
    public List<Map<String, Object>> obtenerCarritoConProductos(int idCliente) {
        String sql = """
            SELECT 
                dc.id_detalle_carrito,
                dc.cantidad,
                dc.precio_unitario,
                dc.subtotal,
                p.id_producto,
                p.nombre,
                p.imagen,
                p.color,
                p.precio AS precio_actual,
                dp.talla,
                dp.id_detalle_producto
            FROM carrito c
            INNER JOIN detalle_carrito dc ON c.id_carrito = dc.id_carrito
            INNER JOIN detalle_producto dp ON dc.id_detalle_producto = dp.id_detalle_producto
            INNER JOIN producto p ON dp.id_producto = p.id_producto
            WHERE c.id_cliente = ?
            ORDER BY dc.fecha_agregado DESC
        """;

        return jdbcTemplate.queryForList(sql, idCliente);
    }

    // 🆕 Contar items en el carrito
    public int contarItems(int idCliente) {
        String sql = """
            SELECT COALESCE(SUM(dc.cantidad), 0)
            FROM carrito c
            INNER JOIN detalle_carrito dc ON c.id_carrito = dc.id_carrito
            WHERE c.id_cliente = ?
        """;

        Integer resultado = jdbcTemplate.queryForObject(sql, Integer.class, idCliente);
        return resultado != null ? resultado : 0;
    }

    // 🆕 Calcular total del carrito
    public double calcularTotal(int idCliente) {
        String sql = """
            SELECT COALESCE(SUM(dc.subtotal), 0)
            FROM carrito c
            INNER JOIN detalle_carrito dc ON c.id_carrito = dc.id_carrito
            WHERE c.id_cliente = ?
        """;

        Double resultado = jdbcTemplate.queryForObject(sql, Double.class, idCliente);
        return resultado != null ? resultado : 0.0;
    }

    // 🆕 Actualizar cantidad de un item
    public int actualizarCantidad(int idDetalleCarrito, int cantidad) {
        String sql = """
            UPDATE detalle_carrito 
            SET cantidad = ?,
                subtotal = cantidad * precio_unitario
            WHERE id_detalle_carrito = ?
        """;

        return jdbcTemplate.update(sql, cantidad, idDetalleCarrito);
    }

    // 🆕 Eliminar item del carrito
    public int eliminarItem(int idDetalleCarrito) {
        String sql = "DELETE FROM detalle_carrito WHERE id_detalle_carrito = ?";
        return jdbcTemplate.update(sql, idDetalleCarrito);
    }

    // 🆕 Vaciar todos los items del carrito
    public int vaciarCarrito(int idCliente) {
        String sql = """
            DELETE FROM detalle_carrito 
            WHERE id_carrito = (SELECT id_carrito FROM carrito WHERE id_cliente = ?)
        """;
        return jdbcTemplate.update(sql, idCliente);
    }

    // 🆕 Obtener ID de detalle_producto por id_producto y talla
    public Integer obtenerIdDetalleProducto(int idProducto, String talla) {
        String sql = """
            SELECT id_detalle_producto 
            FROM detalle_producto 
            WHERE id_producto = ? AND talla = ?
            LIMIT 1
        """;

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, idProducto, talla);
        } catch (Exception e) {
            return null;
        }
    }
}