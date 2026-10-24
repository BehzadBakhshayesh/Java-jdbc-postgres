package library.repository;

import java.sql.Connection;

public class MemberRepository {
    private final Connection connection;

    public MemberRepository(Connection connection) {
        this.connection = connection;
    }
}
