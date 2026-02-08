package com.example.demo.Java1.Tablas;

public class detalle_carrito {
    private int id_detalle_carrito;
    private int id_carrito;
    private int id_detalle_producto;
    private int cantidad;
    private Double precio_unitario;
    private Double subtotal;
    private String fecha_agregado;

    public detalle_carrito() {}

    public int getId_detalle_carrito() {
        return id_detalle_carrito;
    }

    public void setId_detalle_carrito(int id_detalle_carrito) {
        this.id_detalle_carrito = id_detalle_carrito;
    }

    public int getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
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

    public void setPrecio_unitario(Double precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getFecha_agregado() {
        return fecha_agregado;
    }

    public void setFecha_agregado(String fecha_agregado) {
        this.fecha_agregado = fecha_agregado;
    }

    public detalle_carrito(int id_detalle_carrito, int id_carrito, int id_detalle_producto, int cantidad, Double precio_unitario, Double subtotal, String fecha_agregado){
        this.id_detalle_carrito = id_detalle_carrito;
        this.id_carrito = id_carrito;
        this.id_detalle_producto = id_detalle_producto;
        this.cantidad = cantidad;
        this.precio_unitario = precio_unitario;
        this.subtotal = subtotal;
        this.fecha_agregado = fecha_agregado;
    }
}
