package library.test;

import library.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseQueryTest {

    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();

        try (
                Connection connection = db.getConnection(
                        "jdbc:postgresql://localhost:5000/library_db",
                        "postgres",
                        "Behzad123"
                )
        ) {
            // Query 1: Count books
//            try (
//                    Statement stmt = connection.createStatement();
//                    ResultSet resultSet =
//                            stmt.executeQuery("SELECT COUNT(*) FROM books")
//            ) {
//                if (resultSet.next()) {
//                    long count = resultSet.getLong(1);
//                    System.out.println("Book count: " + count);
//                }
//            }

            // Query 2: Select books
            try (
                    Statement stmt = connection.createStatement();
                    ResultSet resultSet = stmt.executeQuery("SELECT id, title, author, year, is_available FROM books")
            ) {
                while (resultSet.next()) {
                    System.out.println(
                            resultSet.getLong("id")
                                    + " | " + resultSet.getString("title")
                                    + " | " + resultSet.getString("author")
                                    + " | " + resultSet.getInt("year")
                                    + " | " + resultSet.getBoolean("is_available")
                    );
                }
            }


        } catch (RuntimeException | SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
        }
    }
}