package com.example.demo.Java1.Tablas;

public class detalle_producto {
    private int id_detalle_producto;
    private String talla;
    private String color;
    private int id_producto;
    private int id_categoria;
    private double precio;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public detalle_producto(int id_detalle_producto, String talla, String color, int id_producto, int id_categoria, double precio) {
        this.id_detalle_producto = id_detalle_producto;
        this.talla = talla;
        this.color = color;
        this.id_producto = id_producto;
        this.id_categoria = id_categoria;
        this.precio = precio;


    }
}
