package library.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private Connection connection;

    public Connection getConnection(String url, String username, String password) {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
            log.info("Connected to database");
            return this.connection;
        } catch (SQLException e) {
            log.error("Database connection failed (SQLState={})", e.getSQLState(), e);
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    this.connection.close();
                    log.info("Database connection closed");
                }
            } catch (SQLException e) {
                log.error("Failed to close database connection", e);
                throw new RuntimeException("Failed to close database connection", e);
            }
        }
    }
}