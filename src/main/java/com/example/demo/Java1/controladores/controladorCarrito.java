package com.example.demo.Java1.controladores;

import com.example.demo.Java1.conexiones.conexionCarrito;
import com.example.demo.Java1.conexiones.conexionDetalle_carrito;
import com.example.demo.Java1.seguridad.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class controladorCarrito {

    @Autowired
    private conexionCarrito conexionCarrito;

    @Autowired
    private conexionDetalle_carrito conexionDetalleCarrito;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/carrito/agregar")
    @Operation(summary = "Agregar producto al carrito")
    public Map<String, Object> agregarAlCarrito(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> datos
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            System.out.println("═══════════════════════════════════════");
            System.out.println("🛒 AGREGAR AL CARRITO - INICIO");
            System.out.println("═══════════════════════════════════════");
            System.out.println("📦 Datos recibidos: " + datos);

            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            System.out.println("👤 ID Cliente desde JWT: " + idCliente);

            // Validar que vienen los datos necesarios
            if (!datos.containsKey("id_detalle_producto")) {
                respuesta.put("success", false);
                respuesta.put("mensaje", "Falta id_detalle_producto");
                return respuesta;
            }

            if (!datos.containsKey("precio")) {
                respuesta.put("success", false);
                respuesta.put("mensaje", "Falta precio");
                return respuesta;
            }

            int idDetalleProducto = Integer.parseInt(datos.get("id_detalle_producto").toString());
            int cantidad = datos.containsKey("cantidad") ?
                    Integer.parseInt(datos.get("cantidad").toString()) : 1;
            double precio = Double.parseDouble(datos.get("precio").toString());

            System.out.println("📋 Parámetros procesados:");
            System.out.println("   - ID Detalle Producto: " + idDetalleProducto);
            System.out.println("   - Cantidad: " + cantidad);
            System.out.println("   - Precio: " + precio);

            // Agregar al carrito
            System.out.println("💾 Llamando a conexionDetalleCarrito.agregarProducto...");
            int resultado = conexionDetalleCarrito.agregarProducto(idCliente, idDetalleProducto, cantidad, precio);

            System.out.println("✅ Resultado de inserción/actualización: " + resultado);

            if (resultado > 0) {
                int cantidadItems = conexionDetalleCarrito.contarItems(idCliente);
                System.out.println("🔢 Cantidad total de items en carrito: " + cantidadItems);

                respuesta.put("success", true);
                respuesta.put("mensaje", "Producto agregado al carrito");
                respuesta.put("cantidad_items", cantidadItems);

                System.out.println("✅ Respuesta exitosa: " + respuesta);
            } else {
                respuesta.put("success", false);
                respuesta.put("mensaje", "Error al agregar al carrito");
                System.out.println("❌ Error: resultado = 0");
            }

            System.out.println("═══════════════════════════════════════");
            System.out.println("🛒 AGREGAR AL CARRITO - FIN");
            System.out.println("═══════════════════════════════════════");

        } catch (Exception e) {
            System.err.println("═══════════════════════════════════════");
            System.err.println("❌ ERROR EN AGREGAR AL CARRITO");
            System.err.println("═══════════════════════════════════════");
            System.err.println("Mensaje: " + e.getMessage());
            System.err.println("Causa: " + e.getCause());
            e.printStackTrace();
            System.err.println("═══════════════════════════════════════");

            respuesta.put("success", false);
            respuesta.put("mensaje", "Error: " + e.getMessage());
        }

        return respuesta;
    }

    // 🆕 Obtener carrito con productos
    @GetMapping("/carrito/mis-productos")
    @Operation(summary = "Obtener mi carrito")
    public Map<String, Object> obtenerMiCarrito(
            @RequestHeader("Authorization") String authHeader
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            List<Map<String, Object>> items = conexionDetalleCarrito.obtenerCarritoConProductos(idCliente);
            double total = conexionDetalleCarrito.calcularTotal(idCliente);
            int cantidadItems = conexionDetalleCarrito.contarItems(idCliente);

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

    // 🆕 Contador del carrito
    @GetMapping("/carrito/contador")
    @Operation(summary = "Obtener contador del carrito")
    public Map<String, Object> obtenerContador(
            @RequestHeader("Authorization") String authHeader
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int cantidad = conexionDetalleCarrito.contarItems(idCliente);
            respuesta.put("cantidad", cantidad);

        } catch (Exception e) {
            respuesta.put("cantidad", 0);
        }

        return respuesta;
    }

    // 🆕 Actualizar cantidad
    @PutMapping("/carrito/actualizar")
    @Operation(summary = "Actualizar cantidad de item")
    public Map<String, Object> actualizarCantidad(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> datos
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int idDetalleCarrito = Integer.parseInt(datos.get("id_carrito").toString());
            int cantidad = Integer.parseInt(datos.get("cantidad").toString());

            int resultado = conexionDetalleCarrito.actualizarCantidad(idDetalleCarrito, cantidad);

            respuesta.put("success", resultado > 0);

            if (resultado > 0) {
                respuesta.put("total", conexionDetalleCarrito.calcularTotal(idCliente));
            }

        } catch (Exception e) {
            respuesta.put("success", false);
            respuesta.put("mensaje", e.getMessage());
        }

        return respuesta;
    }

    // 🆕 Eliminar item
    @DeleteMapping("/carrito/eliminar/{idDetalleCarrito}")
    @Operation(summary = "Eliminar item del carrito")
    public Map<String, Object> eliminarItem(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable int idDetalleCarrito
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int resultado = conexionDetalleCarrito.eliminarItem(idDetalleCarrito);

            respuesta.put("success", resultado > 0);

            if (resultado > 0) {
                respuesta.put("cantidad_items", conexionDetalleCarrito.contarItems(idCliente));
                respuesta.put("total", conexionDetalleCarrito.calcularTotal(idCliente));
            }

        } catch (Exception e) {
            respuesta.put("success", false);
        }

        return respuesta;
    }

    // 🆕 Vaciar carrito
    @DeleteMapping("/carrito/vaciar")
    @Operation(summary = "Vaciar carrito completo")
    public Map<String, Object> vaciarCarrito(
            @RequestHeader("Authorization") String authHeader
    ) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            String token = authHeader.replace("Bearer ", "");
            int idCliente = jwtUtil.obtenerIdUsuarioDeToken(token);

            int resultado = conexionDetalleCarrito.vaciarCarrito(idCliente);

            respuesta.put("success", resultado >= 0); // >= 0 porque puede ser 0 si ya estaba vacío

        } catch (Exception e) {
            respuesta.put("success", false);
        }

        return respuesta;
    }
}