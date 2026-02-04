package com.yugsi.service;

import com.yugsi.db.ConexionMongo;
import com.yugsi.exception.AlquilerException;
import com.yugsi.model.*;

public class AlquilerService {
    private Agencia agencia;
    private ConexionMongo conexionMongo;

    public AlquilerService(Agencia agencia, ConexionMongo conexionMongo) {
        this.agencia = agencia;
        this.conexionMongo = conexionMongo;
    }

    public Alquiler procesarAlquiler(Cliente cliente, Vehiculo vehiculo, int dias)
            throws AlquilerException {

        try {
            Alquiler alquiler = agencia.registrarAlquiler(cliente, vehiculo, dias);

            boolean guardado = conexionMongo.guardarAlquiler(alquiler);

            if (!guardado) {
                throw new AlquilerException("Error al guardar en la base de datos",
                        AlquilerException.TipoError.ERROR_GUARDADO_BD);
            }

            return alquiler;

        } catch (Exception e) {
            if (e instanceof AlquilerException) {
                throw (AlquilerException) e;
            }
            throw new AlquilerException("Error al procesar alquiler: " + e.getMessage(),
                    AlquilerException.TipoError.ERROR_GUARDADO_BD, e);
        }
    }
}