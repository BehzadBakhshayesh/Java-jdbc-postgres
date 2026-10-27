package library.test;

import library.config.DatabaseConnection;
import library.domain.Book;
import library.repository.BookRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BookRepositoryTest {


    public static void main(String[] args) {




        DatabaseConnection db = new DatabaseConnection();
        try (Connection connection = db.getConnection(
                "jdbc:postgresql://localhost:5000/library_db",
                "postgres",
                "Behzad123"
        )) {
            BookRepository bookRepository = new BookRepository(connection);

            // 1. insert
            Book saved = bookRepository.insert(new Book("Clean Code1", "Robert Martin", 2007));
            Book saved2 = bookRepository.insert(new Book("Clean Code2", "Robert Martin", 2008));
            Book saved3 = bookRepository.insert(new Book("Clean Code3", "Robert Martin", 2009));
            System.out.println("Inserted: " + saved);
            // 2. findById
            Optional<Book> findById = bookRepository.findById(1);
            System.out.println("findById:" + findById);
            // 3. searchByTitle("clean")
            List<Book> searchByTitle = bookRepository.searchByTitle("clean");
            System.out.println("searchByTitle:" + searchByTitle);
            // 4. update
            Book update = bookRepository.update(1, new Book("java", "Behzad", 2020));
            System.out.println("update:" + update);
            // 5. updateAvailability(false)
            bookRepository.updateAvailability(1, false);
            Optional<Book> findById2 = bookRepository.findById(1);
            System.out.println("findById:" + findById2);
            // 6. delete
            bookRepository.delete(1);
            // 7. findById بعد از delete باید Optional.empty باشد
            Optional<Book> findById3 = bookRepository.findById(1);
            System.out.println("findById:" + findById3);
        } catch (RuntimeException | SQLException e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}



