package library.repository;

import library.exceptions.LibraryException;
import library.domain.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private final Connection connection;

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    public Book insert(Book book) {
        String sql = "INSERT INTO books (title, author, year, is_available) VALUES (?, ?, ?, ?)";


        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            pstmt.setBoolean(4, book.isAvailable());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new LibraryException("Creating book failed, no rows affected.");
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    long generatedId = generatedKeys.getLong(1);

                    return new Book(
                            generatedId,
                            book.getTitle(),
                            book.getAuthor(),
                            book.getYear(),
                            book.isAvailable()
                    );
                } else {
                    throw new LibraryException("Creating book failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            throw new LibraryException("Database error: " + e.getMessage());
        }
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT id, title, author, year, is_available FROM books";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                boolean isAvailable = rs.getBoolean("is_available");

                Book book = new Book(id, title, author, year, isAvailable);
                books.add(book);
            }

        } catch (SQLException e) {
            throw new LibraryException("Database error: " + e.getMessage());
        }

        return books;
    }
}
