package com.yugsi.model;

public abstract class Vehiculo implements Facturable {
    protected String matricula;
    protected String marca;
    protected String modelo;
    protected double precioPorDia;
    protected boolean disponible;

    public Vehiculo(String matricula, String marca, String modelo, double precioPorDia) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.precioPorDia = precioPorDia;
        this.disponible = true;
    }

    public abstract String getInfoDetallada();

    @Override
    public double calcularCostoTotal(int dias) {
        return precioPorDia * dias;
    }

    public String getMatricula() { return matricula; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getPrecioPorDia() { return precioPorDia; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + matricula + ")";
    }
}