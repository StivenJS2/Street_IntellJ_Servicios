package com.example.demo.Java1.Tablas;

public class carrito {
    private int id_carrito;
    private int id_cliente;
    private int id_detalle_producto;
    private int cantidad;
    private Double precio_unitario;
    private  Double subtotal;

    public carrito() {}


    public int getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_detalle_producto() {
        return id_detalle_producto;
    }

    public void setId_detalle_producto(int id_detalle_producto) {
        this.id_detalle_producto = id_detalle_producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_untario(Double precio_untario) {
        this.precio_unitario = precio_untario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public carrito(int id_carrito, int id_cliente, int id_detalle_producto, int cantidad, Double precio_untario, Double subtotal) {
        this.id_carrito = id_carrito;
        this.id_cliente = id_cliente;
        this.id_detalle_producto = id_detalle_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_untario;
        this.subtotal = subtotal;



    }
}