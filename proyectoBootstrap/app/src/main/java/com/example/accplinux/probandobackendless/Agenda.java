package com.example.accplinux.probandobackendless;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by accplinux on 15/01/17.
 */

public class Agenda {

    private String titulo;
    private String localizacion;
    private String description;
    private BackendlessUser usuario;

    Date inicio;
    Date fin;

    public Agenda(String titulo, String localizacion, String description, Date inicio, BackendlessUser usuario){

        this.titulo=titulo;
        this.localizacion=localizacion;
        this.description=description;
        this.inicio=inicio;
        this.usuario=usuario;

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


    public BackendlessUser getUsuario() {
        return usuario;
    }

    public void setUsuario(BackendlessUser usuario) {
        this.usuario = usuario;
    }

}
