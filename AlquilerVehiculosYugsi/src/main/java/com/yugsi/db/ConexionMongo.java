package com.yugsi.db;

import com.yugsi.model.Alquiler;
import com.yugsi.model.Cliente;
import com.yugsi.model.Vehiculo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.time.ZoneId;
import java.util.Date;

public class ConexionMongo {
    private static ConexionMongo instance;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> alquileresCollection;

    private static final String CONNECTION_STRING =
            "mongodb+srv://Yercox:Jeansuper101@cluster0.pgpjnm4.mongodb.net/?appName=Cluster0";
    private static final String DATABASE_NAME = "devrental";
    private static final String COLLECTION_NAME = "alquileres";

    private ConexionMongo() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            alquileresCollection = database.getCollection(COLLECTION_NAME);

            System.out.println("Conexión Singleton a MongoDB Atlas establecida correctamente");

        } catch (Exception e) {
            System.err.println("Error al conectar con MongoDB Atlas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized ConexionMongo getInstance() {
        if (instance == null) {
            instance = new ConexionMongo();
        }
        return instance;
    }



    public boolean guardarAlquiler(Alquiler alquiler) {
        try {
            Document docAlquiler = new Document();

            docAlquiler.append("idAlquiler", alquiler.getIdAlquiler())
                    .append("fechaRegistro", new Date())
                    .append("fechaInicio",
                            Date.from(alquiler.getFechaInicio()
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("fechaFin",
                            Date.from(alquiler.getFechaFin()
                                    .atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("costoTotal", alquiler.getCostoTotal())
                    .append("estado", alquiler.getEstado());

            Cliente cliente = alquiler.getCliente();
            Document docCliente = new Document()
                    .append("dni", cliente.getDni())
                    .append("nombre", cliente.getNombre())
                    .append("licenciaConducir", cliente.getLicenciaConducir())
                    .append("telefono", cliente.getTelefono());
            docAlquiler.append("cliente", docCliente);

            Vehiculo vehiculo = alquiler.getVehiculo();
            Document docVehiculo = new Document()
                    .append("matricula", vehiculo.getMatricula())
                    .append("marca", vehiculo.getMarca())
                    .append("modelo", vehiculo.getModelo())
                    .append("precioPorDia", vehiculo.getPrecioPorDia())
                    .append("tipo", vehiculo.getClass().getSimpleName());

            if (vehiculo instanceof com.yugsi.model.Automovil) {
                com.yugsi.model.Automovil auto = (com.yugsi.model.Automovil) vehiculo;
                docVehiculo.append("numeroPuertas", auto.getNumeroPuertas())
                        .append("tipoCombustible", auto.getTipoCombustible());
            } else if (vehiculo instanceof com.yugsi.model.Motocicleta) {
                com.yugsi.model.Motocicleta moto = (com.yugsi.model.Motocicleta) vehiculo;
                docVehiculo.append("cilindrada", moto.getCilindrada())
                        .append("tieneMaletero", moto.isTieneMaletero());
            }

            docAlquiler.append("vehiculo", docVehiculo);

            alquileresCollection.insertOne(docAlquiler);

            System.out.println("Alquiler guardado en MongoDB Atlas: " + alquiler.getIdAlquiler());
            return true;

        } catch (Exception e) {
            System.err.println("Error al guardar alquiler en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}