package library.controller;

import library.domain.Book;
import library.domain.Member;
import library.exceptions.LibraryException;
import library.services.BookServices;
import library.services.LibraryServices;
import library.services.MemberServices;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private final Scanner input = new Scanner(System.in);
    private final BookServices bookServices;
    private final MemberServices memberServices;
    private final LibraryServices libraryServices;

    private static final String[] CHOICES = {
            "Add Book",
            "Show Books",
            "Search Book",
            "Edit Book",
            "Delete Book",
            "Add Member",
            "Show Members",
            "Search Member",
            "Edit Member",
            "Delete Member",
            "Borrow Book",
            "Return Book",
            "Exit"
    };

    public Menu(BookServices bookServices, MemberServices memberServices, LibraryServices libraryServices) {
        this.bookServices = bookServices;
        this.memberServices = memberServices;
        this.libraryServices = libraryServices;
    }

    public void run() {
        System.out.println("===== Library System =====");
        while (true) {
            this.printMenu();
            try {
                int choice = this.readInt("CHOICE: ");
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> this.showBooks();
                    case 3 -> this.searchBook();
                    case 4 -> this.editBook();
                    case 5 -> this.deleteBook();
                    case 6 -> this.addMember();
                    case 7 -> this.showMembers();
                    case 8 -> this.searchMember();
                    case 9 -> this.editMember();
                    case 10 -> this.deleteMember();
                    case 11 -> this.borrowBook();
                    case 12 -> this.returnBook();
                    case 13 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            } catch (LibraryException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printMenu() {
        for (int i = 0; i < CHOICES.length; i++) {
            System.out.println(i + 1 + ". " + CHOICES[i]);
        }
    }

    private String readString(String msg) {
        System.out.print(msg);
        return input.nextLine();
    }

    private int readInt(String msg) {
        String text = readString(msg).trim();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new LibraryException("Invalid input! Please enter a number.");
        }
    }

    private long readLong(String msg) {
        String text = readString(msg).trim();
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            throw new LibraryException("Invalid input! Please enter a number.");
        }
    }

    private void addBook() {
        String title = readString("Write your book name: ");
        String author = readString("Write your book author: ");
        int year = readInt("Write your book year: ");
        bookServices.addBook(title, author, year);
        System.out.println("Book added successfully!");
    }

    private void showBooks() {
        List<Book> allBooks = bookServices.allBooks();
        if (allBooks.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        allBooks.forEach(System.out::println);
    }

    private void searchBook() {
        String search = this.readString("Search Book: ");
        var searchList = bookServices.searchBook(search);
        if (searchList.isEmpty()) {
            System.out.println("No books found.");
            return;
        }
        searchList.forEach(System.out::println);
    }

    private void editBook() {
        long id = readLong("Enter Book ID: ");
        String title = readString("Edit your book name: ");
        String author = readString("Edit your book author: ");
        int year = readInt("Edit your book year: ");
        Book edited = bookServices.editBook(id, title, author, year);
        System.out.println(edited);
        System.out.println("Book edited successfully!");
    }

    private void deleteBook() {
        long id = readLong("Enter Book ID: ");
        bookServices.deleteBook(id);
        System.out.println("Book deleted successfully!");
    }

    private void addMember() {
        String name = this.readString("Enter name: ");
        String email = this.readString("Enter email: ");
        memberServices.addMember(name, email);
        System.out.println("Member added successfully!");
    }

    private void showMembers() {
        List<Member> membersList = memberServices.allMembers();
        if (membersList.isEmpty()) {
            System.out.println("Member not found!");
        }
        for (Member i : membersList) {
            System.out.println(i.toString());
        }

    }

    private void searchMember() {
        String searchMember = this.readString("Search Member: ");
        var searchMemberList = memberServices.searchMember(searchMember);
        if (searchMemberList.isEmpty()) {
            System.out.println("Member not found!");

        }
        for (Member i : searchMemberList) {
            System.out.println(i.toString());
        }
    }

    private void editMember() {
        long editMemberId = this.readLong("Enter Member ID: ");
        String editMemberName = this.readString("Edit your Member name: ");
        String editMemberEmail = this.readString("Edit your Member Email: ");
        memberServices.editMember(editMemberId, editMemberName, editMemberEmail);
        System.out.println("Member edited successfully!");
    }

    private void deleteMember() {
        long removeMemberId = this.readLong("Enter Member id: ");
        memberServices.deleteMember(removeMemberId);
        System.out.println("Member deleted successfully!");
    }

    private void borrowBook() {
        long bookId = this.readLong("Enter Book ID: ");
        long memberId = this.readLong("Enter Member id: ");
        libraryServices.borrowBook(bookId, memberId);
        System.out.println("Book borrowed successfully!");
    }

    private void returnBook() {
        long bookId = this.readLong("Enter Book ID: ");
        long memberId = this.readLong("Enter Member id: ");
        libraryServices.returnBook(bookId, memberId);
        System.out.println("Book returned successfully!");
    }
}
