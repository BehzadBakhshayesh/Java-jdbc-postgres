package library.repository;

import library.domain.Book;
import library.domain.Member;
import library.exceptions.LibraryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemberRepository {
    private final Connection connection;
    private static final String SELECT_COLUMNS = "SELECT id, name, email FROM members";

    public MemberRepository(Connection connection) {
        this.connection = connection;
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email")
        );
    }

    private LibraryException toLibraryException(SQLException e) {
        if ("23505".equals(e.getSQLState())) {
            return new LibraryException("Email already exists!");
        }
        return new LibraryException("Database error: " + e.getMessage());
    }

    public Member insert(Member member) {
        String sql = "INSERT INTO members (name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());

            if (pstmt.executeUpdate() == 0) {
                throw new LibraryException("Creating member failed, no rows affected.");
            }
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Member(keys.getLong(1), member.getName(), member.getEmail());
                }
                throw new LibraryException("Creating member failed, no ID obtained.");
            }
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
    }

    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();
        String sql = SELECT_COLUMNS + " ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
        return members;
    }

    public Optional<Member> findById(long id) {
        String sql = SELECT_COLUMNS + " WHERE id = ?";
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

    public List<Member> search(String text) {
        String sql = SELECT_COLUMNS + " WHERE name ILIKE ? OR email ILIKE ? ORDER BY id";
        List<Member> result = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + text + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
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

    public Member update(long id, Member member) {
        String sql = "UPDATE members SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getEmail());
            pstmt.setLong(3, id);

            if (pstmt.executeUpdate() == 0) {
                throw new LibraryException("Member not found with id: " + id);
            }
            return new Member(id, member.getName(), member.getEmail());
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
    }

    public void delete(long id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            if (pstmt.executeUpdate() == 0) {
                throw new LibraryException("Member not found with id: " + id);
            }
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
    }

    public List<Book> findBorrowedBooks(long memberId) {
        String sql = """
            SELECT b.id, b.title, b.author, b.year, b.is_available
            FROM books b
            JOIN borrowings br ON br.book_id = b.id
            WHERE br.member_id = ? AND br.returned_at IS NULL
            ORDER BY b.id
            """;
        List<Book> result = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new Book(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getInt("year"),
                            rs.getBoolean("is_available")
                    ));
                }
            }
        } catch (SQLException e) {
            throw toLibraryException(e);
        }
        return result;
    }
}