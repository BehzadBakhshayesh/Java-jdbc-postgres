package library.domain;

import library.utils.BookData;
import library.exceptions.LibraryException;

import java.time.Year;

public class Book {
    private final long id;
    private String title;
    private String author;
    private int year;
    private boolean isAvailable;

    public Book(String title, String author, int year) {
        this(0, title, author, year, true);
    }

    public Book(long id, String title, String author, int year, boolean isAvailable) {
        validateParams(title, author, year);
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = isAvailable;
    }

    public long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        validateTitle(title);
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        validateAuthor(author);
        this.author = author;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        validateYear(year);
        this.year = year;
    }

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new LibraryException("Title can not be blank");
        }
    }

    private static void validateAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new LibraryException("Author can not be blank");
        }
    }

    private static void validateYear(int year) {
        if (year < 1 || year > Year.now().getValue()) {
            throw new LibraryException("Year is not valid");
        }
    }

    public static void validateParams(String title, String author, int year) {
        validateTitle(title);
        validateAuthor(author);
        validateYear(year);
    }

    @Override
    public String toString() {
        return "ID : " + this.id + " | Title: " + this.title + " | Author: " + this.author + " | Year: " + this.year + " | Status: " + (this.isAvailable ? "Available" : "Borrowed");
    }
}



