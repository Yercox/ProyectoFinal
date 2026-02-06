package com.yugsi.exception;
import java.util.regex.Pattern;

public class Validaciones {
    public static boolean validarDNI(String dni) {
        return dni != null && Pattern.matches("^\\d{8}$", dni);
    }

    public static boolean validarNombre(String nombre) {
        return nombre != null && Pattern.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{3,50}$", nombre);
    }

    public static boolean validarTelefono(String telefono) {
        return telefono != null && Pattern.matches("^9\\d{8}$", telefono);
    }

    public static boolean validarLicencia(String licencia) {
        return licencia != null && licencia.trim().length() >= 5;
    }

    public static boolean validarDias(int dias) {
        return dias >= 1 && dias <= 365;
    }

    public static boolean validarPrecio(double precio) {
        return precio > 0;
    }
}