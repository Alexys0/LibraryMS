package Main;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import Models.Book;
import Models.Member;
import Services.LibraryService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Library Management System ===");
            System.out.println("1. Add a Book");
            System.out.println("2. Search Books by Genre");
            System.out.println("3. Add a Member");
            System.out.println("4. Borrow a Book");
            System.out.println("5. Return a Book");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            try {
                switch (choice) {
                    case 1 : addBook(scanner); break;
                    case 2 : searchBooksByGenre(scanner); break;
                    case 3 : addMember(scanner); break;
                    case 4 : borrowBook(scanner);break;
                    case 5 : returnBook(scanner);break;
                    case 6 : {
                        running = false;
                        System.out.println("Exiting the system. Goodbye!");
                    }
                    break;
                    default : System.out.println("Invalid choice. Please try again.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void addBook(Scanner scanner) throws SQLException {
        System.out.println("\n=== Add a New Book ===");
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter publication year: ");
        int year = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        Book book = new Book(title, author, genre, year, isbn);
        Book.addBookToDatabase(book);
        System.out.println("Book added successfully!");
    }

    private static void searchBooksByGenre(Scanner scanner) throws SQLException {
        System.out.println("\n=== Search Books by Genre ===");
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();

        List<Book> books = LibraryService.searchBooksByGenre(genre);
        if (books.isEmpty()) {
            System.out.println("No books found for the genre: " + genre);
        } else {
            System.out.println("Books found:");
            books.forEach(System.out::println);
        }
    }

    private static void addMember(Scanner scanner) throws SQLException {
        System.out.println("\n=== Add a New Member ===");
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();

        Member member = new Member(name);
        Member.addMemberToDatabase(member);
        System.out.println("Member added successfully!");
    }

    private static void borrowBook(Scanner scanner) throws SQLException {
        System.out.println("\n=== Borrow a Book ===");
        System.out.print("Enter Member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.nextLine();

        Member member = Member.getMemberById(memberId);
        Book book = Book.getBookByISBN(isbn);

        if (member == null) {
            System.out.println("Invalid Member ID.");
        } else if (book == null) {
            System.out.println("Invalid Book ISBN.");
        } else if (!book.checkAvailability()) {
            System.out.println("The book is currently not available.");
        } else {
            LibraryService.borrowBook(member, book);
        }
    }

    private static void returnBook(Scanner scanner) throws SQLException {
        System.out.println("\n=== Return a Book ===");
        System.out.print("Enter Member ID: ");
        int memberId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Book ISBN: ");
        String isbn = scanner.nextLine();

        Member member = Member.getMemberById(memberId);
        Book book = Book.getBookByISBN(isbn);

        if (member == null) {
            System.out.println("Invalid Member ID.");
        } else if (book == null) {
            System.out.println("Invalid Book ISBN.");
        } else {
            LibraryService.returnBook(member, book);
        }
    }
}