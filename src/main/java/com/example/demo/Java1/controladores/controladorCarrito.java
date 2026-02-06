package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.carrito;
import com.example.demo.Java1.conexiones.conexionCarrito;
import com.example.demo.Java1.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.*;

@RestController
public class controladorCarrito {

    @Autowired
    private conexionCarrito Conexioncarrito;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/carrito")
    @Operation(summary = "Obtener carritos",
            description = "Devuelve una lista con todos los carritos existentes")
    public List<carrito> obtenerCarrito() {
        return Conexioncarrito.obtenerCarrito();
    }

    @GetMapping("/carrito/cliente/{id_cliente}")
    @Operation(summary = "Obtener carritos por cliente",
            description = "Devuelve una lista con el carrito especificado por id")
    public List<carrito> obtenerCarritoPorCliente(@PathVariable int id_cliente) {
        return Conexioncarrito.obtenerCarritoPorCliente(id_cliente);
    }

    // 🆕 Obtener MI carrito con JWT (con detalles de productos)
    @GetMapping("/carrito/mis-productos")
    @Operation(summary = "Obtener mi carrito con detalles",
            description = "Devuelve el carrito del usuario autenticado con información de productos")
    public Map<String, Object> obtenerMiCarrito(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            List<Map<String, Object>> items = Conexioncarrito.obtenerCarritoConDetalles(idCliente);
            double total = Conexioncarrito.calcularTotalCarrito(idCliente);
            int cantidadItems = Conexioncarrito.contarItemsCarrito(idCliente);

            respuesta.put("items", items);
            respuesta.put("total", total);
            respuesta.put("cantidad_items", cantidadItems);

        } catch (Exception e) {
            respuesta.put("error", e.getMessage());
            respuesta.put("items", new ArrayList<>());
            respuesta.put("total", 0);
            respuesta.put("cantidad_items", 0);
        }

        return respuesta;
    }

    // 🆕 Agregar producto al carrito con JWT
    @PostMapping("/carrito/agregar")
    @Operation(summary = "Agregar producto al carrito",
            description = "Agrega un producto al carrito del usuario autenticado")
    public Map<String, Object> agregarAlCarrito(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> datos
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int idProducto = Integer.parseInt(datos.get("id_producto").toString());
            String talla = (String) datos.get("talla");
            int cantidad = datos.containsKey("cantidad") ?
                    Integer.parseInt(datos.get("cantidad").toString()) : 1;
            double precio = Double.parseDouble(datos.get("precio").toString());

            // Obtener id_detalle_producto
            Integer idDetalleProducto = Conexioncarrito.obtenerIdDetalleProducto(idProducto, talla);

            if (idDetalleProducto == null) {
                respuesta.put("success", false);
                respuesta.put("mensaje", "No se encontró el producto con esa talla");
                return respuesta;
            }

            int resultado = Conexioncarrito.agregarOActualizarCarrito(idCliente, idDetalleProducto, cantidad, precio);

            if (resultado > 0) {
                respuesta.put("success", true);
                respuesta.put("mensaje", "Producto agregado al carrito");
                respuesta.put("cantidad_items", Conexioncarrito.contarItemsCarrito(idCliente));
            } else {
                respuesta.put("success", false);
                respuesta.put("mensaje", "Error al agregar al carrito");
            }

        } catch (Exception e) {
            respuesta.put("success", false);
            respuesta.put("mensaje", "Error: " + e.getMessage());
        }

        return respuesta;
    }

    // 🆕 Contador del carrito
    @GetMapping("/carrito/contador")
    @Operation(summary = "Obtener contador del carrito",
            description = "Devuelve la cantidad de items en el carrito")
    public Map<String, Object> obtenerContador(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int cantidad = Conexioncarrito.contarItemsCarrito(idCliente);
            respuesta.put("cantidad", cantidad);

        } catch (Exception e) {
            respuesta.put("cantidad", 0);
        }

        return respuesta;
    }

    // 🆕 Actualizar cantidad de un item
    @PutMapping("/carrito/actualizar-cantidad")
    @Operation(summary = "Actualizar cantidad de item",
            description = "Actualiza la cantidad de un item específico del carrito")
    public Map<String, Object> actualizarCantidadItem(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> datos
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int idCarrito = Integer.parseInt(datos.get("id_carrito").toString());
            int cantidad = Integer.parseInt(datos.get("cantidad").toString());

            int resultado = Conexioncarrito.actualizarCantidadItem(idCarrito, cantidad);

            respuesta.put("success", resultado > 0);

            if (resultado > 0) {
                respuesta.put("total", Conexioncarrito.calcularTotalCarrito(idCliente));
            }

        } catch (Exception e) {
            respuesta.put("success", false);
            respuesta.put("mensaje", e.getMessage());
        }

        return respuesta;
    }

    // 🆕 Eliminar item específico
    @DeleteMapping("/carrito/eliminar-item/{idCarrito}")
    @Operation(summary = "Eliminar item del carrito",
            description = "Elimina un item específico del carrito")
    public Map<String, Object> eliminarItem(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int idCarrito
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            Conexioncarrito.eliminarCarrito(idCarrito);

            respuesta.put("success", true);
            respuesta.put("cantidad_items", Conexioncarrito.contarItemsCarrito(idCliente));
            respuesta.put("total", Conexioncarrito.calcularTotalCarrito(idCliente));

        } catch (Exception e) {
            respuesta.put("success", false);
        }

        return respuesta;
    }

    // 🆕 Vaciar carrito completo
    @DeleteMapping("/carrito/vaciar")
    @Operation(summary = "Vaciar carrito",
            description = "Elimina todos los items del carrito del usuario")
    public Map<String, Object> vaciarCarrito(@RequestHeader("Authorization") String authHeader) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int resultado = Conexioncarrito.vaciarCarritoCliente(idCliente);

            respuesta.put("success", resultado > 0);

        } catch (Exception e) {
            respuesta.put("success", false);
        }

        return respuesta;
    }

    // Métodos originales
    @PostMapping("/carrito")
    @Operation(summary = "Agregar Carritos",
            description = "Crea un nuevo carrito")
    public String agregarCarrito(@RequestBody carrito Carrito) {
        Conexioncarrito.agregarCarrito(Carrito);
        return "carrito creado correctamente.";
    }

    @DeleteMapping("/carrito/{id_carrito}")
    @Operation(summary = "Eliminar Carritos",
            description = "Elimina un carrito")
    public String eliminarCarrito(@PathVariable int id_carrito) {
        Conexioncarrito.eliminarCarrito(id_carrito);
        return "carrito eliminado correctamente.";
    }

    @PutMapping("/carrito/{id_carrito}")
    @Operation(summary = "Actualizar Carritos",
            description = "Actualiza un carrito")
    public String actualizarCarrito(@PathVariable int id_carrito, @RequestBody carrito Carrito) {
        Conexioncarrito.actualizarCarrito(id_carrito, Carrito);
        return "carrito actualizado con exito";
    }
}