package com.yugsi.model;

public class Motocicleta extends Vehiculo {
    private int cilindrada;
    private boolean tieneMaletero;

    public Motocicleta(String matricula, String marca, String modelo,
                       double precioPorDia, int cilindrada, boolean tieneMaletero) {
        super(matricula, marca, modelo, precioPorDia);
        this.cilindrada = cilindrada;
        this.tieneMaletero = tieneMaletero;
    }

    @Override
    public String getInfoDetallada() {
        return String.format("Motocicleta - %s %s%nMatrícula: %s%nCilindrada: %d cc%nMaletero: %s%nPrecio/día: $%.2f",
                marca, modelo, matricula, cilindrada,
                tieneMaletero ? "Sí" : "No", precioPorDia);
    }

    public int getCilindrada() { return cilindrada; }
    public boolean isTieneMaletero() { return tieneMaletero; }
}