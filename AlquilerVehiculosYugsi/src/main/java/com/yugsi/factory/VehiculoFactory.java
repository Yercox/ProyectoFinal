package com.yugsi.factory;

import com.yugsi.model.Vehiculo;

public interface VehiculoFactory {
    Vehiculo crearVehiculo(String tipo, String matricula, String marca, String modelo,
                           double precioPorDia, Object... parametros);
}