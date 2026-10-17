package library.services;

import library.exceptions.LibraryException;
import library.domain.Book;
import java.util.ArrayList;

public class BookServices {

    private ArrayList<Book> books = new ArrayList<>();

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

    public ArrayList<Book> allBooks() {
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

    public void clearBooks() {
        books.clear();
    }
}
