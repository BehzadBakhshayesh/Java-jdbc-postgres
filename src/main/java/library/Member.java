package library;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

public class Member {
    private ArrayList<Book> borrowedBooks = new ArrayList<>();
    private final long id;
    private String name;
    private String email;

    public Member(String name, String email) {
        validateParams(name, email);
        this.id = IdGenerator.generateMemberId();
        this.name = name;
        this.email = email;
    }

    public Member(long id, String name, String email) {
        validateParams(name, email);
        this.id = id;
        this.name = name;
        this.email = email;
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new LibraryException("Name can not be blank");
        }
    }

    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new LibraryException("Email can not be blank");
        }
        if (!(email.contains("@") && email.contains("."))) {
            throw new LibraryException("Email is not valid");
        }
    }

    public static void validateParams(String name, String email) {
        validateName(name);
        validateEmail(email);
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        validateEmail(email);
        this.email = email;
    }

    public ArrayList<Book> borrowedBooks() {
        return new ArrayList<>(this.borrowedBooks);
    }

    public void addBorrowedBooks(Book book) {
        if (borrowedBooks.contains(book)) {
            throw new LibraryException("This member already borrowed this book!");
        }
        this.borrowedBooks.add(book);
    }

    public void removeBorrowedBook(Book book) {
        this.borrowedBooks.remove(book);
    }

    public boolean hasBorrowedBook(Book book) {
        return borrowedBooks.contains(book);
    }

    public MemberData toData() {
        List<Long> ids = this.borrowedBooks.stream().map(book -> book.getId()).collect(Collectors.toList());
        return new MemberData(this.id, this.name, this.email, ids);
    }

    public static Member fromData(MemberData d) {
        // Borrowed books are linked later by Library.load()
        return new Member(d.getId(), d.getName(), d.getEmail());
    }

    @Override
    public String toString() {
        return "ID: " + this.id + " | Name: " + this.name + " | Email: " + this.email + " | Borrowed Books: " + borrowedBooks.stream().map(book -> book.getTitle()).collect(Collectors.joining(", "));
    }
}
