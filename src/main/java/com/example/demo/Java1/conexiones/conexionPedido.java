package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.pedido;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class conexionPedido {


    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                        rs.getString("estado")
                );
            }
        });
    }


    public void agregarPedido(pedido Pedido ) {
        String sql = "INSERT INTO pedido (id_cliente,fecha_pedido,total,estado ) " +
                "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                Pedido.getId_cliente(),
                Pedido.getFecha_pedido(),
                Pedido.getTotal(),
                Pedido.getEstado()

        );
    }


    @DeleteMapping("/pedido/{id_pedido}")
    public void eliminarPedido(int id_pedido) {
        String sql = "DELETE FROM pedido WHERE id_pedido = ?";
        jdbcTemplate.update(sql, id_pedido);
    }

    @PutMapping("/pedido/{id_pedido}")
    public void actualizarPedido(@PathVariable int id_pedido, @RequestBody pedido Pedido ) {
        String sql = "UPDATE pedido SET id_cliente = ?, fecha_pedido = ?,total=?,estado=? WHERE id_pedido = ?";
        jdbcTemplate.update(sql,
                Pedido.getId_cliente(),
                Pedido.getFecha_pedido(),
                Pedido.getTotal(),
                Pedido.getEstado(),
                id_pedido);

    }
}
