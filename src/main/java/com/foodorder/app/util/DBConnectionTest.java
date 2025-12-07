package com.foodorder.app.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        try {
            Connection conn = DBConnection.getConnection();
            System.out.println("Database connection successful");
            System.out.println("Database: " + conn.getCatalog());
            System.out.println("Connection status: " + (!conn.isClosed() ? "Connected" : "Closed"));
            conn.close();
            System.out.println("✓ Connection closed successfully");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
