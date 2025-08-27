package com.example.demo.Java1.Tablas;

public class detalle_carrito {
    private int id_detalle_carrito;
    private String cantidad;
    private String id_cliente;

    public detalle_carrito(int id_detalle_carrito, String cantidad, String id_cliente) {
        this.id_detalle_carrito = id_detalle_carrito;
        this.cantidad = cantidad;
        this.id_cliente = id_cliente;
    }

    public int getId_detalle_carrito() {
        return id_detalle_carrito;
    }

    public void setId_detalle_carrito(int id_detalle_carrito) {
        this.id_detalle_carrito = id_detalle_carrito;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(String id_cliente) {
        this.id_cliente = id_cliente;
    }
}
