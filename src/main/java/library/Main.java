package library;

import library.config.DatabaseConnection;
import library.domain.Book;
import library.domain.Member;
import library.exceptions.LibraryException;
import library.repository.BookRepository;
import library.repository.LibraryRepository;
import library.repository.MemberRepository;
import library.services.BookServices;
import library.services.LibraryServices;
import library.services.MemberServices;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseConnection db = new DatabaseConnection();
        try (Connection connection = db.getConnection("jdbc:postgresql://localhost:5000/library_db", "postgres", "Behzad123")) {

            BookServices bookServices = new BookServices(new BookRepository(connection));
            MemberServices memberServices = new MemberServices(new MemberRepository(connection));
            LibraryServices libraryServices = new LibraryServices(new LibraryRepository(connection));

            runMenu(bookServices, memberServices, libraryServices);
        } catch (RuntimeException | SQLException e) {
            System.out.println("Could not connect to the database: " + e.getMessage());
        }
    }

    private static void runMenu(BookServices bookServices, MemberServices memberServices,LibraryServices libraryServices) {
        Scanner input = new Scanner(System.in);
        System.out.println("===== Library System =====");
        String[] choices = {
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
        while (true) {
            for (int i = 0; i < choices.length; i++) {
                System.out.println(i + 1 + ". " + choices[i]);
            }
            System.out.print("CHOICE: ");
            byte choice;
            try {
                choice = input.nextByte();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                input.nextLine();
                continue;
            }
            input.nextLine();
            switch (choice) {
                case 1:
                    try {
                        System.out.print("Write your book name: ");
                        String title = input.nextLine();
                        System.out.print("Write your book author: ");
                        String author = input.nextLine();
                        System.out.print("Write your book year: ");
                        int year = input.nextInt();
                        input.nextLine();
                        bookServices.addBook(title, author, year);
                        System.out.println("Book added successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid year! Please enter a number.");
                        input.nextLine();
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    List<Book> allBooks = bookServices.allBooks();
                    if (allBooks.isEmpty()) {
                        System.out.println("Book not found!");
                        break;
                    }
                    for (Book i : allBooks) {
                        System.out.println(i.toString());
                    }
                    break;
                case 3:
                    System.out.print("Search Book: ");
                    String search = input.nextLine();
                    var searchList = bookServices.searchBook(search);

                    if (searchList.isEmpty()) {
                        System.out.println("Book not found!");
                        break;

                    }
                    for (Book i : searchList) {
                        System.out.println(i.toString());
                    }
                    break;
                case 4:
                    try {
                        System.out.print("Enter Book ID: ");
                        long bookId = input.nextLong();
                        input.nextLine();
                        System.out.print("Edit your book name: ");
                        String selectedTitle = input.nextLine();
                        System.out.print("Edit your book author: ");
                        String selectedAuthor = input.nextLine();
                        System.out.print("Edit your book year: ");
                        int selectedYear = input.nextInt();
                        input.nextLine();
                        Book edited = bookServices.editBook(bookId, selectedTitle, selectedAuthor, selectedYear);
                        System.out.println(edited);
                        System.out.println("Book edited successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        input.nextLine();
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    try {
                        System.out.print("Enter book ID: ");
                        long bookId = input.nextLong();
                        input.nextLine();
                        bookServices.deleteBook(bookId);
                        System.out.println("Book deleted successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid book number! Please enter a number.");
                        input.nextLine();
                        continue;
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    try {
                        System.out.print("Enter name: ");
                        String name = input.nextLine();
                        System.out.print("Enter email: ");
                        String email = input.nextLine();
                        memberServices.addMember(name, email);
                        System.out.println("Member added successfully!");
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    List<Member> membersList = memberServices.allMembers();
                    if (membersList.isEmpty()) {
                        System.out.println("Member not found!");
                        break;
                    }
                    for (Member i : membersList) {
                        System.out.println(i.toString());
                    }
                    break;
                case 8:
                    System.out.print("Search Member: ");
                    String searchMember = input.nextLine();
                    var searchMemberList = memberServices.searchMember(searchMember);

                    if (searchMemberList.isEmpty()) {
                        System.out.println("Member not found!");
                        break;

                    }
                    for (Member i : searchMemberList) {
                        System.out.println(i.toString());
                    }
                    break;
                case 9:
                    try {
                        System.out.print("Enter Member ID: ");
                        long editMemberId = input.nextLong();
                        input.nextLine();
                        System.out.print("Edit your Member name: ");
                        String editMemberName = input.nextLine();
                        System.out.print("Edit your Member Email: ");
                        String editMemberEmail = input.nextLine();
                        memberServices.editMember(editMemberId, editMemberName, editMemberEmail);
                        System.out.println("Member edited successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        input.nextLine();
                        continue;
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 10:
                    try {
                        System.out.print("Enter Member id: ");
                        long removeMemberId = input.nextLong();
                        input.nextLine();
                        memberServices.deleteMember(removeMemberId);
                        System.out.println("Member deleted successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid ID! Please enter a number.");
                        input.nextLine();
                        continue;
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 11:
                    try {
                        System.out.print("Enter Book ID: ");
                        long bookId = input.nextLong();
                        input.nextLine();
                        System.out.print("Enter Member id: ");
                        long memberId = input.nextLong();
                        input.nextLine();
                        libraryServices.borrowBook(bookId, memberId);
                        System.out.println("Book borrowed successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        input.nextLine();
                        continue;
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 12:
                    try {
                        System.out.print("Enter Book ID: ");
                        long bookId = input.nextLong();
                        input.nextLine();
                        System.out.print("Enter Member id: ");
                        long memberId = input.nextLong();
                        input.nextLine();
                        libraryServices.returnBook(bookId, memberId);
                        System.out.println("Book returned successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input! Please enter a number.");
                        input.nextLine();
                        continue;
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 13:
                    System.out.println("Goodbye!");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid input! Please enter a number.");
            }
        }
    }

}