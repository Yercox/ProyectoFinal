package com.yugsi.exception;

public class AlquilerException extends Exception {
    private TipoError tipoError;

    public enum TipoError {
        VALIDACION_CLIENTE,
        VALIDACION_VEHICULO,
        ERROR_CONEXION_BD,
        ERROR_GUARDADO_BD,
        DATOS_INVALIDOS
    }

    public AlquilerException(String mensaje, TipoError tipoError) {
        super(mensaje);
        this.tipoError = tipoError;
    }

    public AlquilerException(String mensaje, TipoError tipoError, Throwable causa) {
        super(mensaje, causa);
        this.tipoError = tipoError;
    }

    public TipoError getTipoError() {
        return tipoError;
    }

    @Override
    public String getMessage() {
        return String.format("[%s] %s", tipoError.name(), super.getMessage());
    }
}