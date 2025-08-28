package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.promocion;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class conexionPromocion {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<promocion> obtenerPromociones() {
        String sql = "SELECT * FROM promocion";
        return jdbcTemplate.query(sql, new RowMapper<promocion>() {
            @Override
            public promocion mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new promocion(
                        rs.getInt("id_promocion"),
                        rs.getString("descripcion"),
                        rs.getDouble("descuento"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin"),
                        rs.getInt("id_producto")
                );
            }
        });
    }


    public void agregarPromocion(promocion promo) {
        String sql = "INSERT INTO promocion (descripcion, descuento, fecha_inicio, fecha_fin, id_producto) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                promo.getDescripcion(),
                promo.getDescuento(),
                promo.getFecha_inicio(),
                promo.getFecha_fin(),
                promo.getId_producto()
        );
    }


    public void eliminarPromocion(int id_promocion) {
        String sql = "DELETE FROM promocion WHERE id_promocion = ?";
        jdbcTemplate.update(sql, id_promocion);
    }


    public void actualizarPromocion(int id_promocion, promocion promo) {
        String sql = "UPDATE promocion SET descripcion=?, descuento=?, fecha_inicio=?, fecha_fin=?, id_producto=? " +
                "WHERE id_promocion=?";
        jdbcTemplate.update(sql,
                promo.getDescripcion(),
                promo.getDescuento(),
                promo.getFecha_inicio(),
                promo.getFecha_fin(),
                promo.getId_producto(),
                id_promocion
        );
    }
}
