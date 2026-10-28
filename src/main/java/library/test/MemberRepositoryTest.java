package library.test;

import library.config.DatabaseConnection;
import library.domain.Member;
import library.exceptions.LibraryException;
import library.repository.MemberRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class MemberRepositoryTest {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection connection = db.getConnection(
                "jdbc:postgresql://localhost:5000/library_db",
                "postgres",
                "Behzad123"
        )) {
            MemberRepository repo = new MemberRepository(connection);

            Member m1 = repo.insert(new Member("Ali", "ali@test.com"));
            Member m2 = repo.insert(new Member("Sara", "sara@test.com"));
            System.out.println("Inserted: " + m1);
            System.out.println("Inserted: " + m2);

            System.out.println("findById: " + repo.findById(m1.getId()));
            System.out.println("search 'sara': " + repo.search("sara"));
            System.out.println("findAll: " + repo.findAll());

            Member updated = repo.update(m1.getId(), new Member("Ali Reza", "alireza@test.com"));
            System.out.println("update: " + updated);

            // ایمیل تکراری
            try {
                repo.insert(new Member("Fake", "sara@test.com"));
                System.out.println("ERROR: expected duplicate email exception");
            } catch (LibraryException e) {
                System.out.println("Expected error: " + e.getMessage());
            }

            // پاکسازی
            repo.delete(m1.getId());
            repo.delete(m2.getId());
            System.out.println("after delete: " + repo.findById(m1.getId()));

            // حذف دوباره
            try {
                repo.delete(m1.getId());
                System.out.println("ERROR: expected exception");
            } catch (LibraryException e) {
                System.out.println("Expected error: " + e.getMessage());
            }
        } catch (RuntimeException | SQLException e) {
            System.out.println("Test failed: " + e.getMessage());
        }
    }
}