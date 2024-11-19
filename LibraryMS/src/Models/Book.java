package Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.DatabaseConnection;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private String isbn;
    private boolean isAvailable;

    public Book(String title, String author, String genre, int publicationYear, String isbn) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.isAvailable = true;
    }

    public Book(int id, String title, String author, String genre, int publicationYear, String isbn, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
    }
    
    
    public static List<Book> searchBooksByGenre(String genre) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE genre = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, genre);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getInt("publication_year"),
                        resultSet.getString("isbn"),
                        resultSet.getBoolean("is_available")
                ));
            }
        }
        return books;
    }

    // Add a book to the database
    public static void addBookToDatabase(Book book) throws SQLException {
        String query = "INSERT INTO books (title, author, genre, publication_year, isbn, is_available) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, book.title);
            preparedStatement.setString(2, book.author);
            preparedStatement.setString(3, book.genre);
            preparedStatement.setInt(4, book.publicationYear);
            preparedStatement.setString(5, book.isbn);
            preparedStatement.setBoolean(6, book.isAvailable);
            preparedStatement.executeUpdate();
        }
    }

    // Get a book by its ISBN
    public static Book getBookByISBN(String isbn) throws SQLException {
        String query = "SELECT * FROM books WHERE isbn = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getInt("publication_year"),
                        resultSet.getString("isbn"),
                        resultSet.getBoolean("is_available")
                );
            }
        }
        return null; // Book not found
    }

    // Update the availability of a book
    public static void updateBookAvailability(String isbn, boolean isAvailable) throws SQLException {
        String query = "UPDATE books SET is_available = ? WHERE isbn = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, isAvailable);
            preparedStatement.setString(2, isbn);
            preparedStatement.executeUpdate();
        }
    }

    // Getters for Book properties
    public boolean checkAvailability() {
        return isAvailable;
    }

    public void borrowBook() {
        isAvailable = false;
    }

    public void returnBook() {
        isAvailable = true;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (" + genre + ", " + publicationYear + ") ISBN: " + isbn + ", Available: " + isAvailable;
    }
}
