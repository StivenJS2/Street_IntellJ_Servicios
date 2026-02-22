package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class conexionPedido {

    private static final Logger LOGGER = Logger.getLogger(conexionPedido.class.getName());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Obtiene todos los pedidos de la base de datos
     */
    public List<pedido> obtenerPedido() {
        String sql = "SELECT * FROM pedido";
        return jdbcTemplate.query(sql, new RowMapper<pedido>() {

            @Override
            public pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new pedido(
                        rs.getInt("id_pedido"),
                        rs.getInt("id_cliente"),
                        rs.getString("fecha_pedido"),
                        rs.getDouble("total"),
                        rs.getString("estado"),
                        rs.getString("metodo_pago"),
                        rs.getString("ruta_factura"),
                        rs.getString("numero_factura")
                );
            }
        });
    }

    /**
     * Agrega un nuevo pedido a la base de datos
     */
    public void agregarPedido(pedido Pedido) {
        String sql = "INSERT INTO pedido (id_cliente, fecha_pedido, total, estado, metodo_pago, ruta_factura, numero_factura) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                    Pedido.getId_cliente(),
                    Pedido.getFecha_pedido(),
                    Pedido.getTotal(),
                    Pedido.getEstado(),
                    Pedido.getMetodo_pago(),
                    Pedido.getRuta_factura() != null ? Pedido.getRuta_factura() : "",
                    Pedido.getNumero_factura()
            );
            LOGGER.info("✅ Pedido agregado correctamente: " + Pedido.getId_cliente());
        } catch (Exception e) {
            LOGGER.severe("❌ Error al agregar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina un pedido por su ID
     */
    public void eliminarPedido(int id_pedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        try {
            jdbcTemplate.update(sql, id_pedido);
            LOGGER.info("✅ Pedido eliminado: " + id_pedido);
        } catch (Exception e) {
            LOGGER.severe("❌ Error al eliminar pedido: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la ruta de la factura por ID de pedido
     */
    public String obtenerRutaFactura(int id_pedido) {
        String sql = "SELECT ruta_factura FROM pedido WHERE id_pedido = ?";
        try {
            String ruta = jdbcTemplate.queryForObject(sql, String.class, id_pedido);
            if (ruta != null && !ruta.isEmpty()) {
                LOGGER.info("✅ Ruta de factura encontrada: " + ruta);
                return ruta;
            } else {
                LOGGER.warning("⚠️ Ruta de factura vacía para pedido: " + id_pedido);
                return null;
            }
        } catch (Exception e) {
            LOGGER.warning("⚠️ No se encontró ruta de factura para pedido: " + id_pedido);
            return null;
        }
    }

    /**
     * Obtiene un pedido completo por su ID
     */
    public pedido obtenerPedidoPorId(int id_pedido) {
        String sql = "SELECT * FROM pedido WHERE id_pedido = ?";
        try {
            pedido resultado = jdbcTemplate.queryForObject(sql, new RowMapper<pedido>() {
                @Override
                public pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new pedido(
                            rs.getInt("id_pedido"),
                            rs.getInt("id_cliente"),
                            rs.getString("fecha_pedido"),
                            rs.getDouble("total"),
                            rs.getString("estado"),
                            rs.getString("metodo_pago"),
                            rs.getString("ruta_factura"),
                            rs.getString("numero_factura")
                    );
                }
            }, id_pedido);

            if (resultado != null) {
                LOGGER.info("✅ Pedido encontrado: " + id_pedido);
            }
            return resultado;
        } catch (Exception e) {
            LOGGER.warning("⚠️ Pedido no encontrado: " + id_pedido);
            return null;
        }
    }

    /**
     * Actualiza la ruta de la factura de un pedido
     */
    public void actualizarRutaFactura(int id_pedido, String rutaFactura) {
        String sql = "UPDATE pedido SET ruta_factura = ? WHERE id_pedido = ?";
        try {
            if (rutaFactura == null || rutaFactura.isEmpty()) {
                LOGGER.warning("⚠️ Intento de actualizar ruta factura vacía para pedido: " + id_pedido);
                return;
            }

            int filasActualizadas = jdbcTemplate.update(sql, rutaFactura, id_pedido);

            if (filasActualizadas > 0) {
                LOGGER.info("✅ Ruta de factura actualizada: " + rutaFactura);
            } else {
                LOGGER.warning("⚠️ No se encontró pedido para actualizar: " + id_pedido);
            }
        } catch (Exception e) {
            LOGGER.severe("❌ Error al actualizar ruta de factura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el último pedido creado por un cliente
     */
    public pedido obtenerUltimoPedidoPorCliente(int id_cliente) {
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? ORDER BY id_pedido DESC LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new RowMapper<pedido>() {
                @Override
                public pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new pedido(
                            rs.getInt("id_pedido"),
                            rs.getInt("id_cliente"),
                            rs.getString("fecha_pedido"),
                            rs.getDouble("total"),
                            rs.getString("estado"),
                            rs.getString("metodo_pago"),
                            rs.getString("ruta_factura"),
                            rs.getString("numero_factura")
                    );
                }
            }, id_cliente);
        } catch (Exception e) {
            LOGGER.warning("⚠️ No se encontró pedido para cliente: " + id_cliente);
            return null;
        }
    }

    /**
     * Obtiene todos los pedidos de un cliente específico
     */
    public List<pedido> obtenerPedidosPorCliente(int id_cliente) {
        String sql = "SELECT * FROM pedido WHERE id_cliente = ? ORDER BY id_pedido DESC";
        try {
            return jdbcTemplate.query(sql, new RowMapper<pedido>() {
                @Override
                public pedido mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new pedido(
                            rs.getInt("id_pedido"),
                            rs.getInt("id_cliente"),
                            rs.getString("fecha_pedido"),
                            rs.getDouble("total"),
                            rs.getString("estado"),
                            rs.getString("metodo_pago"),
                            rs.getString("ruta_factura"),
                            rs.getString("numero_factura")
                    );
                }
            }, id_cliente);
        } catch (Exception e) {
            LOGGER.warning("⚠️ Error al obtener pedidos del cliente: " + id_cliente);
            return List.of();
        }
    }

    @Transactional
    public Map<String, Object> confirmarPedido(pedido nuevoPedido, int idCliente) {
        Map<String, Object> resultado = new HashMap<>();

        try {
            LOGGER.info("🔹 Iniciando confirmación de pedido para cliente: " + idCliente);

            // 1️⃣ Validar que el carrito tenga items
            String sqlValidarCarrito = """
            SELECT COUNT(*) 
            FROM carrito c
            INNER JOIN detalle_carrito dc ON c.id_carrito = dc.id_carrito
            WHERE c.id_cliente = ?
        """;

            Integer cantidadItems = jdbcTemplate.queryForObject(sqlValidarCarrito, Integer.class, idCliente);

            if (cantidadItems == null || cantidadItems == 0) {
                resultado.put("exito", false);
                resultado.put("mensaje", "El carrito está vacío");
                return resultado;
            }

            // 2️⃣ Validar stock ANTES de crear el pedido
            String sqlValidarStock = """
            SELECT 
                p.nombre,
                dp.talla,
                dc.cantidad as cantidad_solicitada,
                dp.cantidad as stock_disponible
            FROM carrito c
            INNER JOIN detalle_carrito dc ON c.id_carrito = dc.id_carrito
            INNER JOIN detalle_producto dp ON dc.id_detalle_producto = dp.id_detalle_producto
            INNER JOIN producto p ON dp.id_producto = p.id_producto
            WHERE c.id_cliente = ?
              AND dp.cantidad < dc.cantidad
        """;

            List<Map<String, Object>> itemsSinStock = jdbcTemplate.queryForList(sqlValidarStock, idCliente);

            if (!itemsSinStock.isEmpty()) {
                List<String> erroresStock = new ArrayList<>();
                for (Map<String, Object> item : itemsSinStock) {
                    erroresStock.add(String.format(
                            "%s (Talla %s): Solicitado %d, Disponible %d",
                            item.get("nombre"),
                            item.get("talla"),
                            item.get("cantidad_solicitada"),
                            item.get("stock_disponible")
                    ));
                }

                resultado.put("exito", false);
                resultado.put("mensaje", "Stock insuficiente");
                resultado.put("detalles", erroresStock);
                LOGGER.warning("❌ Stock insuficiente: " + erroresStock);
                return resultado;
            }

            // 3️⃣ Crear el pedido (el trigger hace el resto automáticamente)
            LOGGER.info("🔹 Creando pedido... El trigger descontará stock y vaciará carrito");
            agregarPedido(nuevoPedido);

            resultado.put("exito", true);
            resultado.put("mensaje", "Pedido confirmado exitosamente");
            LOGGER.info("✅ Pedido confirmado para cliente: " + idCliente);

        } catch (Exception e) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al procesar pedido: " + e.getMessage());
            LOGGER.severe("❌ Error al confirmar pedido: " + e.getMessage());
            e.printStackTrace();
        }

        return resultado;
    }
    /**
     * esto es unicamente para el boton estado no afceta nada de lo que pusieron anteriormente
     */
    public void actualizarEstado(int id, String estado) {
        String sql = "UPDATE pedido SET estado = ? WHERE id_pedido = ?";
        jdbcTemplate.update(sql, estado, id);
    }






}