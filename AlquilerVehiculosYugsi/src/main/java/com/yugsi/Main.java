package com.yugsi;

import com.yugsi.view.VistaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            VistaPrincipal ventana = new VistaPrincipal();
            ventana.setVisible(true);
        });
    }
}