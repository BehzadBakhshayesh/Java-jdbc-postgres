package library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Connection connection;

    public Connection getConnection(String url, String username, String password) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected successfully!");
            return this.connection;
        } catch (SQLException e) {
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    this.connection.close();
                    System.out.println("Connection closed successfully!");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection", e);
            }
        }
    }
}