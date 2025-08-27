package com.example.demo.Java1.Tablas;

public class producto {
    private int id_producto;
    private String nombre;
    private String descripcion;
    private String estado;
    private int stock;
    private Double precio;

public producto(int id_producto, String nombre, String descripcion, String estado, int stock, Double precio) {
    this.id_producto = id_producto;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.estado = estado;
    this.stock = stock;
    this.precio = precio;
}

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}

