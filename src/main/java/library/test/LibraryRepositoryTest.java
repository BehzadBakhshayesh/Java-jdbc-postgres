package library.test;

import library.config.DatabaseConnection;
import library.domain.Book;
import library.domain.Member;
import library.exceptions.LibraryException;
import library.repository.BookRepository;
import library.repository.LibraryRepository;
import library.repository.MemberRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryRepositoryTest {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection connection = db.getConnection(
                "jdbc:postgresql://localhost:5000/library_db",
                "postgres",
                "Behzad123"
        )) {
            BookRepository books = new BookRepository(connection);
            MemberRepository members = new MemberRepository(connection);
            LibraryRepository library = new LibraryRepository(connection);

            Book book = books.insert(new Book("Java", "Behzad", 2020));
            Member ali = members.insert(new Member("Ali", "ali@test.com"));
            Member sara = members.insert(new Member("Sara", "sara@test.com"));

            library.borrowBook(book.getId(), ali.getId());
            System.out.println("after borrow: " + books.findById(book.getId()));

            try {
                library.borrowBook(book.getId(), sara.getId());
                System.out.println("ERROR: expected exception");
            } catch (LibraryException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            try {
                library.returnBook(book.getId(), sara.getId());
                System.out.println("ERROR: expected exception");
            } catch (LibraryException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            library.returnBook(book.getId(), ali.getId());
            System.out.println("after return: " + books.findById(book.getId()));

            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate("DELETE FROM borrowings");
            }
            books.delete(book.getId());
            members.delete(ali.getId());
            members.delete(sara.getId());
        } catch (RuntimeException | SQLException e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}