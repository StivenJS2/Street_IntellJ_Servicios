package com.example.demo.Java1.Tablas;

import java.util.List;
import java.util.Map;

public class pedido {
    private int id_pedido;
    private int id_cliente;
    private String fecha_pedido;
    private Double total;
    private String estado;
    private String metodo_pago;
    private String ruta_factura;
    private String numero_factura;
    private List<Map<String, Object>> items; // 👈 NUEVO

    // Constructor completo
    public pedido(int id_pedido, int id_cliente, String fecha_pedido, Double total,
                  String estado, String metodo_pago, String ruta_factura, String numero_factura) {
        this.id_pedido = id_pedido;
        this.id_cliente = id_cliente;
        this.fecha_pedido = fecha_pedido;
        this.total = total;
        this.estado = estado;
        this.metodo_pago = metodo_pago;
        this.ruta_factura = ruta_factura;
        this.numero_factura = numero_factura;
    }

    // Constructor vacío
    public pedido() {
    }

    // Getters y Setters
    public int getId_pedido() { return id_pedido; }
    public void setId_pedido(int id_pedido) { this.id_pedido = id_pedido; }

    public int getId_cliente() { return id_cliente; }
    public void setId_cliente(int id_cliente) { this.id_cliente = id_cliente; }

    public String getFecha_pedido() { return fecha_pedido; }
    public void setFecha_pedido(String fecha_pedido) { this.fecha_pedido = fecha_pedido; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMetodo_pago() { return metodo_pago; }
    public void setMetodo_pago(String metodo_pago) { this.metodo_pago = metodo_pago; }

    public String getRuta_factura() { return ruta_factura; }
    public void setRuta_factura(String ruta_factura) { this.ruta_factura = ruta_factura; }

    public String getNumero_factura() { return numero_factura; }
    public void setNumero_factura(String numero_factura) { this.numero_factura = numero_factura; }

    // 👇 NUEVO: Getter y Setter para items
    public List<Map<String, Object>> getItems() { return items; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }
}