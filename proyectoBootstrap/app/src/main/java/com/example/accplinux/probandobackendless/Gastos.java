package com.example.accplinux.probandobackendless;

import java.io.Serializable;

/**
 * Created by accplinux on 10/01/17.
 */

public class Gastos implements Serializable {

    Proyecto proyecto;
    Float cantidad;
    String descripcion;

    public Gastos(){

    }

    public Gastos (String descripcion, Float cantidad, Proyecto proyecto){
        this.descripcion=descripcion;
        this.cantidad=cantidad;
        this.proyecto=proyecto;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getCantidad() {
        return cantidad;
    }

    public void setCantidad(Float cantidad) {
        this.cantidad = cantidad;
    }
}
