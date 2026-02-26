package com.example.demo.Java1.conexiones;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class conexionFavorito {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 🔹 Obtener favoritos de un cliente (con datos del producto)
    public List<Map<String, Object>> obtenerFavoritos(int id_cliente) {
        String sql = """
            SELECT 
                f.id_favorito,
                f.id_producto,
                f.fecha_agregado,
                p.nombre,
                p.descripcion,
                p.precio,
                p.imagen,
                p.color,
                p.estado
            FROM favorito f
            INNER JOIN producto p ON p.id_producto = f.id_producto
            WHERE f.id_cliente = ?
            ORDER BY f.fecha_agregado DESC
        """;
        return jdbcTemplate.queryForList(sql, id_cliente);
    }

    // 🔹 Agregar producto a favoritos
    public void agregarFavorito(int id_cliente, int id_producto) {
        String sql = "INSERT INTO favorito (id_cliente, id_producto) VALUES (?, ?)";
        jdbcTemplate.update(sql, id_cliente, id_producto);
    }

    // 🔹 Eliminar un favorito puntual
    public void eliminarFavorito(int id_favorito) {
        jdbcTemplate.update("DELETE FROM favorito WHERE id_favorito = ?", id_favorito);
    }

    // 🔹 Verificar si ya es favorito (para el botón toggle en el front)
    public boolean esFavorito(int id_cliente, int id_producto) {
        String sql = "SELECT COUNT(*) FROM favorito WHERE id_cliente = ? AND id_producto = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id_cliente, id_producto);
        return count != null && count > 0;
    }
}