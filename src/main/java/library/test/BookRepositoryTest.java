package library.test;

import library.config.DatabaseConnection;
import library.domain.Book;
import library.exceptions.LibraryException;
import library.repository.BookRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class BookRepositoryTest {


    public static void main(String[] args) {




        DatabaseConnection db = new DatabaseConnection();
        try (Connection connection = db.getConnection(
                "jdbc:postgresql://localhost:5000/library_db",
                "postgres",
                "Behzad123"
        )) {
            BookRepository repo = new BookRepository(connection);

            Book b1 = repo.insert(new Book("Clean Code1", "Robert Martin", 2007));
            Book b2 = repo.insert(new Book("Clean Code2", "Robert Martin", 2008));
            System.out.println("Inserted: " + b1);
            System.out.println("Inserted: " + b2);

            System.out.println("findById: " + repo.findById(b1.getId()));
            System.out.println("search: " + repo.searchByTitle("clean"));

            Book updated = repo.update(b1.getId(), new Book("Java", "Behzad", 2020));
            System.out.println("update: " + updated);

            repo.updateAvailability(b1.getId(), false);
            System.out.println("after availability: " + repo.findById(b1.getId()));

            System.out.println("findAll: " + repo.findAll());

            repo.delete(b1.getId());
            repo.delete(b2.getId());
            System.out.println("after delete: " + repo.findById(b1.getId()));

            try {
                repo.delete(b1.getId());
                System.out.println("ERROR: expected exception");
            } catch (LibraryException e) {
                System.out.println("Expected error: " + e.getMessage());
            }
        } catch (RuntimeException | SQLException e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}



