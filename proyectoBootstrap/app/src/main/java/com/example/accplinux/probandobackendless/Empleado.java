package com.example.accplinux.probandobackendless;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by accplinux on 5/11/16.
 */

public class Empleado implements Serializable{

    private String nombre;
    private String apellidos;
    private String email;
    private String movil;
    private String direccion;
    private String objectId;
    private Boolean antiguo;
    private BigDecimal salario;
    private Proyecto proyecto;
    private Date fechaContratacion;
    private Boolean desasignado;


    public Empleado(){

    }


    public Empleado( String nombre, String apellidos,String email,String movil,String direccion,String objectId,BigDecimal salario,Boolean antiguo, Boolean desasignado, Date fechaContratacion)
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.movil = movil;
        this.direccion = direccion;
        this.objectId=objectId;
        this.salario = salario;
        this.antiguo=antiguo;
        this.desasignado = desasignado;
        this.fechaContratacion = fechaContratacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Boolean getAntiguo() {
        return antiguo;
    }

    public void setAntiguo(Boolean antiguo) {
        this.antiguo = antiguo;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Boolean getDesasignado() {
        return desasignado;
    }

    public void setDesasignado(Boolean desasignado) {
        this.desasignado = desasignado;
    }

    public Date getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(Date fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

}
