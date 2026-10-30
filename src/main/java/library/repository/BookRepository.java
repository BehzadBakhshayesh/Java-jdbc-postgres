package library.repository;

import library.exceptions.LibraryException;
import library.domain.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepository {
    private final Connection connection;
    private static final String SELECT_COLUMNS = "SELECT id, title, author, year, is_available FROM books";

    public BookRepository(Connection connection) {
        this.connection = connection;
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("year"),
                rs.getBoolean("is_available")
        );
    }


    private LibraryException toLibraryException(SQLException e) {
        return new LibraryException("Database error: " + e.getMessage());
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
            throw toLibraryException(e);
        }
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql =SELECT_COLUMNS + " ORDER BY id";

        try (
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                Book book = mapRow(rs);
                books.add(book);
            }

        } catch (SQLException e) {
            throw toLibraryException(e);
        }

        return books;
    }

    public Optional<Book> findById(long id) {
        String sql =SELECT_COLUMNS + " WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
    }

    public List<Book> searchByTitle(String text) {
        String sql = SELECT_COLUMNS + " WHERE title ILIKE ? ORDER BY id";
        List<Book> result = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + text + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
        return result;
    }

    public Book update(long id, Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getYear());
            pstmt.setLong(4, id);

            if (pstmt.executeUpdate() == 0) {
                throw new LibraryException("Book not found with id: " + id);
            }
            return findById(id).orElseThrow(() -> new LibraryException("Book not found with id: " + id));        } catch (SQLException e) {
            throw toLibraryException(e);
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            if (pstmt.executeUpdate() == 0) {
                throw new LibraryException("Book not found with id: " + id);
            }

        } catch (SQLException e) {
            if ("23503".equals(e.getSQLState())) {
                throw new LibraryException("Cannot delete a book with borrowing history!");
            }
            throw toLibraryException(e);
        }
    }




}
