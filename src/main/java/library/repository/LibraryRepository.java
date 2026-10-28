package library.repository;

import library.exceptions.LibraryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LibraryRepository {
    private final Connection connection;

    public LibraryRepository(Connection connection) {
        this.connection = connection;
    }

    public void borrowBook(long bookId, long memberId) {
        try {
            connection.setAutoCommit(false);
            try {
                String lockSql = "UPDATE books SET is_available = FALSE WHERE id = ? AND is_available = TRUE";
                try (PreparedStatement pstmt = connection.prepareStatement(lockSql)) {
                    pstmt.setLong(1, bookId);
                    if (pstmt.executeUpdate() == 0) {
                        throw new LibraryException("Book not found or already borrowed!");
                    }
                }

                String insertSql = "INSERT INTO borrowings (book_id, member_id) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertSql)) {
                    pstmt.setLong(1, bookId);
                    pstmt.setLong(2, memberId);
                    pstmt.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                if ("23503".equals(e.getSQLState())) {
                    throw new LibraryException("Member not found!");
                }
                throw new LibraryException("Database error: " + e.getMessage());
            } catch (LibraryException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new LibraryException("Database error: " + e.getMessage());
        }
    }

    public void returnBook(long bookId, long memberId) {
        try {
            connection.setAutoCommit(false);
            try {
                String closeSql = """
                        UPDATE borrowings SET returned_at = NOW()
                        WHERE book_id = ? AND member_id = ? AND returned_at IS NULL
                        """;
                try (PreparedStatement pstmt = connection.prepareStatement(closeSql)) {
                    pstmt.setLong(1, bookId);
                    pstmt.setLong(2, memberId);
                    if (pstmt.executeUpdate() == 0) {
                        throw new LibraryException("This member didn't borrow this book!");
                    }
                }

                String freeSql = "UPDATE books SET is_available = TRUE WHERE id = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(freeSql)) {
                    pstmt.setLong(1, bookId);
                    pstmt.executeUpdate();
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new LibraryException("Database error: " + e.getMessage());
            } catch (LibraryException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new LibraryException("Database error: " + e.getMessage());
        }
    }
}