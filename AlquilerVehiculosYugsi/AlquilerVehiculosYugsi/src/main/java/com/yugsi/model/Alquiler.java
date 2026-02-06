package com.yugsi.model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Alquiler {
    private String idAlquiler;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double costoTotal;
    private String estado; // "ACTIVO", "FINALIZADO"

    public Alquiler(Cliente cliente, Vehiculo vehiculo, int dias) {
        this.idAlquiler = "ALQ" + System.currentTimeMillis();
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.fechaInicio = LocalDate.now();
        this.fechaFin = fechaInicio.plusDays(dias);
        this.costoTotal = vehiculo.calcularCostoTotal(dias);
        this.estado = "ACTIVO";
        this.vehiculo.setDisponible(false);
    }

    public String getIdAlquiler() { return idAlquiler; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public double getCostoTotal() { return costoTotal; }
    public String getEstado() { return estado; }

    public void finalizarAlquiler() {
        this.estado = "FINALIZADO";
        this.vehiculo.setDisponible(true);
    }

    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaFin);
    }
}