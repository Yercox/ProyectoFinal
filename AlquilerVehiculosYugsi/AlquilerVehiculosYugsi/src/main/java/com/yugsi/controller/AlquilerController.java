package com.yugsi.controller;

import com.yugsi.db.ConexionMongo;
import com.yugsi.exception.AlquilerException;
import com.yugsi.model.*;
import com.yugsi.service.AlquilerService;
import com.yugsi.view.VistaPrincipal;

import java.util.List;

public class AlquilerController {
    private AlquilerService alquilerService;
    private Agencia agencia;
    private VistaPrincipal vista;

    public AlquilerController() {
        this.agencia = new Agencia("DevRentalYugsi");
        ConexionMongo conexionMongo = ConexionMongo.getInstance();
        this.alquilerService = new AlquilerService(agencia, conexionMongo);
    }

    public void setVista(VistaPrincipal vista) {
        this.vista = vista;
    }

    public Agencia getAgencia() {
        return agencia;
    }

    public List<Vehiculo> obtenerVehiculosDisponibles() {
        return agencia.getVehiculosDisponibles();
    }

    public Alquiler registrarAlquiler(String dni, String nombre, String licencia,
                                      String telefono, Vehiculo vehiculo, int dias)
            throws AlquilerException {

        validarDatosCliente(dni, nombre);
        validarDias(dias);
        validarVehiculo(vehiculo);

        Cliente cliente = new Cliente(dni, nombre, licencia, telefono);

        return alquilerService.procesarAlquiler(cliente, vehiculo, dias);
    }

    public void validarDatosCliente(String dni, String nombre) throws AlquilerException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new AlquilerException("El DNI es obligatorio",
                    AlquilerException.TipoError.VALIDACION_CLIENTE);
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new AlquilerException("El nombre es obligatorio",
                    AlquilerException.TipoError.VALIDACION_CLIENTE);
        }
    }

    public void validarDias(int dias) throws AlquilerException {
        if (dias <= 0 || dias > 365) {
            throw new AlquilerException("Los días deben estar entre 1 y 365",
                    AlquilerException.TipoError.DATOS_INVALIDOS);
        }
    }

    public void validarVehiculo(Vehiculo vehiculo) throws AlquilerException {
        if (vehiculo == null) {
            throw new AlquilerException("Debe seleccionar un vehículo",
                    AlquilerException.TipoError.VALIDACION_VEHICULO);
        }
        if (!vehiculo.isDisponible()) {
            throw new AlquilerException("El vehículo no está disponible",
                    AlquilerException.TipoError.VALIDACION_VEHICULO);
        }
    }

    public double calcularCostoEstimado(Vehiculo vehiculo, int dias) {
        if (vehiculo == null) return 0;
        return vehiculo.calcularCostoTotal(dias);
    }
}