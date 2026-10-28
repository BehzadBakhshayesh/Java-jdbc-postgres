package library.services;

import library.domain.Book;
import library.exceptions.LibraryException;
import library.repository.BookRepository;

import java.util.List;

public class BookServices {
    private final BookRepository bookRepository;

    public BookServices(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(String title, String author, int year) {
        return bookRepository.insert(new Book(title, author, year));
    }

    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    public List<Book> searchBook(String text) {
        return bookRepository.searchByTitle(text);
    }

    public Book findBookById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new LibraryException("Book not found!"));
    }

    public Book editBook(long id, String title, String author, int year) {
        Book book = findBookById(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        return bookRepository.update(id, book);
    }

    public void deleteBook(long id) {
        Book book = findBookById(id);
        if (!book.isAvailable()) {
            throw new LibraryException("Cannot delete a borrowed book!");
        }
        bookRepository.delete(id);
    }
}