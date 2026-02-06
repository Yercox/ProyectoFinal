package com.yugsi.factory;

import com.yugsi.model.*;

public class VehiculoFactoryImpl implements VehiculoFactory {

    @Override
    public Vehiculo crearVehiculo(String tipo, String matricula, String marca, String modelo,
                                  double precioPorDia, Object... parametros) {

        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo");
        }

        switch (tipo.toUpperCase()) {
            case "AUTOMOVIL":
                if (parametros.length < 2) {
                    throw new IllegalArgumentException("Faltan parámetros para Automóvil");
                }
                int puertas = (Integer) parametros[0];
                String combustible = (String) parametros[1];
                return new Automovil(matricula, marca, modelo, precioPorDia, puertas, combustible);

            case "MOTOCICLETA":
                if (parametros.length < 2) {
                    throw new IllegalArgumentException("Faltan parámetros para Motocicleta");
                }
                int cilindrada = (Integer) parametros[0];
                boolean maletero = (Boolean) parametros[1];
                return new Motocicleta(matricula, marca, modelo, precioPorDia, cilindrada, maletero);

            default:
                throw new IllegalArgumentException("Tipo de vehículo no soportado: " + tipo);
        }
    }

    public static Automovil crearAutomovil(String matricula, String marca, String modelo,
                                           double precioPorDia, int puertas, String combustible) {
        return new Automovil(matricula, marca, modelo, precioPorDia, puertas, combustible);
    }

    public static Motocicleta crearMotocicleta(String matricula, String marca, String modelo,
                                               double precioPorDia, int cilindrada, boolean maletero) {
        return new Motocicleta(matricula, marca, modelo, precioPorDia, cilindrada, maletero);
    }
}