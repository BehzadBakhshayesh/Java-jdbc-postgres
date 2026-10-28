package library.services;

import library.repository.LibraryRepository;

public class LibraryServices {
    private final LibraryRepository libraryRepository;

    public LibraryServices(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public void borrowBook(long bookId, long memberId) {
        libraryRepository.borrowBook(bookId, memberId);
    }

    public void returnBook(long bookId, long memberId) {
        libraryRepository.returnBook(bookId, memberId);
    }
}