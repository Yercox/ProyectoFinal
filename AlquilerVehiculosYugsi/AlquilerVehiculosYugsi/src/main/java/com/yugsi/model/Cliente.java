package com.yugsi.model;

public class Cliente {
    private String dni;
    private String nombre;
    private String licenciaConducir;
    private String telefono;

    public Cliente(String dni, String nombre, String licenciaConducir, String telefono) {
        this.dni = dni;
        this.nombre = nombre;
        this.licenciaConducir = licenciaConducir;
        this.telefono = telefono;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getLicenciaConducir() { return licenciaConducir; }
    public String getTelefono() { return telefono; }

    @Override
    public String toString() {
        return nombre + " (" + dni + ")";
    }
}