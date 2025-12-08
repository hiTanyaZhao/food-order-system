package com.foodorder.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection manager for PostgreSQL
 */
public class DatabaseConnection {
    
    // Database connection configuration
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    
    static {
        loadDatabaseConfig();
    }
    
    private static DatabaseConnection instance;
    private Connection connection;
    
    /**
     * Load database configuration from properties file
     */
    private static void loadDatabaseConfig() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                System.err.println("Unable to find database.properties file");
                // Fallback to default values
                URL = "jdbc:postgresql://localhost:5432/restaurant_db";
                USERNAME = "postgres";
                PASSWORD = "postgres";
                return;
            }
            props.load(input);
            URL = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/restaurant_db");
            USERNAME = props.getProperty("db.username", "postgres");
            PASSWORD = props.getProperty("db.password", "postgres");
            
            System.out.println("Database configuration loaded successfully");
        } catch (IOException e) {
            System.err.println("Error loading database configuration: " + e.getMessage());
            // Fallback to default values
            URL = "jdbc:postgresql://localhost:5432/restaurant_db";
            USERNAME = "postgres";
            PASSWORD = "postgres";
        }
    }
    
    private DatabaseConnection() {
        try {
            // Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    
    /**
     * Get database connection instance (singleton pattern)
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        try {
            // Check if connection is valid, reconnect if not
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get database connection: " + e.getMessage());
        }
        return connection;
    }
    
    /**
     * Close database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }
}
