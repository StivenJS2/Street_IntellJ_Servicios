package com.example.demo.Java1.Tablas;

public class carrito {
    private int id_carrito;
    private int id_cliente;
    private String fecha_creacion;

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

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }



    public carrito(int id_carrito, int id_cliente, String fecha_creacion) {
        this.id_carrito = id_carrito;
        this.id_cliente = id_cliente;
        this.fecha_creacion = fecha_creacion;

    }
}