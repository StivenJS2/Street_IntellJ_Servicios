package com.example.demo.Java1.conexiones;

import com.example.demo.Java1.Tablas.carrito;
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
public class conexionCarrito {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<carrito> obtenerCarrito() {
        String sql = "SELECT * FROM carrito";
        return jdbcTemplate.query(sql, new RowMapper<carrito>() {

            @Override
            public carrito mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new carrito(

                        rs.getInt("id_carrito"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_detalle_producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario"),
                        rs.getDouble("subtotal")
                );
            }
        });
    }


    public void agregarCarrito(carrito Carrito ) {
        String sql = "INSERT INTO carrito (id_cliente,id_detalle_producto,cantidad,precio_unitario,subtotal ) " +
                "VALUES (?, ?, ?, ?,?)";
        jdbcTemplate.update(sql,
                Carrito.getId_cliente(),
                Carrito.getId_detalle_producto(),
                Carrito.getCantidad(),
                Carrito.getPrecio_unitario(),
                Carrito.getSubtotal()

        );
    }


    @DeleteMapping("/carrito/{id_carrito}")
    public void eliminarCarrito(int id_carrito) {
        String sql = "DELETE FROM carrito WHERE id_carrito = ?";
        jdbcTemplate.update(sql, id_carrito);
    }

    @PutMapping("/carrito/{id_carrito}")
    public void actualizarCarrito(@PathVariable int id_carrito, @RequestBody carrito Carrito ) {
        String sql = "UPDATE carrito SET  id_cliente = ?,id_detalle_producto=?,cantidad=?,precio_unitario=?,subtotal=? WHERE id_carrito = ?";
        jdbcTemplate.update(sql,
                Carrito.getId_cliente(),
               Carrito.getId_detalle_producto(),
                Carrito.getCantidad(),
               Carrito.getPrecio_unitario(),
                Carrito.getSubtotal(),
                id_carrito);

    }


}
