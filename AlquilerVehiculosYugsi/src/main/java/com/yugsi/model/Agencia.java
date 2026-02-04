package com.yugsi.model;

import com.yugsi.factory.VehiculoFactory;
import com.yugsi.factory.VehiculoFactoryImpl;
import java.util.ArrayList;
import java.util.List;

public class Agencia {
    private String nombre;
    private List<Vehiculo> inventario;
    private List<Alquiler> alquileres;
    private VehiculoFactory factory;

    public Agencia(String nombre) {
        this.nombre = nombre;
        this.inventario = new ArrayList<>();
        this.alquileres = new ArrayList<>();
        this.factory = new VehiculoFactoryImpl();
        inicializarDatosDemo();
    }

    private void inicializarDatosDemo() {
        inventario.add(factory.crearVehiculo("AUTOMOVIL", "ABC123", "Toyota", "Corolla",
                50.0, 4, "Gasolina"));

        inventario.add(factory.crearVehiculo("AUTOMOVIL", "DEF456", "Honda", "Civic",
                55.0, 4, "Híbrido"));

        inventario.add(factory.crearVehiculo("MOTOCICLETA", "GHI789", "Yamaha", "MT-07",
                35.0, 689, true));

        inventario.add(factory.crearVehiculo("MOTOCICLETA", "JKL012", "Kawasaki", "Ninja 400",
                40.0, 399, false));
    }

    public void agregarVehiculo(String tipo, String matricula, String marca,
                                String modelo, double precio, Object... params) {
        Vehiculo vehiculo = factory.crearVehiculo(tipo, matricula, marca, modelo,
                precio, params);
        inventario.add(vehiculo);
    }

    public List<Vehiculo> getVehiculosDisponibles() {
        return inventario.stream()
                .filter(v -> v.isDisponible())
                .toList();
    }

    public Alquiler registrarAlquiler(Cliente cliente, Vehiculo vehiculo, int dias) {
        Alquiler nuevoAlquiler = new Alquiler(cliente, vehiculo, dias);
        alquileres.add(nuevoAlquiler);
        return nuevoAlquiler;
    }

    public List<Alquiler> getAlquileresActivos() {
        return alquileres.stream()
                .filter(a -> "ACTIVO".equals(a.getEstado()))
                .toList();
    }

    public List<Vehiculo> getInventario() { return inventario; }
    public List<Alquiler> getAlquileres() { return alquileres; }
    public String getNombre() { return nombre; }
}