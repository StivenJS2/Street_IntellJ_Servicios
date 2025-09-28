package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.Tablas.vendedor;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Service
public class conexionLogin {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Object autenticar(String correo, String contrasena) {
        String sqlCliente = "SELECT * FROM cliente WHERE correo_electronico = ? AND contrasena = ?";
        String sqlVendedor = "SELECT * FROM vendedor WHERE correo_electronico = ? AND contrasena = ?";

        try {
            return jdbcTemplate.queryForObject(sqlCliente, (rs, rowNum) -> new cliente(
                    rs.getInt("id_cliente"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("contrasena"),
                    rs.getString("direccion"),
                    rs.getString("telefono"),
                    rs.getString("correo_electronico")
            ), correo, contrasena);
        } catch (Exception e1) {
            try {
                return jdbcTemplate.queryForObject(sqlVendedor, (rs, rowNum) -> new vendedor(
                        rs.getInt("id_vendedor"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo_electronico"),
                        rs.getString("telefono"),
                        rs.getString("contrasena")
                ), correo, contrasena);
            } catch (Exception e2) {
                return null; // no encontró en ninguna tabla
            }
        }
    }

}