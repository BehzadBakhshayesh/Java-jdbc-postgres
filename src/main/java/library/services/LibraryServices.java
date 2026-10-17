package library.services;

import library.utils.IdGenerator;
import library.exceptions.LibraryException;
import library.utils.LibraryStorage;
import library.domain.Book;
import library.domain.Member;
import library.utils.BookData;
import library.utils.LibraryData;
import library.utils.MemberData;

import java.util.List;
import java.util.stream.Collectors;

public class LibraryServices {
    private final LibraryStorage storage;
    BookServices bookServices=  BookServices.getInstance();
    MemberServices  memberServices=  MemberServices.getInstance();
    public LibraryServices(LibraryStorage storage) {
        this.storage = storage;
    }

    public void borrowBook(long bookId, long memberId) {
        Book foundBook = bookServices.findBookById(bookId);
        if (foundBook == null) {
            throw new LibraryException("Book not found!");
        }
        if (!foundBook.isAvailable()) {
            throw new LibraryException("Book is already borrowed!");
        }

        Member foundMember = memberServices.findMemberById(memberId);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        foundMember.addBorrowedBooks(foundBook);
        foundBook.setIsAvailable(false);
    }

    public void returnBook(long bookId, long memberId) {
        Book foundBook = bookServices.findBookById(bookId);
        if (foundBook == null) {
            throw new LibraryException("Book not found!");
        }
        if (foundBook.isAvailable()) {
            throw new LibraryException("Book is not borrowed!");
        }
        Member foundMember = memberServices.findMemberById(memberId);
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
        List<BookData> booksData = bookServices.allBooks().stream().map(Book::toData).collect(Collectors.toList());
        List<MemberData> membersData = memberServices.allMembers().stream().map(Member::toData).collect(Collectors.toList());
        this.storage.save(new LibraryData(booksData, membersData));
    }

    public void load() {
        bookServices.clearBooks();
        memberServices.clearMembers();

        LibraryData data = this.storage.load();

        long maxBookId = 0;
        for (BookData bd : data.getBooks()) {
            Book book = Book.fromData(bd);
            bookServices.addBook(book.getTitle(),book.getAuthor(),book.getYear());
            maxBookId = Math.max(maxBookId, book.getId());
        }
        IdGenerator.setBookCounter(maxBookId + 1);

        long maxMemberId = 0;
        for (MemberData md : data.getMembers()) {
            Member member = Member.fromData(md);
            for (long bookId : md.getBorrowedBookIds()) {
                Book book = bookServices.findBookById(bookId);
                if (book == null) {
                    throw new LibraryException("Member " + member.getId()
                            + " refers to a missing book: " + bookId);
                }
                member.addBorrowedBooks(book);
            }
            memberServices.addMember(member.getName(), member.getEmail());
            maxMemberId = Math.max(maxMemberId, member.getId());
        }
        IdGenerator.setMemberCounter(maxMemberId + 1);
    }
}
