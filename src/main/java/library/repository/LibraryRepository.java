package library.repository;

import java.sql.Connection;

public class LibraryRepository {
    private final Connection connection;

    public LibraryRepository(Connection connection) {
        this.connection = connection;
    }
}
