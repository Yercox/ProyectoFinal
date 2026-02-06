package com.yugsi.model;

public class Automovil extends Vehiculo {
    private int numeroPuertas;
    private String tipoCombustible;

    public Automovil(String matricula, String marca, String modelo,
                     double precioPorDia, int numeroPuertas, String tipoCombustible) {
        super(matricula, marca, modelo, precioPorDia);
        this.numeroPuertas = numeroPuertas;
        this.tipoCombustible = tipoCombustible;
    }

    @Override
    public String getInfoDetallada() {
        return String.format("Automóvil - %s %s%nMatrícula: %s%nPuertas: %d%nCombustible: %s%nPrecio/día: $%.2f",
                marca, modelo, matricula, numeroPuertas, tipoCombustible, precioPorDia);
    }

    public int getNumeroPuertas() { return numeroPuertas; }
    public String getTipoCombustible() { return tipoCombustible; }
}