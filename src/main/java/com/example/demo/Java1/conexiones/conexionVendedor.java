package com.example.demo.Java1.conexiones;


import com.example.demo.Java1.Tablas.vendedor;
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

@Service
public class conexionVendedor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<vendedor> obtenerVendedor() {
        String sql = "SELECT * FROM vendedor";
        return jdbcTemplate.query(sql, new RowMapper<vendedor>() {

            @Override
            public vendedor mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new vendedor(

                        rs.getInt("id_vendedor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo_electronico"),
                        rs.getString("telefono")
                );

            }
        });
    }


    public void agregarVendedor(vendedor Vendedor) {
        String sql = "INSERT INTO vendedor (nombre, apellido,correo_electronico, telefono ) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                Vendedor.getNombre(),
                Vendedor.getApellido(),
                Vendedor.getCorreo_electronico(),

                Vendedor.getTelefono()

        );
    }


    @DeleteMapping("/vendedor/{id_vendedor}")
    public void eliminarVendedor(int id_vendedor) {
        String sql = "DELETE FROM vendedor WHERE id_vendedor = ?";
        jdbcTemplate.update(sql, id_vendedor);
    }

    @PutMapping("/vendedor/{id_vendedor}")
    public void actualizarVendedor(@PathVariable int id_vendedor, @RequestBody vendedor Vendedor) {
        String sql = "UPDATE vendedor SET nombre = ?, apellido = ?,correo_electronico=?,telefono=? WHERE id_vendedor = ?";
        jdbcTemplate.update(sql,
                Vendedor.getNombre(),
                Vendedor.getApellido(),
                Vendedor.getCorreo_electronico(),
                Vendedor.getTelefono(),
                id_vendedor);

    }

}


