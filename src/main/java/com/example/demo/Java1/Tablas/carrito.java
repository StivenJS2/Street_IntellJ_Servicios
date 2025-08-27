package com.example.demo.Java1.Tablas;

public class carrito {
    private int id_carrito;
    private int id_detalle_carrito;
    private int id_producto;
    private int id_pedido;

    public carrito(int id_carrito, int id_detalle_carrito, int id_producto, int id_pedido) {
        this.id_carrito = id_carrito;
        this.id_detalle_carrito = id_detalle_carrito;
        this.id_producto = id_producto;
        this.id_pedido = id_pedido;
    }

    public int getId_carrito() {
        return id_carrito;
    }

    public void setId_carrito(int id_carrito) {
        this.id_carrito = id_carrito;
    }

    public int getId_detalle_carrito() {
        return id_detalle_carrito;
    }

    public void setId_detalle_carrito(int id_detalle_carrito) {
        this.id_detalle_carrito = id_detalle_carrito;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_pedido() {
        return id_pedido;
    }

    public void setId_pedido(int id_pedido) {
        this.id_pedido = id_pedido;
    }
}