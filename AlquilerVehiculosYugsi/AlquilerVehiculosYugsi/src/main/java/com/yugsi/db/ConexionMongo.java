package com.yugsi.db;

import com.yugsi.model.Alquiler;
import com.yugsi.model.Cliente;
import com.yugsi.model.Vehiculo;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // CREATE - Crear un nuevo alquiler
    public boolean crearAlquiler(Alquiler alquiler) {
        try {
            Document docAlquiler = convertirAlquilerADocumento(alquiler);
            alquileresCollection.insertOne(docAlquiler);

            System.out.println("Alquiler creado en MongoDB Atlas: " + alquiler.getIdAlquiler());
            return true;

        } catch (Exception e) {
            System.err.println("Error al crear alquiler en MongoDB: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // READ - Obtener alquiler por ID
    public Document obtenerAlquilerPorId(String idAlquiler) {
        try {
            Bson filtro = Filters.eq("idAlquiler", idAlquiler);
            return alquileresCollection.find(filtro).first();
        } catch (Exception e) {
            System.err.println("Error al obtener alquiler por ID: " + e.getMessage());
            return null;
        }
    }

    // READ - Obtener todos los alquileres
    public List<Document> obtenerTodosAlquileres() {
        List<Document> alquileres = new ArrayList<>();
        try {
            alquileresCollection.find().forEach(alquileres::add);
            return alquileres;
        } catch (Exception e) {
            System.err.println("Error al obtener todos los alquileres: " + e.getMessage());
            return alquileres;
        }
    }

    // READ - Obtener alquileres por DNI de cliente
    public List<Document> obtenerAlquileresPorCliente(String dni) {
        List<Document> alquileres = new ArrayList<>();
        try {
            Bson filtro = Filters.eq("cliente.dni", dni);
            alquileresCollection.find(filtro).forEach(alquileres::add);
            return alquileres;
        } catch (Exception e) {
            System.err.println("Error al obtener alquileres por cliente: " + e.getMessage());
            return alquileres;
        }
    }

    // READ - Obtener alquileres activos
    public List<Document> obtenerAlquileresActivos() {
        List<Document> alquileres = new ArrayList<>();
        try {
            Bson filtro = Filters.eq("estado", "ACTIVO");
            alquileresCollection.find(filtro).forEach(alquileres::add);
            return alquileres;
        } catch (Exception e) {
            System.err.println("Error al obtener alquileres activos: " + e.getMessage());
            return alquileres;
        }
    }

    // UPDATE - Actualizar estado del alquiler
    public boolean actualizarEstadoAlquiler(String idAlquiler, String nuevoEstado) {
        try {
            Bson filtro = Filters.eq("idAlquiler", idAlquiler);
            Bson actualizacion = Updates.set("estado", nuevoEstado);

            UpdateResult result = alquileresCollection.updateOne(filtro, actualizacion);

            if (result.getModifiedCount() > 0) {
                System.out.println("Alquiler actualizado: " + idAlquiler + " -> " + nuevoEstado);
                return true;
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error al actualizar estado del alquiler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // UPDATE - Actualizar información del vehículo en un alquiler
    public boolean actualizarVehiculoAlquiler(String idAlquiler, Vehiculo nuevoVehiculo) {
        try {
            Bson filtro = Filters.eq("idAlquiler", idAlquiler);

            Document docVehiculo = new Document()
                    .append("matricula", nuevoVehiculo.getMatricula())
                    .append("marca", nuevoVehiculo.getMarca())
                    .append("modelo", nuevoVehiculo.getModelo())
                    .append("precioPorDia", nuevoVehiculo.getPrecioPorDia())
                    .append("tipo", nuevoVehiculo.getClass().getSimpleName());

            if (nuevoVehiculo instanceof com.yugsi.model.Automovil) {
                com.yugsi.model.Automovil auto = (com.yugsi.model.Automovil) nuevoVehiculo;
                docVehiculo.append("numeroPuertas", auto.getNumeroPuertas())
                        .append("tipoCombustible", auto.getTipoCombustible());
            } else if (nuevoVehiculo instanceof com.yugsi.model.Motocicleta) {
                com.yugsi.model.Motocicleta moto = (com.yugsi.model.Motocicleta) nuevoVehiculo;
                docVehiculo.append("cilindrada", moto.getCilindrada())
                        .append("tieneMaletero", moto.isTieneMaletero());
            }

            Bson actualizacion = Updates.set("vehiculo", docVehiculo);
            UpdateResult result = alquileresCollection.updateOne(filtro, actualizacion);

            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error al actualizar vehículo del alquiler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminar alquiler por ID
    public boolean eliminarAlquiler(String idAlquiler) {
        try {
            Bson filtro = Filters.eq("idAlquiler", idAlquiler);
            DeleteResult result = alquileresCollection.deleteOne(filtro);

            if (result.getDeletedCount() > 0) {
                System.out.println("Alquiler eliminado: " + idAlquiler);
                return true;
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error al eliminar alquiler: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // DELETE - Eliminar alquileres por DNI de cliente
    public boolean eliminarAlquileresPorCliente(String dni) {
        try {
            Bson filtro = Filters.eq("cliente.dni", dni);
            DeleteResult result = alquileresCollection.deleteMany(filtro);

            System.out.println("Alquileres eliminados para cliente " + dni + ": " + result.getDeletedCount());
            return result.getDeletedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error al eliminar alquileres por cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método auxiliar para convertir Alquiler a Document
    private Document convertirAlquilerADocumento(Alquiler alquiler) {
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

        return docAlquiler;
    }

    // Método para contar documentos
    public long contarAlquileres() {
        try {
            return alquileresCollection.countDocuments();
        } catch (Exception e) {
            System.err.println("Error al contar alquileres: " + e.getMessage());
            return 0;
        }
    }

    // Método para buscar por rango de fechas
    public List<Document> buscarPorRangoFechas(Date desde, Date hasta) {
        List<Document> resultados = new ArrayList<>();
        try {
            Bson filtro = Filters.and(
                    Filters.gte("fechaInicio", desde),
                    Filters.lte("fechaInicio", hasta)
            );
            alquileresCollection.find(filtro).forEach(resultados::add);
            return resultados;
        } catch (Exception e) {
            System.err.println("Error al buscar por rango de fechas: " + e.getMessage());
            return resultados;
        }
    }

    public boolean guardarAlquiler(Alquiler alquiler) {
        return crearAlquiler(alquiler);
    }
}