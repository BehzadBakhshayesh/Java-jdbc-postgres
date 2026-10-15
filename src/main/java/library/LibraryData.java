package library;

import java.util.ArrayList;
import java.util.List;

public class LibraryData {
    private List<BookData> books;
    private List<MemberData> members;

    public LibraryData(List<BookData> books, List<MemberData> members) {
        this.books = books;
        this.members = members;
    }

    public List<BookData> getBooks() {
        return this.books == null ? new ArrayList<>() : this.books;
    }

    public List<MemberData> getMembers() {
        return this.members == null ? new ArrayList<>() : this.members;
    }
}