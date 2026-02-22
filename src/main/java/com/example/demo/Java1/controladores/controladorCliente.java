package com.example.demo.Java1.controladores;

import com.example.demo.Java1.Tablas.cliente;
import com.example.demo.Java1.conexiones.conexionCliente;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cliente")
public class controladorCliente {

    @Autowired
    private conexionCliente conexion;

    /* ===== ADMIN ===== */

    @GetMapping
    @Operation(summary = "Obtener clientes")
    public List<cliente> obtenerUsuarios() {
        return conexion.obtenerUsuarios();
    }

    @GetMapping("/{id_cliente}")
    @Operation(summary = "Obtener cliente por ID")
    public cliente obtenerClientePorId(@PathVariable int id_cliente) {
        return conexion.obtenerClientePorId(id_cliente);
    }

    @PostMapping
    @Operation(summary = "Agregar cliente")
    public String agregarUsuario(@RequestBody cliente cliente) {
        conexion.agregarUsuario(cliente);
        return "Cliente registrado correctamente.";
    }

    @PutMapping("/{id_cliente}")
    @Operation(summary = "Actualizar cliente (admin)")
    public String actualizarCliente(
            @PathVariable int id_cliente,
            @RequestBody cliente cliente
    ) {
        conexion.actualizarCliente(id_cliente, cliente);
        return "Cliente actualizado con éxito.";
    }

    @DeleteMapping("/{id_cliente}")
    @Operation(summary = "Eliminar cliente")
    public String eliminarCliente(@PathVariable int id_cliente) {
        conexion.eliminarCliente(id_cliente);
        return "Cliente eliminado correctamente.";
    }

    /* ===== PERFIL CLIENTE ===== */

    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del cliente autenticado")

    public Map<String, Object> obtenerPerfil(Authentication authentication) {
        System.out.println("AUTH OBJECT: " + authentication);
        System.out.println("AUTH NAME: " + authentication.getName());
        System.out.println("AUTHORITIES: " + authentication.getAuthorities());
        String correo = authentication.getName();
        return conexion.obtenerPerfilPorCorreo(correo);
    }

    @PutMapping("/perfil")
    @Operation(summary = "Actualizar perfil del cliente autenticado")
    public String actualizarPerfil(
            Authentication authentication,
            @RequestBody Map<String, String> datos
    ) {
        String correo = authentication.getName();
        conexion.actualizarPerfilPorCorreo(correo, datos);
        return "Perfil actualizado correctamente.";
    }

// unicamente para la barra de busqueda
    @GetMapping("/cliente/buscar")
    public List<cliente> buscarCliente(@RequestParam String dato) {
        return conexion.buscarPorDatos(dato);
    }
}
