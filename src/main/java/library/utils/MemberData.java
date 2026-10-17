package library.utils;

import java.util.ArrayList;
import java.util.List;

public class MemberData {
    private long id;
    private String name;
    private String email;
    private List<Long> borrowedBookIds;

    public MemberData(long id, String name, String email,  List<Long>  borrowedBookIds) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.borrowedBookIds = borrowedBookIds;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return
                this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public List<Long> getBorrowedBookIds() {
        return this.borrowedBookIds == null ? new ArrayList<>() : this.borrowedBookIds;
    }
}
