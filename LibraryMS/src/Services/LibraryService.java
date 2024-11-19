package Services;

import java.sql.SQLException;
import java.util.List;

import Models.Book;
import Models.Member;

public class LibraryService {
    // Search books by genre
    public static List<Book> searchBooksByGenre(String genre) throws SQLException {
        return Book.searchBooksByGenre(genre);
    }

    // Borrow a book
    public static void borrowBook(Member member, Book book) throws SQLException {
        if (!book.checkAvailability()) {
            System.out.println("Book is not available.");
            return;
        }
        book.borrowBook();
        Book.updateBookAvailability(book.getIsbn(), false);
        System.out.println("Book borrowed successfully by " + member.getName());
    }

    // Return a book
    public static void returnBook(Member member, Book book) throws SQLException {
        book.returnBook();
        Book.updateBookAvailability(book.getIsbn(), true);
        System.out.println("Book returned successfully by " + member.getName());
    }
}