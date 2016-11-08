package com.example.accplinux.probandobackendless;

/**
 * Created by accplinux on 5/11/16.
 */

public class Empleado {

    private String nombre;
    private String apellidos;
    private String email;
    private String movil;
    private String direccion;

    public Empleado()
    {}

    public Empleado( String nombre, String apellidos,String email,String movil,String direccion )
    {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.movil = movil;
        this.direccion = direccion;

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


}
