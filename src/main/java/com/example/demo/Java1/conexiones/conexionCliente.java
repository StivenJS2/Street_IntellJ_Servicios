package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.cliente;
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
public class conexionCliente {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<cliente> obtenerUsuarios() {
        String sql = "SELECT * FROM cliente";
        return jdbcTemplate.query(sql, new RowMapper<cliente>() {

            @Override
            public cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new cliente(

                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("contrasena"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico")
                );
            }
        });
    }

    public void agregarUsuario(cliente Cliente) {
        String sql = "INSERT INTO cliente (nombre, apellido, contrasena, direccion, telefono, correo_electronico) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                Cliente.getNombre(),
                Cliente.getApellido(),
                Cliente.getContrasena(),
                Cliente.getDireccion(),
                Cliente.getTelefono(),
                Cliente.getCorreo_electronico()
        );
    }

    @DeleteMapping("/cliente/{id_cliente}")
    public void eliminarCliente(int id_cliente) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        jdbcTemplate.update(sql, id_cliente);
    }

    @PutMapping("/cliente/{id_cliente}")
    public void actualizarCliente(@PathVariable int id_cliente, @RequestBody cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, apellido = ?,contrasena=?,direccion=?,telefono=?, correo_electronico = ? WHERE id_cliente = ?";
        jdbcTemplate.update(sql,
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getContrasena(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getCorreo_electronico(),
                id_cliente);

    }

    }

