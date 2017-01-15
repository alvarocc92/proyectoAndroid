package com.example.accplinux.probandobackendless;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by accplinux on 15/01/17.
 */

public class Agenda {

    private String titulo;
    private String localizacion;
    private String description;

    Date inicio;
    Date fin;



    public Agenda(String titulo, String localizacion, String description, Date inicio){

        this.titulo=titulo;
        this.localizacion=localizacion;
        this.description=description;
        this.inicio=inicio;

    }

    public Agenda(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
