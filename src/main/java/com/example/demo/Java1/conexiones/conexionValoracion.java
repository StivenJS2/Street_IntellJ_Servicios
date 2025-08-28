package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.valoracion;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class conexionValoracion {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Obtener todas las valoraciones
    public List<valoracion> obtenerValoraciones() {
        String sql = "SELECT * FROM valoracion";
        return jdbcTemplate.query(sql, new RowMapper<valoracion>() {
            @Override
            public valoracion mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new valoracion(
                        rs.getInt("id_valoracion"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_producto"),
                        rs.getInt("calificacion"),
                        rs.getString("comentario"),
                        rs.getString("fecha_valoracion")
                );
            }
        });
    }

    // Agregar una valoración
    public void agregarValoracion(valoracion val) {
        String sql = "INSERT INTO valoracion (id_cliente, id_producto, calificacion, comentario, fecha_valoracion) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                val.getId_cliente(),
                val.getId_producto(),
                val.getCalificacion(),
                val.getComentario(),
                val.getFecha_valoracion()
        );
    }

    // Eliminar una valoración
    public void eliminarValoracion(int id_valoracion) {
        String sql = "DELETE FROM valoracion WHERE id_valoracion = ?";
        jdbcTemplate.update(sql, id_valoracion);
    }

    // Actualizar una valoración
    public void actualizarValoracion(int id_valoracion, valoracion val) {
        String sql = "UPDATE valoracion SET id_cliente=?, id_producto=?, calificacion=?, comentario=?, fecha_valoracion=? " +
                "WHERE id_valoracion=?";
        jdbcTemplate.update(sql,
                val.getId_cliente(),
                val.getId_producto(),
                val.getCalificacion(),
                val.getComentario(),
                val.getFecha_valoracion(),
                id_valoracion
        );
    }
}
