package com.example.demo.Java1.Tablas;

public class favorito {

    private int id_favorito;
    private int id_cliente;
    private int id_producto;
    private String fecha_agregado;

    public favorito() {
    }

    public int getId_favorito() {
        return id_favorito;
    }

    public void setId_favorito(int id_favorito) {
        this.id_favorito = id_favorito;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getFecha_agregado() {
        return fecha_agregado;
    }

    public void setFecha_agregado(String fecha_agregado) {
        this.fecha_agregado = fecha_agregado;
    }

    public favorito(int id_favorito, int id_cliente, int id_producto, String fecha_agregado) {
        this.id_favorito     = id_favorito;
        this.id_cliente      = id_cliente;
        this.id_producto     = id_producto;
        this.fecha_agregado  = fecha_agregado;
    }
}