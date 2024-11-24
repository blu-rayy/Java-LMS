package backend;
import java.sql.*;

public class LibraryDatabase {

    // Create tables for Books, Authors, and Members
    public static void createTables() {
        try (Connection conn = SQLiteDatabase.connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Create Books table
                String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "title TEXT NOT NULL, "
                        + "author TEXT NOT NULL, "
                        + "isbn TEXT NOT NULL, "
                        + "publicationDate TEXT, "
                        + "availableCopies INTEGER)";
                stmt.execute(createBooksTable);

                // Create Authors table
                String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL)";
                stmt.execute(createAuthorsTable);

                // Create Members table
                String createMembersTable = "CREATE TABLE IF NOT EXISTS members ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL, "
                        + "memberID TEXT NOT NULL)";
                stmt.execute(createMembersTable);

                System.out.println("Tables created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    // Insert a new book into the database
    public static void insertBook(Book book) {
        String sql = "INSERT INTO books(title, author, isbn, publicationDate, availableCopies) VALUES(?,?,?,?,?)";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getISBN());
            pstmt.setString(4, book.getPublicationDate());
            pstmt.setInt(5, book.getAvailableCopies());
            pstmt.executeUpdate();
            System.out.println("Book added: " + book.getTitle());
        } catch (SQLException e) {
            System.out.println("Error inserting book: " + e.getMessage());
        }
    }

    // Insert a new member into the database
    public static void insertMember(Member member) {
        String sql = "INSERT INTO members(name, memberID) VALUES(?,?)";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getID());
            pstmt.executeUpdate();
            System.out.println("Member added: " + member.getName());
        } catch (SQLException e) {
            System.out.println("Error inserting member: " + e.getMessage());
        }
    }

    // Read all books from the database
    public static void readBooks() {
        String sql = "SELECT * FROM books";
        try (Connection conn = SQLiteDatabase.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") + ", Author: " + rs.getString("author"));
            }
        } catch (SQLException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
    }

    // Update book availability in the database
    public static void updateBookAvailability(int bookId, int availableCopies) {
        String sql = "UPDATE books SET availableCopies = ? WHERE id = ?";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, availableCopies);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            System.out.println("Book availability updated.");
        } catch (SQLException e) {
            System.out.println("Error updating book availability: " + e.getMessage());
        }
    }

    // Delete a book from the database
    public static void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            System.out.println("Book deleted.");
        } catch (SQLException e) {
            System.out.println("Error deleting book: " + e.getMessage());
        }
    }
}
