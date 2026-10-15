package library;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionTest {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try {
            Connection connection = db.getConnection(
                    "jdbc:postgresql://localhost:5000/postgres",
                    "postgres",
                    System.getenv("Behzad123")
            );

            if (connection != null && !connection.isClosed()) {
                System.out.println("Connection test passed!");
            }

        } catch (RuntimeException | SQLException e) {
            System.out.println("Connection test failed: " + e.getMessage());
            System.out.println("Cause: " + e.getCause());
        } finally {
            db.closeConnection();
        }
    }
}