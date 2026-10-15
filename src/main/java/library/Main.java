package library;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Library library = new Library(new JsonStorage("library.json"));
        try {
            library.load();
        } catch (LibraryException e) {
            System.out.println("Could not load data: " + e.getMessage());
            System.out.println("Exiting without saving to protect your files.");
            return;
        }
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
                        library.addBook(title, author, year);
                        System.out.println("Book added successfully!");
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid year! Please enter a number.");
                        input.nextLine();
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    ArrayList<Book> showList = library.showBooks();
                    if (showList.isEmpty()) {
                        System.out.println("Book not found!");
                        break;
                    }
                    for (Book i : showList) {
                        System.out.println(i.toString());
                    }
                    break;
                case 3:
                    System.out.print("Search Book: ");
                    String search = input.nextLine();
                    var searchList = library.searchBook(search);

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
                        int bookId = input.nextInt();
                        input.nextLine();
                        System.out.print("Edit your book name: ");
                        String selectedTitle = input.nextLine();
                        System.out.print("Edit your book author: ");
                        String selectedAuthor = input.nextLine();
                        System.out.print("Edit your book year: ");
                        int selectedYear = input.nextInt();
                        input.nextLine();
                        Book edited = library.editBook(bookId, selectedTitle, selectedAuthor, selectedYear);
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
                        int bookId = input.nextInt();
                        input.nextLine();
                        library.deleteBook(bookId);
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
                        library.addMember(name, email);
                        System.out.println("Member added successfully!");
                    } catch (LibraryException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    ArrayList<Member> membersList = library.showMembers();
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
                    var searchMemberList = library.searchMember(searchMember);

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
                        int editMemberId = input.nextInt();
                        input.nextLine();
                        System.out.print("Edit your Member name: ");
                        String editMemberName = input.nextLine();
                        System.out.print("Edit your Member Email: ");
                        String editMemberEmail = input.nextLine();
                        library.editMember(editMemberId, editMemberName, editMemberEmail);
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
                        int removeMemberId = input.nextInt();
                        input.nextLine();
                        library.deleteMember(removeMemberId);
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
                        int bookId = input.nextInt();
                        input.nextLine();
                        System.out.print("Enter Member id: ");
                        int memberId = input.nextInt();
                        input.nextLine();
                        library.borrowBook(bookId, memberId);
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
                        int bookId = input.nextInt();
                        input.nextLine();
                        System.out.print("Enter Member id: ");
                        int memberId = input.nextInt();
                        input.nextLine();
                        library.returnBook(bookId, memberId);
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
                    try {
                        library.save();
                    } catch (LibraryException e) {
                        System.out.println("Could not save data: " + e.getMessage());
                        System.out.println("Fix the problem and choose Exit again.");
                        break;
                    }
                    System.out.println("Goodbye!");
                    input.close();
                    return;
                default:
                    System.out.println("Invalid input! Please enter a number.");
            }
        }
    }
}