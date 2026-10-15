package library;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class Library {
    private final LibraryStorage storage;
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Member> members = new ArrayList<>();


    public Library(LibraryStorage storage) {
        this.storage = storage;
    }


    public void addBook(String title, String author, int year) {
        Book newBook = new Book(title, author, year);
        this.books.add(newBook);
    }

    public Book findBookById(long id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> showBooks() {
        return new ArrayList<>(this.books);
    }

    public ArrayList<Book> searchBook(String search) {
        ArrayList<Book> searchList = new ArrayList<>();
        String searchText = search.toLowerCase();
        for (int i = 0; i < this.books.size(); i++) {
            if (this.books.get(i).getTitle().toLowerCase().contains(searchText)) {
                searchList.add(this.books.get(i));
            }
        }
        return searchList;
    }

    public Book editBook(long bookId, String title, String author, int year) {
        Book foundBook = this.findBookById(bookId);
        if (foundBook == null) {
            throw new LibraryException("Book not found! Invalid ID.");
        }
        Book.validateParams(title, author, year);
        foundBook.setTitle(title);
        foundBook.setAuthor(author);
        foundBook.setYear(year);
        return foundBook;
    }

    public void deleteBook(long bookId) {
        Book foundBook = this.findBookById(bookId);
        if (foundBook == null) {
            throw new LibraryException("An error occurred while deleting the book! The number is invalid.");
        }
        if (!foundBook.isAvailable()) {
            throw new LibraryException("Cannot delete a borrowed book!");
        }
        this.books.remove(foundBook);
    }

    public void addMember(String name, String email) {
        Member member = new Member(name, email);
        this.members.add(member);
    }

    public ArrayList<Member> showMembers() {
        return new ArrayList<>(this.members);
    }

    public ArrayList<Member> searchMember(String search) {
        ArrayList<Member> searchList = new ArrayList<>();
        String searchText = search.toLowerCase();
        for (int i = 0; i < this.members.size(); i++) {
            if (this.members.get(i).getName().toLowerCase().contains(searchText) || this.members.get(i).getEmail().toLowerCase().contains(searchText)) {
                searchList.add(this.members.get(i));
            }
        }
        return searchList;
    }

    public Member findMemberById(long id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    public void editMember(long id, String name, String email) {
        Member foundMember = this.findMemberById(id);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        Member.validateParams(name, email);
        foundMember.setName(name);
        foundMember.setEmail(email);
    }

    public void deleteMember(long id) {
        Member foundMember = this.findMemberById(id);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        if (!foundMember.borrowedBooks().isEmpty()) {
            throw new LibraryException("The member has borrowed a book!");
        }
        this.members.remove(foundMember);
    }

    public void borrowBook(long bookId, long memberId) {
        Book foundBook = this.findBookById(bookId);
        if (foundBook == null) {
            throw new LibraryException("Book not found!");
        }
        if (!foundBook.isAvailable()) {
            throw new LibraryException("Book is already borrowed!");
        }

        Member foundMember = this.findMemberById(memberId);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        foundMember.addBorrowedBooks(foundBook);
        foundBook.setIsAvailable(false);
    }

    public void returnBook(long bookId, long memberId) {
        Book foundBook = this.findBookById(bookId);
        if (foundBook == null) {
            throw new LibraryException("Book not found!");
        }
        if (foundBook.isAvailable()) {
            throw new LibraryException("Book is not borrowed!");
        }
        Member foundMember = this.findMemberById(memberId);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        if (!foundMember.hasBorrowedBook(foundBook)) {
            throw new LibraryException("This member didn't borrow this book!");
        }
        foundBook.setIsAvailable(true);
        foundMember.removeBorrowedBook(foundBook);
    }


    public void save() {
        List<BookData> booksData = this.books.stream().map(Book::toData).collect(Collectors.toList());
        List<MemberData> membersData = this.members.stream().map(Member::toData).collect(Collectors.toList());
        this.storage.save(new LibraryData(booksData, membersData));
    }
    public void load() {
        this.books.clear();
        this.members.clear();

        LibraryData data = this.storage.load();

        long maxBookId = 0;
        for (BookData bd : data.getBooks()) {
            Book book = Book.fromData(bd);
            this.books.add(book);
            maxBookId = Math.max(maxBookId, book.getId());
        }
        IdGenerator.setBookCounter(maxBookId + 1);

        long maxMemberId = 0;
        for (MemberData md : data.getMembers()) {
            Member member = Member.fromData(md);
            for (long bookId : md.getBorrowedBookIds()) {
                Book book = this.findBookById(bookId);
                if (book == null) {
                    throw new LibraryException("Member " + member.getId()
                            + " refers to a missing book: " + bookId);
                }
                member.addBorrowedBooks(book);
            }
            this.members.add(member);
            maxMemberId = Math.max(maxMemberId, member.getId());
        }
        IdGenerator.setMemberCounter(maxMemberId + 1);
    }



}
