package com.example.demo.Java1.Tablas;

public class producto {
    private int id_producto;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private String imagen;
    private int id_vendedor;
    private String estado;
    private double precio;
    private String color;
    private int id_categoria;

    public producto() {
    }//  para poder usar la barra de busqueda

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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId_vendedor() {
        return id_vendedor;
    }

    public void setId_vendedor(int id_vendedor) {
        this.id_vendedor = id_vendedor;
    }

    public String getEstado() {return estado;}

    public void setEstado(String estado) {this.estado = estado; }

    public double getPrecio() {return precio; }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getColor() {return color; }

    public void setColor(String color) {this.color = color; }

    public int getId_categoria() {return id_categoria; }

    public void setId_categoria(int categoria) {
        this.id_categoria = categoria;  //  usa el parámetro recibido
    }

    public producto(int id_producto, String nombre, String descripcion, int cantidad, String imagen, int id_vendedor, String estado, double precio, String color, int id_categoria) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.imagen = imagen;
        this.id_vendedor = id_vendedor;
        this.estado = estado;
        this.precio = precio;
        this.color = color;
        this.id_categoria = id_categoria;

    }
}