package com.example.accplinux.probandobackendless;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by accplinux on 22/11/16.
 */

public class Proyecto implements Serializable{

    private String nombre;
    private String jefeProyecto;
    private Long presupuesto;
    private String cliente;
    private Date fechaInicio;
    private Date fechaFin;
    private Boolean finalizado;
    private String objectId;


    public Proyecto(){

    }

    public Proyecto( String nombre, String jefeProyecto, Long presupuesto, String cliente, Date fechaInicio,Date fechaFin, Boolean finalizado)
    {
        this.nombre = nombre;
        this.jefeProyecto = jefeProyecto;
        this.presupuesto = presupuesto;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.fechaFin=fechaFin;
        this.finalizado=finalizado;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getJefeProyecto() {
        return jefeProyecto;
    }

    public void setJefeProyecto(String jefeProyecto) {
        this.jefeProyecto = jefeProyecto;
    }

    public Long getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Long presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(Boolean finalizado) {
        this.finalizado = finalizado;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
