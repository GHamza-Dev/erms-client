package com.hahn.erms.client.ui.screens;

import com.hahn.erms.client.service.AuthService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class LoginScreen extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final AuthService authService;

    public LoginScreen() {
        this.authService = new AuthService();

        // Frame setup
        setTitle("ERMS Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        // Create panel with MigLayout
        JPanel panel = new JPanel(new MigLayout("fillx, insets 20", "[][grow]", "[]20[]20[]"));

        // Components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        // Add components
        panel.add(new JLabel("Username:"), "right");
        panel.add(usernameField, "growx, wrap");
        panel.add(new JLabel("Password:"), "right");
        panel.add(passwordField, "growx, wrap");
        panel.add(loginButton, "span 2, center");

        // Login action
        loginButton.addActionListener(e -> login());

        add(panel);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            boolean success = authService.login(username, password);
            if (success) {
                MainScreen mainScreen = new MainScreen();
                mainScreen.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Login failed",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}