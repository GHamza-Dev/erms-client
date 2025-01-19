package com.hahn.erms;

import com.hahn.erms.client.ui.screens.LoginScreen;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.printf("Look and feel not supported: %s%n", e.getMessage());
            e.printStackTrace();
        }

        // Start application on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                LoginScreen loginScreen = new LoginScreen();
                loginScreen.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error starting application",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}