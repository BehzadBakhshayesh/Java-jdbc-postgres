package library;

import library.config.DatabaseConnection;
import library.controller.Menu;
import library.repository.BookRepository;
import library.repository.LibraryRepository;
import library.repository.MemberRepository;
import library.services.BookServices;
import library.services.LibraryServices;
import library.services.MemberServices;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        DatabaseConnection db = new DatabaseConnection();

        try (Connection connection = db.getConnection("jdbc:postgresql://localhost:5000/library_db", "postgres", "Behzad123")) {

            BookServices bookServices = new BookServices(new BookRepository(connection));
            MemberServices memberServices = new MemberServices(new MemberRepository(connection));
            LibraryServices libraryServices = new LibraryServices(new LibraryRepository(connection));

            Menu.runMenu(bookServices, memberServices, libraryServices);

        } catch (RuntimeException | SQLException e) {

            System.out.println("Could not connect to the database: " + e.getMessage());
        }
    }
}