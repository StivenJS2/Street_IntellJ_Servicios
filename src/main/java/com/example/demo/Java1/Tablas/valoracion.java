package com.example.demo.Java1.Tablas;

public class valoracion {
    private int id_valoracion;
    private int id_cliente;
    private int id_producto;
    private int calificacion;
    private String comentario;
    private String fecha_valoracion;

    public valoracion(int id_valoracion, int id_cliente, int id_producto, int calificacion, String comentario, String fecha_valoracion) {
        this.id_valoracion = id_valoracion;
        this.id_cliente = id_cliente;
        this.id_producto = id_producto;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha_valoracion = fecha_valoracion;
    }

    public int getId_valoracion() {
        return id_valoracion;
    }

    public void setId_valoracion(int id_valoracion) {
        this.id_valoracion = id_valoracion;
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

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha_valoracion() {
        return fecha_valoracion;
    }

    public void setFecha_valoracion(String fecha_valoracion) {
        this.fecha_valoracion = fecha_valoracion;
    }
}
