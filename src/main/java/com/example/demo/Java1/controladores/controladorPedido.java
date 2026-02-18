package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.pedido;
import com.example.demo.Java1.conexiones.conexionPedido;
import com.example.demo.Java1.servicios.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class controladorPedido {

    @Autowired
    private conexionPedido Conexionpedido;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/pedido")
    @Operation(summary = "Obtener pedidos",
            description = "Devuelve una lista con todos los pedidos existentes")
    public List<pedido> obtenerPedido() {
        return Conexionpedido.obtenerPedido();
    }

    /**
     * ✅ NUEVO ENDPOINT: Confirmar pedido con validación de stock
     */
    @PostMapping("/pedido/confirmar")
    @Operation(summary = "Confirmar pedido con validación de stock",
            description = "Valida el stock, crea el pedido, descuenta inventario y genera factura")
    public ResponseEntity<Map<String, Object>> confirmarPedido(@RequestBody pedido Pedido) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            // 1️⃣ Validar y confirmar pedido (esto valida stock y descuenta)
            Map<String, Object> resultadoConfirmacion = Conexionpedido.confirmarPedido(Pedido, Pedido.getId_cliente());

            // 2️⃣ Verificar si hubo errores de stock
            if (!(boolean) resultadoConfirmacion.get("exito")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultadoConfirmacion);
            }

            // 3️⃣ Obtener el ID del pedido recién creado
            pedido pedidoCreado = Conexionpedido.obtenerUltimoPedidoPorCliente(Pedido.getId_cliente());

            if (pedidoCreado == null) {
                respuesta.put("exito", false);
                respuesta.put("mensaje", "Error al obtener el pedido creado");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
            }

            // 4️⃣ Actualizar el objeto pedido con el ID
            Pedido.setId_pedido(pedidoCreado.getId_pedido());

            // 5️⃣ Generar la factura en PDF
            String rutaFactura = facturaService.generarFactura(Pedido);

            // 6️⃣ Actualizar la ruta de la factura en la base de datos
            if (rutaFactura != null) {
                Conexionpedido.actualizarRutaFactura(Pedido.getId_pedido(), rutaFactura);
                System.out.println("✅ Factura generada y guardada: " + rutaFactura);
            }

            // 7️⃣ Retornar respuesta exitosa
            respuesta.put("exito", true);
            respuesta.put("id_pedido", Pedido.getId_pedido());
            respuesta.put("numero_factura", Pedido.getNumero_factura());
            respuesta.put("ruta_factura", rutaFactura);
            respuesta.put("mensaje", "Pedido confirmado y factura generada exitosamente");

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            System.err.println("❌ Error al confirmar pedido: " + e.getMessage());
            e.printStackTrace();

            respuesta.put("exito", false);
            respuesta.put("mensaje", "Error al procesar el pedido: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }

    /**
     * ⚠️ DEPRECADO: Usar /pedido/confirmar en su lugar
     * Este endpoint NO valida stock
     */
    @PostMapping("/pedido")
    @Operation(summary = "Agregar pedidos (DEPRECADO - usar /pedido/confirmar)",
            description = "Crea un nuevo pedido y genera la factura automáticamente. NO VALIDA STOCK.")
    @Deprecated
    public ResponseEntity<Object> agregarPedido(@RequestBody pedido Pedido) {
        try {
            // 1️⃣ Guardar el pedido en la base de datos
            Conexionpedido.agregarPedido(Pedido);

            // 2️⃣ Obtener el ID del pedido recién creado
            List<pedido> pedidos = Conexionpedido.obtenerPedido();
            int idPedidoNuevo = pedidos.get(pedidos.size() - 1).getId_pedido();

            // 3️⃣ Actualizar el objeto pedido con el ID
            Pedido.setId_pedido(idPedidoNuevo);

            // 4️⃣ Generar la factura en PDF
            String rutaFactura = facturaService.generarFactura(Pedido);

            // 5️⃣ Actualizar la ruta de la factura en la base de datos
            if (rutaFactura != null) {
                Conexionpedido.actualizarRutaFactura(idPedidoNuevo, rutaFactura);
                System.out.println("✅ Factura generada y guardada: " + rutaFactura);
            }

            // 6️⃣ Retornar respuesta con el ID del pedido
            return ResponseEntity.ok()
                    .body(new HashMap<String, Object>() {{
                        put("id_pedido", idPedidoNuevo);
                        put("numero_factura", Pedido.getNumero_factura());
                        put("ruta_factura", rutaFactura);
                        put("mensaje", "Pedido registrado correctamente");
                    }});

        } catch (Exception e) {
            System.err.println("❌ Error al agregar pedido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<String, String>() {{
                        put("error", "Error al procesar el pedido: " + e.getMessage());
                    }});
        }
    }

    @DeleteMapping("/pedido/{id_pedido}")
    @Operation(summary = "Eliminar pedidos",
            description = "Elimina un pedido")
    public String eliminarPedido(@PathVariable int id_pedido) {
        Conexionpedido.eliminarPedido(id_pedido);
        return "Pedido eliminado correctamente.";
    }

    @GetMapping("/pedido/{id_pedido}/factura")
    @Operation(summary = "Descargar factura",
            description = "Descarga la factura en PDF de un pedido específico")
    public ResponseEntity<Resource> descargarFactura(@PathVariable int id_pedido) {
        try {
            String rutaFactura = Conexionpedido.obtenerRutaFactura(id_pedido);

            if (rutaFactura == null || rutaFactura.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Path archivoPath = Paths.get(rutaFactura);

            if (!Files.exists(archivoPath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(archivoPath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            pedido pedido = Conexionpedido.obtenerPedidoPorId(id_pedido);
            String nombreArchivo = pedido.getNumero_factura() + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + nombreArchivo + "\"")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/pedido/{id_pedido}/factura/ver")
    @Operation(summary = "Ver factura en el navegador",
            description = "Muestra la factura en PDF directamente en el navegador")
    public ResponseEntity<Resource> verFactura(@PathVariable int id_pedido) {
        try {
            String rutaFactura = Conexionpedido.obtenerRutaFactura(id_pedido);

            if (rutaFactura == null || rutaFactura.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Path archivoPath = Paths.get(rutaFactura);

            if (!Files.exists(archivoPath)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(archivoPath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(resource);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}