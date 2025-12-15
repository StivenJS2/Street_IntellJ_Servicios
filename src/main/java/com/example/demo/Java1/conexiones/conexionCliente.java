package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.cliente;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class conexionCliente {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<cliente> obtenerUsuarios() {
        String sql = "SELECT * FROM cliente";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("contrasena"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico")
                )
        );
    }

    public cliente obtenerClientePorId(int id_cliente) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            cliente c = new cliente();
            c.setId_cliente(rs.getInt("id_cliente"));
            c.setNombre(rs.getString("nombre"));
            c.setApellido(rs.getString("apellido"));
            c.setDireccion(rs.getString("direccion"));
            c.setTelefono(rs.getString("telefono"));
            c.setCorreo_electronico(rs.getString("correo_electronico"));
            return c;
        }, id_cliente);
    }

    public void agregarUsuario(cliente Cliente) {
        String sql = """
            INSERT INTO cliente (nombre, apellido, contrasena, direccion, telefono, correo_electronico)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        jdbcTemplate.update(
                sql,
                Cliente.getNombre(),
                Cliente.getApellido(),
                Cliente.getContrasena(),
                Cliente.getDireccion(),
                Cliente.getTelefono(),
                Cliente.getCorreo_electronico()
        );
    }

    public void eliminarCliente(int id_cliente) {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        jdbcTemplate.update(sql, id_cliente);
    }

    public void actualizarCliente(int id_cliente, cliente cliente) {
        String sql = """
            UPDATE cliente SET
                nombre = ?,
                apellido = ?,
                contrasena = ?,
                direccion = ?,
                telefono = ?,
                correo_electronico = ?
            WHERE id_cliente = ?
        """;

        jdbcTemplate.update(
                sql,
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getContrasena(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getCorreo_electronico(),
                id_cliente
        );
    }

    /* ========== PERFIL ========== */

    public Map<String, Object> obtenerPerfilPorCorreo(String correo) {
        String sql = """
            SELECT nombre, apellido, correo_electronico, telefono, direccion
            FROM cliente
            WHERE correo_electronico = ?
        """;
        return jdbcTemplate.queryForMap(sql, correo);
    }

    public void actualizarPerfilPorCorreo(String correo, Map<String, String> datos) {

        boolean cambiaPass = datos.containsKey("contrasena") && !datos.get("contrasena").isBlank();

        String sql = """
            UPDATE cliente SET
                nombre = ?,
                apellido = ?,
                telefono = ?,
                direccion = ?,
                correo_electronico = ?
                %s
            WHERE correo_electronico = ?
        """;

        String finalSql = String.format(
                sql,
                cambiaPass ? ", contrasena = ?" : ""
        );

        if (cambiaPass) {
            jdbcTemplate.update(
                    finalSql,
                    datos.get("nombre"),
                    datos.get("apellido"),
                    datos.get("telefono"),
                    datos.get("direccion"),
                    datos.get("correo_electronico"),
                    datos.get("contrasena"),
                    correo
            );
        } else {
            jdbcTemplate.update(
                    finalSql,
                    datos.get("nombre"),
                    datos.get("apellido"),
                    datos.get("telefono"),
                    datos.get("direccion"),
                    datos.get("correo_electronico"),
                    correo
            );
        }
    }
}
