package com.hahn.erms.client.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    private static String jwtToken;

    // Static initializer to load configuration
    static {
        try {
            // Try to load from external file first
            File externalConfig = new File("config.properties");
            if (externalConfig.exists()) {
                try (FileInputStream input = new FileInputStream(externalConfig)) {
                    properties.load(input);
                }
            } else {
                // If no external file, load from classpath
                try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
                    if (input != null) {
                        properties.load(input);
                    } else {
                        throw new RuntimeException("No configuration file found");
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error loading configuration", ex);
        }
    }

    // Getter for any configuration value
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Configuration key not found: " + key);
        }
        return value;
    }

    // Specific getters for commonly used values
    public static String getApiBaseUrl() {
        return getProperty("api.base.url");
    }

    public static String getAuthPath() {
        return getProperty("auth.path");
    }

    public static String getEmployeesPath() {
        return getProperty("employees.path");
    }

    public static String getJwtSecret() {
        return getProperty("jwt.secret");
    }

    public static void setJwtToken(String token) {
        jwtToken = token;
    }

    public static String getJwtToken() {
        return jwtToken;
    }
}