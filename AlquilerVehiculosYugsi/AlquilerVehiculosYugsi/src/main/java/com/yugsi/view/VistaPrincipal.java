package com.yugsi.view;

import com.yugsi.controller.AlquilerController;
import com.yugsi.exception.AlquilerException;
import com.yugsi.model.*;
import com.yugsi.service.AlquilerService;
import com.yugsi.exception.Validaciones;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaPrincipal extends JFrame {
    private AlquilerController controller;
    private AlquilerService alquilerService;

    private ComponentesRegistroAlquiler compRegistro;
    private ComponentesAgregarVehiculo compVehiculo;
    private ComponentesCRUD compCRUD;

    private Color colorAzul = new Color(41, 128, 185);
    private Color colorAzulClaro = new Color(52, 152, 219);
    private Color colorVerde = new Color(39, 174, 96);
    private Color colorRojo = new Color(192, 57, 43);

    public VistaPrincipal() {
        this.controller = new AlquilerController();
        controller.setVista(this);
        this.alquilerService = new AlquilerService(controller.getAgencia(),
                com.yugsi.db.ConexionMongo.getInstance());

        setTitle("DevRental - Sistema de Alquiler");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        inicializarComponentes();
        organizarInterfaz();
        cargarVehiculos();
    }

    private void inicializarComponentes() {
        compRegistro = new ComponentesRegistroAlquiler();
        compVehiculo = new ComponentesAgregarVehiculo();
        compCRUD = new ComponentesCRUD();
    }

    private void organizarInterfaz() {
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(colorAzul);
        JLabel lblTitulo = new JLabel("SISTEMA DE ALQUILER DE VEHICULOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registro", compRegistro.crearPanel());
        tabbedPane.addTab("Agregar Vehiculo", compVehiculo.crearPanel());
        tabbedPane.addTab("CRUD", compCRUD.crearPanel());

        add(tabbedPane, BorderLayout.CENTER);
        add(compRegistro.lblEstado, BorderLayout.SOUTH);
    }

    private void cargarVehiculos() {
        compRegistro.comboVehiculos.removeAllItems();
        controller.obtenerVehiculosDisponibles().forEach(v -> compRegistro.comboVehiculos.addItem(v));
    }

    private class ComponentesRegistroAlquiler {
        JTextField txtDni = new JTextField(15);
        JTextField txtNombre = new JTextField(15);
        JTextField txtLicencia = new JTextField(15);
        JTextField txtTelefono = new JTextField(15);
        JTextField txtDias = new JTextField(5);
        JComboBox<Vehiculo> comboVehiculos = new JComboBox<>();
        JTextArea txtInfo = new JTextArea(8, 40);
        JButton btnRegistrar = crearBoton("REGISTRAR ALQUILER", colorVerde);
        JButton btnLimpiar = crearBoton("Limpiar", colorAzulClaro);
        JLabel lblEstado = new JLabel(" Listo ");

        JPanel crearPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel panelCliente = new JPanel(new GridLayout(5, 2, 5, 10));
            panelCliente.setBorder(BorderFactory.createTitledBorder("DATOS CLIENTE"));
            panelCliente.add(new JLabel("DNI:"));
            panelCliente.add(txtDni);
            panelCliente.add(new JLabel("Nombre:"));
            panelCliente.add(txtNombre);
            panelCliente.add(new JLabel("Licencia:"));
            panelCliente.add(txtLicencia);
            panelCliente.add(new JLabel("Telefono:"));
            panelCliente.add(txtTelefono);
            panelCliente.add(new JLabel("Dias:"));
            panelCliente.add(txtDias);

            JPanel panelVehiculo = new JPanel(new BorderLayout());
            panelVehiculo.setBorder(BorderFactory.createTitledBorder("VEHICULO"));
            JPanel panelSeleccion = new JPanel(new FlowLayout());
            panelSeleccion.add(new JLabel("Vehiculo:"));
            panelSeleccion.add(comboVehiculos);
            txtInfo.setEditable(false);
            JScrollPane scroll = new JScrollPane(txtInfo);
            panelVehiculo.add(panelSeleccion, BorderLayout.NORTH);
            panelVehiculo.add(scroll, BorderLayout.CENTER);

            JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCliente, panelVehiculo);
            split.setResizeWeight(0.4);

            JPanel panelBotones = new JPanel();
            panelBotones.add(btnRegistrar);
            panelBotones.add(btnLimpiar);

            panel.add(split, BorderLayout.CENTER);
            panel.add(panelBotones, BorderLayout.SOUTH);

            btnRegistrar.addActionListener(e -> registrarAlquiler());
            btnLimpiar.addActionListener(e -> limpiarFormulario());
            comboVehiculos.addActionListener(e -> mostrarInfoVehiculo());

            return panel;
        }
    }

    private class ComponentesAgregarVehiculo {
        JComboBox<String> comboTipo = new JComboBox<>(new String[]{"AUTOMOVIL", "MOTOCICLETA"});
        JTextField txtMatricula = new JTextField(10);
        JTextField txtMarca = new JTextField(10);
        JTextField txtModelo = new JTextField(10);
        JTextField txtPrecio = new JTextField(10);
        JTextField txtParam1 = new JTextField(10);
        JTextField txtParam2 = new JTextField(10);
        JButton btnAgregar = crearBoton("Agregar Vehiculo", colorAzul);

        JPanel crearPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel panelForm = new JPanel(new GridLayout(7, 2, 5, 10));
            panelForm.setBorder(BorderFactory.createTitledBorder("NUEVO VEHICULO"));
            panelForm.add(new JLabel("Tipo:"));
            panelForm.add(comboTipo);
            panelForm.add(new JLabel("Matricula:"));
            panelForm.add(txtMatricula);
            panelForm.add(new JLabel("Marca:"));
            panelForm.add(txtMarca);
            panelForm.add(new JLabel("Modelo:"));
            panelForm.add(txtModelo);
            panelForm.add(new JLabel("Precio/dia:"));
            panelForm.add(txtPrecio);
            panelForm.add(new JLabel("Parametro 1(N puertas/cilindraje):"));
            panelForm.add(txtParam1);
            panelForm.add(new JLabel("Parametro 2 (Tipo de combustible/Tiene maletero)(:"));
            panelForm.add(txtParam2);

            comboTipo.addActionListener(e -> cambiarCampos());
            btnAgregar.addActionListener(e -> agregarVehiculo());

            JPanel panelBotones = new JPanel();
            panelBotones.add(btnAgregar);

            panel.add(panelForm, BorderLayout.CENTER);
            panel.add(panelBotones, BorderLayout.SOUTH);

            cambiarCampos();
            return panel;
        }

        void cambiarCampos() {
            if ("AUTOMOVIL".equals(comboTipo.getSelectedItem())) {
                txtParam1.setText("4");
                txtParam2.setText("Gasolina");
            } else {
                txtParam1.setText("250");
                txtParam2.setText("true");
            }
        }
    }

    private class ComponentesCRUD {
        DefaultTableModel modelo = new DefaultTableModel(new String[]{"ID", "Cliente", "DNI", "Vehiculo", "Estado", "Costo"}, 0);
        JTable tabla = new JTable(modelo);
        JTextField txtBuscarDNI = new JTextField(12);
        JButton btnBuscar = crearBoton("Buscar por DNI", colorAzulClaro);
        JButton btnCargar = crearBoton("Cargar Todos", colorAzul);
        JButton btnEliminar = crearBoton("Eliminar", colorRojo);
        JButton btnActualizar = crearBoton("Actualizar Cliente", colorVerde);
        JTextArea txtDetalles = new JTextArea(8, 40);
        private void cargarTodos() {
            modelo.setRowCount(0);
            List<Document> alquileres = alquilerService.obtenerTodosAlquileres();

            for (Document doc : alquileres) {
                Document cliente = (Document) doc.get("cliente");
                Document vehiculo = (Document) doc.get("vehiculo");
                modelo.addRow(new Object[]{
                        doc.getString("idAlquiler"),
                        cliente.getString("nombre"),
                        cliente.getString("dni"),
                        vehiculo.getString("marca") + " " + vehiculo.getString("modelo"),
                        doc.getString("estado"),
                        String.format("$%.2f", doc.getDouble("costoTotal"))
                });
            }

            txtDetalles.setText("Total: " + alquileres.size());
        }
        JPanel crearPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel panelBusqueda = new JPanel(new FlowLayout());
            panelBusqueda.setBorder(BorderFactory.createTitledBorder("BUSQUEDA"));
            panelBusqueda.add(new JLabel("DNI:"));
            panelBusqueda.add(txtBuscarDNI);
            panelBusqueda.add(btnBuscar);
            panelBusqueda.add(btnCargar);

            JScrollPane scrollTabla = new JScrollPane(tabla);
            scrollTabla.setBorder(BorderFactory.createTitledBorder("ALQUILERES"));

            JPanel panelAcciones = new JPanel();
            panelAcciones.add(btnActualizar);
            panelAcciones.add(btnEliminar);

            txtDetalles.setEditable(false);
            JScrollPane scrollDetalles = new JScrollPane(txtDetalles);

            JPanel panelInferior = new JPanel(new BorderLayout());
            panelInferior.add(scrollDetalles, BorderLayout.CENTER);
            panelInferior.add(panelAcciones, BorderLayout.SOUTH);

            panel.add(panelBusqueda, BorderLayout.NORTH);
            panel.add(scrollTabla, BorderLayout.CENTER);
            panel.add(panelInferior, BorderLayout.SOUTH);

            btnBuscar.addActionListener(e -> buscarPorDNI());
            btnCargar.addActionListener(e -> cargarTodos());
            btnEliminar.addActionListener(e -> eliminarSeleccionado());
            btnActualizar.addActionListener(e -> abrirActualizarCliente());
            tabla.getSelectionModel().addListSelectionListener(e -> mostrarDetalles());

            SwingUtilities.invokeLater(() -> VistaPrincipal.this.cargarTodos());

            return panel;
        }
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        return btn;
    }

    private void mostrarInfoVehiculo() {
        Vehiculo v = (Vehiculo) compRegistro.comboVehiculos.getSelectedItem();
        if (v != null) {
            compRegistro.txtInfo.setText(v.getInfoDetallada());
        }
    }

    private void limpiarFormulario() {
        compRegistro.txtDni.setText("");
        compRegistro.txtNombre.setText("");
        compRegistro.txtLicencia.setText("");
        compRegistro.txtTelefono.setText("");
        compRegistro.txtDias.setText("");
        compRegistro.lblEstado.setText("Formulario limpiado");
    }

    private void registrarAlquiler() {
        try {
            String dni = compRegistro.txtDni.getText().trim();
            String nombre = compRegistro.txtNombre.getText().trim();
            String licencia = compRegistro.txtLicencia.getText().trim();
            String telefono = compRegistro.txtTelefono.getText().trim();
            Vehiculo vehiculo = (Vehiculo) compRegistro.comboVehiculos.getSelectedItem();

            if (!Validaciones.validarDNI(dni)) {
                throw new AlquilerException("DNI invalido: 8 digitos", AlquilerException.TipoError.DATOS_INVALIDOS);
            }
            if (!Validaciones.validarNombre(nombre)) {
                throw new AlquilerException("Nombre invalido", AlquilerException.TipoError.DATOS_INVALIDOS);
            }
            if (!Validaciones.validarLicencia(licencia)) {
                throw new AlquilerException("Licencia invalida", AlquilerException.TipoError.DATOS_INVALIDOS);
            }
            if (!Validaciones.validarTelefono(telefono)) {
                throw new AlquilerException("Telefono invalido: 9 digitos empezando con 9", AlquilerException.TipoError.DATOS_INVALIDOS);
            }

            int dias;
            try {
                dias = Integer.parseInt(compRegistro.txtDias.getText().trim());
                if (!Validaciones.validarDias(dias)) {
                    throw new AlquilerException("Dias invalidos: 1-365", AlquilerException.TipoError.DATOS_INVALIDOS);
                }
            } catch (NumberFormatException e) {
                throw new AlquilerException("Dias debe ser numero", AlquilerException.TipoError.DATOS_INVALIDOS);
            }

            if (vehiculo == null) {
                throw new AlquilerException("Seleccione vehiculo", AlquilerException.TipoError.VALIDACION_VEHICULO);
            }

            Alquiler alquiler = controller.registrarAlquiler(dni, nombre, licencia, telefono, vehiculo, dias);

            JOptionPane.showMessageDialog(this,
                    "Alquiler registrado:\nID: " + alquiler.getIdAlquiler() +
                            "\nCliente: " + nombre +
                            "\nCosto: $" + alquiler.getCostoTotal(),
                    "Exito", JOptionPane.INFORMATION_MESSAGE);

            compRegistro.lblEstado.setText("Registrado: " + alquiler.getIdAlquiler());
            cargarVehiculos();
            compCRUD.cargarTodos();

        } catch (AlquilerException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            compRegistro.lblEstado.setText("Error: " + e.getMessage());
        }
    }

    private void agregarVehiculo() {
        try {
            String tipo = (String) compVehiculo.comboTipo.getSelectedItem();
            String matricula = compVehiculo.txtMatricula.getText().trim();
            String marca = compVehiculo.txtMarca.getText().trim();
            String modelo = compVehiculo.txtModelo.getText().trim();
            String precioStr = compVehiculo.txtPrecio.getText().trim();

            if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty() || precioStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(precioStr);
            if (!Validaciones.validarPrecio(precio)) {
                JOptionPane.showMessageDialog(this, "Precio invalido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Agencia agencia = controller.getAgencia();

            if ("AUTOMOVIL".equals(tipo)) {
                int puertas = Integer.parseInt(compVehiculo.txtParam1.getText().trim());
                String combustible = compVehiculo.txtParam2.getText().trim();
                agencia.agregarVehiculo("AUTOMOVIL", matricula, marca, modelo, precio, puertas, combustible);
            } else {
                int cilindrada = Integer.parseInt(compVehiculo.txtParam1.getText().trim());
                boolean maletero = Boolean.parseBoolean(compVehiculo.txtParam2.getText().trim());
                agencia.agregarVehiculo("MOTOCICLETA", matricula, marca, modelo, precio, cilindrada, maletero);
            }

            JOptionPane.showMessageDialog(this, "Vehiculo agregado", "Exito", JOptionPane.INFORMATION_MESSAGE);
            cargarVehiculos();
            compVehiculo.txtMatricula.setText("");
            compVehiculo.txtMarca.setText("");
            compVehiculo.txtModelo.setText("");
            compVehiculo.txtPrecio.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato numerico", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPorDNI() {
        String dni = compCRUD.txtBuscarDNI.getText().trim();
        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese DNI", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!Validaciones.validarDNI(dni)) {
            JOptionPane.showMessageDialog(this, "DNI invalido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        compCRUD.modelo.setRowCount(0);
        List<Document> alquileres = alquilerService.obtenerAlquileresPorCliente(dni);

        for (Document doc : alquileres) {
            Document cliente = (Document) doc.get("cliente");
            Document vehiculo = (Document) doc.get("vehiculo");
            compCRUD.modelo.addRow(new Object[]{
                    doc.getString("idAlquiler"),
                    cliente.getString("nombre"),
                    cliente.getString("dni"),
                    vehiculo.getString("marca") + " " + vehiculo.getString("modelo"),
                    doc.getString("estado"),
                    String.format("$%.2f", doc.getDouble("costoTotal"))
            });
        }

        compCRUD.txtDetalles.setText("Encontrados: " + alquileres.size());
    }

    private void cargarTodos() {
        compCRUD.modelo.setRowCount(0);
        List<Document> alquileres = alquilerService.obtenerTodosAlquileres();

        for (Document doc : alquileres) {
            Document cliente = (Document) doc.get("cliente");
            Document vehiculo = (Document) doc.get("vehiculo");
            compCRUD.modelo.addRow(new Object[]{
                    doc.getString("idAlquiler"),
                    cliente.getString("nombre"),
                    cliente.getString("dni"),
                    vehiculo.getString("marca") + " " + vehiculo.getString("modelo"),
                    doc.getString("estado"),
                    String.format("$%.2f", doc.getDouble("costoTotal"))
            });
        }

        compCRUD.txtDetalles.setText("Total: " + alquileres.size());
    }

    private void mostrarDetalles() {
        int fila = compCRUD.tabla.getSelectedRow();
        if (fila >= 0) {
            String id = (String) compCRUD.modelo.getValueAt(fila, 0);
            Document doc = alquilerService.obtenerAlquilerPorId(id);
            if (doc != null) {
                Document cliente = (Document) doc.get("cliente");
                Document vehiculo = (Document) doc.get("vehiculo");
                compCRUD.txtDetalles.setText(
                        "ID: " + id + "\n" +
                                "Cliente: " + cliente.getString("nombre") + "\n" +
                                "DNI: " + cliente.getString("dni") + "\n" +
                                "Telefono: " + cliente.getString("telefono") + "\n" +
                                "Vehiculo: " + vehiculo.getString("marca") + " " + vehiculo.getString("modelo") + "\n" +
                                "Costo: $" + doc.getDouble("costoTotal")
                );
            }
        }
    }

    private void eliminarSeleccionado() {
        int fila = compCRUD.tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un alquiler", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) compCRUD.modelo.getValueAt(fila, 0);
        String nombre = (String) compCRUD.modelo.getValueAt(fila, 1);

        int opcion = JOptionPane.showConfirmDialog(this,
                "Eliminar alquiler de " + nombre + "?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            boolean eliminado = alquilerService.eliminarAlquiler(id);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Alquiler eliminado", "Exito", JOptionPane.INFORMATION_MESSAGE);
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirActualizarCliente() {
        int fila = compCRUD.tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un alquiler", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) compCRUD.modelo.getValueAt(fila, 0);
        Document doc = alquilerService.obtenerAlquilerPorId(id);
        if (doc == null) return;

        Document cliente = (Document) doc.get("cliente");

        JDialog dialog = new JDialog(this, "Actualizar Cliente", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ID Alquiler:"));
        JTextField txtId = new JTextField(id);
        txtId.setEditable(false);
        panel.add(txtId);

        panel.add(new JLabel("DNI:"));
        JTextField txtDni = new JTextField(cliente.getString("dni"));
        txtDni.setEditable(false);
        panel.add(txtDni);

        panel.add(new JLabel("Nuevo Nombre:"));
        JTextField txtNombre = new JTextField(cliente.getString("nombre"));
        panel.add(txtNombre);

        panel.add(new JLabel("Nuevo Telefono:"));
        JTextField txtTelefono = new JTextField(cliente.getString("telefono"));
        panel.add(txtTelefono);

        JPanel panelBotones = new JPanel();
        JButton btnCancelar = crearBoton("Cancelar", colorAzulClaro);
        JButton btnGuardar = crearBoton("Guardar", colorVerde);

        btnCancelar.addActionListener(e -> dialog.dispose());
        btnGuardar.addActionListener(e -> {
            String nuevoNombre = txtNombre.getText().trim();
            String nuevoTelefono = txtTelefono.getText().trim();

            if (!Validaciones.validarNombre(nuevoNombre)) {
                JOptionPane.showMessageDialog(dialog, "Nombre invalido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Validaciones.validarTelefono(nuevoTelefono)) {
                JOptionPane.showMessageDialog(dialog, "Telefono invalido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this,
                    "Actualizacion requeriria implementacion en MongoDB\n" +
                            "Datos validados: " + nuevoNombre + " - " + nuevoTelefono,
                    "Informacion", JOptionPane.INFORMATION_MESSAGE);

            dialog.dispose();
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(panelBotones, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}