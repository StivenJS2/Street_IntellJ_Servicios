package com.example.demo.Java1.controladores;

import com.example.demo.Java1.conexiones.conexionFavorito;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Favoritos", description = "Gestión de productos favoritos del cliente")
public class controladorFavorito {

    @Autowired
    private conexionFavorito Conexion;

    // GET /favorito/{id_cliente}
    @GetMapping("/favorito/{id_cliente}")
    @Operation(summary = "Obtener favoritos", description = "Lista todos los favoritos de un cliente con datos del producto")
    public List<Map<String, Object>> obtenerFavoritos(@PathVariable int id_cliente) {
        return Conexion.obtenerFavoritos(id_cliente);
    }

    // POST /favorito/{id_cliente}/{id_producto}
    @PostMapping("/favorito/{id_cliente}/{id_producto}")
    @Operation(summary = "Agregar favorito", description = "Agrega un producto a los favoritos del cliente")
    public ResponseEntity<String> agregarFavorito(
            @PathVariable int id_cliente,
            @PathVariable int id_producto) {
        try {
            Conexion.agregarFavorito(id_cliente, id_producto);
            return ResponseEntity.ok("Producto agregado a favoritos.");
        } catch (Exception e) {
            // Captura el UNIQUE KEY duplicado
            return ResponseEntity.badRequest().body("El producto ya está en favoritos.");
        }
    }

    // DELETE /favorito/{id_favorito}
    @DeleteMapping("/favorito/{id_favorito}")
    @Operation(summary = "Eliminar favorito", description = "Elimina un producto de los favoritos")
    public String eliminarFavorito(@PathVariable int id_favorito) {
        Conexion.eliminarFavorito(id_favorito);
        return "Favorito eliminado correctamente.";
    }

    // GET /favorito/verificar/{id_cliente}/{id_producto}
    @GetMapping("/favorito/verificar/{id_cliente}/{id_producto}")
    @Operation(summary = "Verificar favorito", description = "Indica si un producto ya es favorito del cliente")
    public Map<String, Object> verificarFavorito(
            @PathVariable int id_cliente,
            @PathVariable int id_producto) {
        boolean resultado = Conexion.esFavorito(id_cliente, id_producto);
        return Map.of("esFavorito", resultado);
    }
}