package com.example.demo.Java1.conexiones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class conexionCarrito {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 🆕 Obtener o crear carrito del cliente
    public int obtenerOCrearCarrito(int idCliente) {
        System.out.println("🔸 obtenerOCrearCarrito - idCliente: " + idCliente);

        String sqlBuscar = "SELECT id_carrito FROM carrito WHERE id_cliente = ?";

        try {
            // Intentar obtener carrito existente
            System.out.println("🔸 Buscando carrito existente...");
            int idCarrito = jdbcTemplate.queryForObject(sqlBuscar, Integer.class, idCliente);
            System.out.println("🔸 Carrito encontrado: " + idCarrito);
            return idCarrito;
        } catch (Exception e) {
            // No existe, crear uno nuevo
            System.out.println("🔸 Carrito NO existe, creando uno nuevo...");
            String sqlCrear = "INSERT INTO carrito (id_cliente) VALUES (?)";

            try {
                jdbcTemplate.update(sqlCrear, idCliente);
                System.out.println("🔸 Carrito creado exitosamente");

                // Obtener el ID del carrito recién creado
                int idCarrito = jdbcTemplate.queryForObject(sqlBuscar, Integer.class, idCliente);
                System.out.println("🔸 Nuevo ID de carrito: " + idCarrito);
                return idCarrito;
            } catch (Exception createEx) {
                System.err.println("❌ ERROR AL CREAR CARRITO:");
                System.err.println("   Mensaje: " + createEx.getMessage());
                createEx.printStackTrace();
                throw createEx;
            }
        }
    }

    // Verificar si un cliente tiene carrito
    public boolean tieneCarrito(int idCliente) {
        String sql = "SELECT COUNT(*) FROM carrito WHERE id_cliente = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idCliente);
        return count != null && count > 0;
    }

    // Eliminar carrito (cuando se completa un pedido, por ejemplo)
    public int eliminarCarrito(int idCliente) {
        String sql = "DELETE FROM carrito WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, idCliente);
    }
}