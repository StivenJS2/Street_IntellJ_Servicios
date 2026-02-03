package com.example.demo.Java1.Tablas;

public class detalle_producto {
    private int id_detalle_producto;
    private String talla;
    private String imagen;
    private int id_producto;
    private int id_categoria;
    private int cantidad;

    public int getId_detalle_producto() {
        return id_detalle_producto;
    }

    public void setId_detalle_producto(int id_detalle_producto) {
        this.id_detalle_producto = id_detalle_producto;
    }

    public String getTalla() {
        return talla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public int getCantidad() {return cantidad; }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public detalle_producto(int id_detalle_producto, String talla, String imagen, int id_producto, int id_categoria, int cantidad) {
        this.id_detalle_producto = id_detalle_producto;
        this.talla = talla;
        this.imagen = imagen;
        this.id_producto = id_producto;
        this.id_categoria = id_categoria;
        this.cantidad = cantidad;

    }
}
