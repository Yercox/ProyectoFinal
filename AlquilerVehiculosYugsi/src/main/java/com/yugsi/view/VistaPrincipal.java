package com.yugsi.view;

import com.yugsi.controller.AlquilerController;
import com.yugsi.exception.AlquilerException;
import com.yugsi.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaPrincipal extends JFrame {
    private AlquilerController controller;

    private JTextField txtDni, txtNombre, txtLicencia, txtTelefono, txtDias;
    private JComboBox<Vehiculo> comboVehiculos;
    private JTextArea txtInfoVehiculo;
    private JButton btnRegistrarAlquiler, btnLimpiar, btnMostrarInfo;
    private JLabel lblEstado;
    private JButton btnAgregarVehiculo, btnDemoFactory;
    private JComboBox<String> comboTipoVehiculo;
    private JTextField txtMatricula, txtMarca, txtModelo, txtPrecioDia;
    private JTextField txtParam1, txtParam2;
    private JLabel lblParam1, lblParam2;

    public VistaPrincipal() {
        this.controller = new AlquilerController();
        controller.setVista(this);

        setTitle("DevRental - Sistema de Alquiler de Vehículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700); // Aumentado tamaño
        setLocationRelativeTo(null);

        initComponents();
        organizeComponents();
        cargarVehiculosDisponibles();
    }

    private void initComponents() {

        txtDni = new JTextField(15);
        txtNombre = new JTextField(15);
        txtLicencia = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtDias = new JTextField(5);

        comboVehiculos = new JComboBox<>();
        comboVehiculos.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Vehiculo) {
                    Vehiculo v = (Vehiculo) value;
                    String tipo = v instanceof Automovil ? "Auto" : "Moto";
                    value = String.format("%s %s (%s) - $%.2f/día",
                            v.getMarca(), v.getModelo(), tipo, v.getPrecioPorDia());
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        txtInfoVehiculo = new JTextArea(8, 40);
        txtInfoVehiculo.setEditable(false);
        txtInfoVehiculo.setFont(new Font("Monospaced", Font.PLAIN, 12));

        btnRegistrarAlquiler = new JButton("REGISTRAR ALQUILER");
        btnRegistrarAlquiler.setBackground(new Color(0, 120, 0));
        btnRegistrarAlquiler.setForeground(Color.BLACK);
        btnRegistrarAlquiler.setFont(new Font("Arial", Font.BOLD, 14));

        btnLimpiar = new JButton("Limpiar Formulario");
        btnMostrarInfo = new JButton("Ver Detalles");

        lblEstado = new JLabel(" Listo para registrar alquiler ");
        lblEstado.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(240, 240, 240));

        btnAgregarVehiculo = new JButton(" Agregar Vehículo");
        btnAgregarVehiculo.setBackground(new Color(70, 130, 180));
        btnAgregarVehiculo.setForeground(Color.WHITE);
        btnAgregarVehiculo.setFont(new Font("Arial", Font.BOLD, 12));

        btnDemoFactory = new JButton("Demo Factory Pattern");
        btnDemoFactory.setBackground(new Color(128, 0, 128));
        btnDemoFactory.setForeground(Color.WHITE);

        comboTipoVehiculo = new JComboBox<>(new String[]{"AUTOMOVIL", "MOTOCICLETA"});
        txtMatricula = new JTextField(10);
        txtMarca = new JTextField(10);
        txtModelo = new JTextField(10);
        txtPrecioDia = new JTextField(10);
        txtParam1 = new JTextField(10);
        txtParam2 = new JTextField(10);

        lblParam1 = new JLabel("Puertas:");
        lblParam2 = new JLabel("Combustible:");

        btnRegistrarAlquiler.addActionListener(e -> registrarAlquiler());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnMostrarInfo.addActionListener(e -> mostrarInformacionVehiculo());
        comboVehiculos.addActionListener(e -> mostrarInformacionVehiculo());
        txtDias.addActionListener(e -> mostrarInformacionVehiculo());

        comboTipoVehiculo.addActionListener(e -> cambiarCamposVehiculo());
        btnAgregarVehiculo.addActionListener(e -> agregarNuevoVehiculo());

        cambiarCamposVehiculo();
    }

    private void organizeComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelTitulo = new JPanel();
        JLabel lblTitulo = new JLabel(" REGISTRO DE ALQUILER-DEVRENTAL YUGSI");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Registro Alquiler", crearPanelAlquiler());

        tabbedPane.addTab("Agregar Vehículos", crearPanelAgregarVehiculo());

        add(tabbedPane, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);
    }

    private JPanel crearPanelAlquiler() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelCliente = new JPanel(new GridLayout(5, 2, 5, 10));
        panelCliente.setBorder(BorderFactory.createTitledBorder("👤 DATOS DEL CLIENTE"));
        panelCliente.add(new JLabel("DNI:"));
        panelCliente.add(txtDni);
        panelCliente.add(new JLabel("Nombre Completo:"));
        panelCliente.add(txtNombre);
        panelCliente.add(new JLabel("Licencia de Conducir:"));
        panelCliente.add(txtLicencia);
        panelCliente.add(new JLabel("Teléfono:"));
        panelCliente.add(txtTelefono);
        panelCliente.add(new JLabel("Días de Alquiler:"));
        panelCliente.add(txtDias);

        JPanel panelVehiculo = new JPanel(new BorderLayout(5, 5));
        panelVehiculo.setBorder(BorderFactory.createTitledBorder("SELECCIÓN DE VEHÍCULO"));

        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSeleccion.add(new JLabel("Vehículo Disponible:"));
        panelSeleccion.add(comboVehiculos);
        panelSeleccion.add(btnMostrarInfo);

        JScrollPane scrollInfo = new JScrollPane(txtInfoVehiculo);
        scrollInfo.setBorder(BorderFactory.createTitledBorder("Detalles del Vehículo"));

        panelVehiculo.add(panelSeleccion, BorderLayout.NORTH);
        panelVehiculo.add(scrollInfo, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCliente, panelVehiculo);
        splitPane.setResizeWeight(0.4);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnRegistrarAlquiler);
        panelBotones.add(btnLimpiar);

        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelAgregarVehiculo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelFormulario = new JPanel(new GridLayout(7, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                "AGREGAR NUEVO VEHÍCULO (Usando Factory Pattern)"));

        panelFormulario.add(new JLabel("Tipo de Vehículo:"));
        panelFormulario.add(comboTipoVehiculo);
        panelFormulario.add(new JLabel("Matrícula:"));
        panelFormulario.add(txtMatricula);
        panelFormulario.add(new JLabel("Marca:"));
        panelFormulario.add(txtMarca);
        panelFormulario.add(new JLabel("Modelo:"));
        panelFormulario.add(txtModelo);
        panelFormulario.add(new JLabel("Precio por Día ($):"));
        panelFormulario.add(txtPrecioDia);
        panelFormulario.add(lblParam1);
        panelFormulario.add(txtParam1);
        panelFormulario.add(lblParam2);
        panelFormulario.add(txtParam2);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.add(btnAgregarVehiculo);

        JTextArea txtInfoFactory = new JTextArea(5, 40);
        txtInfoFactory.setEditable(false);
        txtInfoFactory.setText(
                "INFORMACIÓN SOBRE FACTORY PATTERN:\n" +
                        "• Este panel demuestra el uso del patrón Factory\n" +
                        "• Los vehículos se crean dinámicamente según tipo\n" +
                        "• Se utiliza VehiculoFactoryImpl para la creación\n"
        );

        txtInfoFactory.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollInfo = new JScrollPane(txtInfoFactory);

        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(scrollInfo, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    private void cambiarCamposVehiculo() {
        String tipo = (String) comboTipoVehiculo.getSelectedItem();
        if ("AUTOMOVIL".equals(tipo)) {
            lblParam1.setText("Número de Puertas:");
            lblParam2.setText("Tipo de Combustible:");
            txtParam1.setText("4");
            txtParam2.setText("Gasolina");
        } else if ("MOTOCICLETA".equals(tipo)) {
            lblParam1.setText("Cilindrada (cc):");
            lblParam2.setText("Tiene Maletero (true/false):");
            txtParam1.setText("250");
            txtParam2.setText("true");
        }
    }

    private void agregarNuevoVehiculo() {
        try {
            String tipo = (String) comboTipoVehiculo.getSelectedItem();
            String matricula = txtMatricula.getText().trim();
            String marca = txtMarca.getText().trim();
            String modelo = txtModelo.getText().trim();

            if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio = Double.parseDouble(txtPrecioDia.getText().trim());

            Agencia agencia = controller.getAgencia();

            if ("AUTOMOVIL".equals(tipo)) {
                int puertas = Integer.parseInt(txtParam1.getText().trim());
                String combustible = txtParam2.getText().trim();

                agencia.agregarVehiculo("AUTOMOVIL", matricula, marca, modelo,
                        precio, puertas, combustible);

                JOptionPane.showMessageDialog(this,
                        String.format("Automóvil creado exitosamente:\n" +
                                        "• Matrícula: %s\n" +
                                        "• Marca/Modelo: %s %s\n" +
                                        "• Puertas: %d\n" +
                                        "• Combustible: %s\n" +
                                        "• Precio/día: $%.2f",
                                matricula, marca, modelo, puertas, combustible, precio),
                        "Vehículo Agregado", JOptionPane.INFORMATION_MESSAGE);

            } else if ("MOTOCICLETA".equals(tipo)) {
                int cilindrada = Integer.parseInt(txtParam1.getText().trim());
                boolean maletero = Boolean.parseBoolean(txtParam2.getText().trim());

                agencia.agregarVehiculo("MOTOCICLETA", matricula, marca, modelo,
                        precio, cilindrada, maletero);

                JOptionPane.showMessageDialog(this,
                        String.format("Motocicleta creada exitosamente:\n" +
                                        "• Matrícula: %s\n" +
                                        "• Marca/Modelo: %s %s\n" +
                                        "• Cilindrada: %d cc\n" +
                                        "• Maletero: %s\n" +
                                        "• Precio/día: $%.2f",
                                matricula, marca, modelo, cilindrada,
                                maletero ? "Sí" : "No", precio),
                        "Vehículo Agregado", JOptionPane.INFORMATION_MESSAGE);
            }

            cargarVehiculosDisponibles();
            limpiarCamposVehiculo();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error en formato numérico. Verifique:\n" +
                            "• Precio debe ser un número (ej: 50.0)\n" +
                            "• Puertas/Cilindrada debe ser entero\n" +
                            "• Maletero debe ser true/false",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



    private void limpiarCamposVehiculo() {
        txtMatricula.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtPrecioDia.setText("");
        cambiarCamposVehiculo();
    }


    private void cargarVehiculosDisponibles() {
        comboVehiculos.removeAllItems();
        java.util.List<Vehiculo> disponibles = controller.obtenerVehiculosDisponibles();

        if (disponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay vehículos disponibles en este momento",
                    "Inventario Vacío", JOptionPane.WARNING_MESSAGE);
        } else {
            disponibles.forEach(v -> comboVehiculos.addItem(v));
            mostrarInformacionVehiculo();
        }
    }

    private void mostrarInformacionVehiculo() {
        Vehiculo seleccionado = (Vehiculo) comboVehiculos.getSelectedItem();
        if (seleccionado != null) {
            txtInfoVehiculo.setText(seleccionado.getInfoDetallada());

            try {
                String diasTexto = txtDias.getText().trim();
                if (!diasTexto.isEmpty()) {
                    int dias = Integer.parseInt(diasTexto);
                    double costo = controller.calcularCostoEstimado(seleccionado, dias);
                    txtInfoVehiculo.append("\n\n══════════════════════════════════════\n");
                    txtInfoVehiculo.append(String.format("COSTO ESTIMADO PARA %d DÍAS: $%.2f", dias, costo));
                }
            } catch (NumberFormatException e) {

            }
        }
    }

    private void registrarAlquiler() {
        try {
            String dni = txtDni.getText().trim();
            String nombre = txtNombre.getText().trim();
            String licencia = txtLicencia.getText().trim();
            String telefono = txtTelefono.getText().trim();
            Vehiculo vehiculo = (Vehiculo) comboVehiculos.getSelectedItem();

            int dias;
            try {
                dias = Integer.parseInt(txtDias.getText().trim());
            } catch (NumberFormatException e) {
                throw new AlquilerException("Ingrese un número válido de días",
                        AlquilerException.TipoError.DATOS_INVALIDOS);
            }

            Alquiler alquiler = controller.registrarAlquiler(dni, nombre, licencia, telefono, vehiculo, dias);
            mostrarConfirmacionExitosa(alquiler, dias);

        } catch (AlquilerException e) {
            manejarErrorAlquiler(e);
        } catch (Exception e) {
            manejarErrorGeneral(e);
        }
    }

    private void mostrarConfirmacionExitosa(Alquiler alquiler, int dias) {
        String mensajeExito = String.format(
                " ALQUILER REGISTRADO EXITOSAMENTE\n\n" +
                        "ID de Alquiler: %s\n" +
                        "Cliente: %s\n" +
                        "Teléfono: %s\n" +
                        "Vehículo: %s %s\n" +
                        "Matrícula: %s\n" +
                        "Días: %d\n" +
                        "Costo Total: $%.2f\n\n" +
                        " Guardado en MongoDB Atlas\n" +
                        "El vehículo ya no está disponible para nuevos alquileres.",
                alquiler.getIdAlquiler(),
                alquiler.getCliente().getNombre(),
                alquiler.getCliente().getTelefono(),
                alquiler.getVehiculo().getMarca(),
                alquiler.getVehiculo().getModelo(),
                alquiler.getVehiculo().getMatricula(),
                dias,
                alquiler.getCostoTotal()
        );

        JOptionPane.showMessageDialog(this, mensajeExito,
                "Confirmación de Alquiler", JOptionPane.INFORMATION_MESSAGE);

        lblEstado.setText(" Alquiler registrado: " + alquiler.getIdAlquiler() + " - " + new java.util.Date());
        cargarVehiculosDisponibles();
    }

    private void manejarErrorAlquiler(AlquilerException e) {
        String tipoError = "";
        JComponent componenteFocus = txtDni;

        switch (e.getTipoError()) {
            case VALIDACION_CLIENTE:
                tipoError = "Error en datos del cliente";
                componenteFocus = txtDni;
                break;
            case VALIDACION_VEHICULO:
                tipoError = "Error en selección de vehículo";
                componenteFocus = comboVehiculos;
                break;
            case DATOS_INVALIDOS:
                tipoError = "Error en datos ingresados";
                componenteFocus = txtDias;
                txtDias.selectAll();
                break;
            default:
                tipoError = "Error en el sistema";
        }

        JOptionPane.showMessageDialog(this,
                tipoError + ":\n" + e.getMessage(),
                "Error de Validación", JOptionPane.ERROR_MESSAGE);

        lblEstado.setText(" Error: " + e.getMessage());
        componenteFocus.requestFocus();
    }

    private void manejarErrorGeneral(Exception e) {
        String mensajeError = " ERROR INESPERADO AL REGISTRAR ALQUILER\n\n";
        mensajeError += "Mensaje: " + e.getMessage() + "\n";
        mensajeError += "Tipo: " + e.getClass().getSimpleName();

        if (e.getCause() != null) {
            mensajeError += "\nCausa: " + e.getCause().getMessage();
        }

        JOptionPane.showMessageDialog(this, mensajeError,
                "Error Crítico", JOptionPane.ERROR_MESSAGE);

        lblEstado.setText(" Error inesperado: " + e.getClass().getSimpleName());
        e.printStackTrace();
    }

    private void limpiarFormulario() {
        txtDni.setText("");
        txtNombre.setText("");
        txtLicencia.setText("");
        txtTelefono.setText("");
        txtDias.setText("");
        lblEstado.setText(" Formulario limpiado - Listo para nuevo registro ");
        txtDni.requestFocus();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VistaPrincipal ventana = new VistaPrincipal();
            ventana.setVisible(true);
        });
    }
}