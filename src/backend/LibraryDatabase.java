package backend;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDatabase {

    // Create tables for Books, Authors, and Members
    public static void createTables() {
        try (Connection conn = SQLiteDatabase.connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();

                // Create Books table
                String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                        + "isbn TEXT PRIMARY KEY, "
                        + "title TEXT NOT NULL, "
                        + "author TEXT NOT NULL, "
                        + "publicationDate TEXT, "
                        + "availableCopies INTEGER)";
                stmt.execute(createBooksTable);

                // Create Authors table
                String createAuthorsTable = "CREATE TABLE IF NOT EXISTS authors ("
                        + "authorID TEXT PRIMARY KEY, "
                        + "name TEXT NOT NULL)";
                stmt.execute(createAuthorsTable);

                // Create Members table
                String createMembersTable = "CREATE TABLE IF NOT EXISTS members ("
                        + "memberID TEXT PRIMARY KEY, "
                        + "name TEXT NOT NULL)";
                stmt.execute(createMembersTable);

                System.out.println("Tables created successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    //authoID generation
    public static String generateNextAuthorID() {
        String sql = "SELECT MAX(authorID) AS maxID FROM authors";
        try (Connection conn = SQLiteDatabase.connect(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String maxID = rs.getString("maxID");
                //System.out.println("Current max AuthorID: " + maxID); // for dubugging only
                
                if (maxID != null && !maxID.isEmpty()) {
                    int nextID = Integer.parseInt(maxID.substring(1)) + 1;
                    return "A" + String.format("%03d", nextID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating authorID: " + e.getMessage());
            e.printStackTrace();
        }
        return "A001";
    }

    // Generate the next memberID
    public static String generateNextMemberID() {
        String sql = "SELECT MAX(memberID) AS maxID FROM members";
        try (Connection conn = SQLiteDatabase.connect(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String maxID = rs.getString("maxID");
                //System.out.println("Current max MemberID: " + maxID); // for dubugging only
                
                if (maxID != null && !maxID.isEmpty()) {
                    int nextID = Integer.parseInt(maxID.substring(1)) + 1;
                    return "M" + String.format("%03d", nextID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating memberID: " + e.getMessage());
            e.printStackTrace();
        }
        return "M001";
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

    public static void insertAuthor(Author author) {
        String sql = "INSERT INTO authors(authorID, name) VALUES(?,?)";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String authorID = generateNextAuthorID();
            author.setAuthorID(authorID);
            
            pstmt.setString(1, authorID);
            pstmt.setString(2, author.getName());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Author added: " + author.getName() + " with ID: " + authorID);
            } else {
                System.out.println("No rows were inserted for author: " + author.getName());
            }
        } catch (SQLException e) {
            System.out.println("Error inserting author: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    // Insert a new member into the database
    public static void insertMember(Member member) {
        String sql = "INSERT INTO members(memberID, name) VALUES(?,?)";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String memberID = generateNextMemberID();
            member.setMemberID(memberID);
            
            pstmt.setString(1, memberID);
            pstmt.setString(2, member.getName());
            
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Member added: " + member.getName() + " with ID: " + memberID);
            } else {
                System.out.println("No rows were inserted for member: " + member.getName());
            }
        } catch (SQLException e) {
            System.out.println("Error inserting member: " + e.getMessage());
            e.printStackTrace();
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

    // Method to populate books
    public static void populateBooks() {
        // Populating books
        List<Book> books = new ArrayList<>();
        books.add(new Book("Murder on The Orient Express", "Agatha Christie", "9780062678058", "1934-01-01", 5));
        books.add(new Book("The Curious Incident of the Dog in the Night-Time", "Mark Haddon", "9781400032716", "2003-05-01", 3));
        books.add(new Book("Introduction to Algorithms, 3rd Edition", "Thomas Cormen", "9780262033848", "2009-09-01", 2));
        books.add(new Book("The Secret Adversary", "Agatha Christie", "9780062074355", "1922-01-01", 3));
        books.add(new Book("Project LOKI", "Cris Ibarra", "9786214141012", "2017-04-12", 8));
        books.add(new Book("Moriartea", "Cris Ibarra", "9786214141203", "2023-05-04", 4));
        books.add(new Book("QED University", "Cris Ibarra", "9783492215626", "2016-10-08", 7));
        books.add(new Book("Oliver Twist", "Charles Dickens", "9781593081744", "1838-01-01", 5));
        books.add(new Book("Harry Potter and the Goblet of Fire", "JK Rowling", "9780439139601", "2000-07-08", 10));
        books.add(new Book("Hamlet", "William Shakespeare", "9780486278070", "1603-01-01", 3));
        books.add(new Book("IT", "Stephen King", "9780670813025", "1986-09-15", 3));
        books.add(new Book("Carrie", "Stephen King", "9780385086950", "1974-04-05", 4));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "9781101099063", "1813-01-28", 12));
        books.add(new Book("1984", "George Orwell", "9780451524935", "1949-06-08", 2));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", "1960-07-11", 3));
        books.add(new Book("Sense and Sensibility", "Jane Austen", "9780141439665", "1811-10-30", 5));
        books.add(new Book("The Adventures of Sherlock Holmes", "Arthur Conan Doyle", "9780140439074", "1892-10-14", 12));
        books.add(new Book("Persuasion", "Jane Austen", "9780141439689", "1817-12-20", 6));
        books.add(new Book("The Hound of the Baskervilles", "Arthur Conan Doyle", "9780141034324", "1902-08-01", 7));
        books.add(new Book("The Shining", "Stephen King", "9780307743657", "1977-01-28", 6));
        books.add(new Book("Harry Potter and the Sorcerer's Stone", "JK Rowling", "9780590353427", "1997-06-26", 9));
        books.add(new Book("The Old Man and the Sea", "Ernest Hemingway", "9780684801223", "1952-09-01", 4));
        books.add(new Book("A Tale of Two Cities", "Charles Dickens", "9780141439603", "1859-04-30", 7));
        books.add(new Book("War and Peace", "Leo Tolstoy", "9780199232765", "1869-01-01", 6));
        books.add(new Book("And Then There Were None", "Agatha Christie", "9780062073488", "1939-11-06", 8));
        books.add(new Book("A Spot of Bother", "Mark Haddon", "9780099506928", "2006-05-18", 5));
        books.add(new Book("Algorithms Unlocked", "Thomas Cormen", "9780262518802", "2013-03-01", 6));
        books.add(new Book("A Study in Scarlet", "Arthur Conan Doyle", "9780140439083", "1887-11-01", 7));
        books.add(new Book("Animal Farm", "George Orwell", "9780451526342", "1945-08-17", 10));
        books.add(new Book("Harry Potter and the Chamber of Secrets", "JK Rowling", "9780439064873", "1998-07-02", 11));
        books.add(new Book("Go Set a Watchman", "Harper Lee", "9780062409850", "2015-07-14", 3));
        books.add(new Book("For Whom the Bell Tolls", "Ernest Hemingway", "9780684803357", "1940-10-21", 4));
        books.add(new Book("Great Expectations", "Charles Dickens", "9780141439566", "1861-01-01", 6));
        books.add(new Book("Anna Karenina", "Leo Tolstoy", "9780140449174", "1877-01-01", 8));
        books.add(new Book("Macbeth", "William Shakespeare", "9780743477109", "1606-01-01", 7));
        books.add(new Book("The ABC Murders", "Agatha Christie", "9780062073571", "1936-01-06", 4));
        books.add(new Book("Boom!", "Mark Haddon", "9780385751872", "1992-07-06", 5));
        books.add(new Book("Data Structures and Algorithms in Python", "Thomas Cormen", "9781118290279", "2011-09-15", 6));
        books.add(new Book("The Sign of Four", "Arthur Conan Doyle", "9780140439076", "1890-02-14", 7));
        books.add(new Book("Homage to Catalonia", "George Orwell", "9780156421171", "1938-04-25", 5));
        books.add(new Book("Harry Potter and the Prisoner of Azkaban", "JK Rowling", "9780439136365", "1999-07-08", 10));
        books.add(new Book("The Mockingbird Next Door", "Harper Lee", "9780143127660", "2014-07-15", 4));
        books.add(new Book("The Sun Also Rises", "Ernest Hemingway", "9780684800714", "1926-10-22", 6));
        books.add(new Book("David Copperfield", "Charles Dickens", "9780140439441", "1850-11-16", 7));
        books.add(new Book("Mansfield Park", "Jane Austen", "9780141439801", "1814-07-09", 5));
        books.add(new Book("The Death of Ivan Ilyich", "Leo Tolstoy", "9780140449617", "1886-01-01", 7));
        books.add(new Book("Othello", "William Shakespeare", "9780743477550", "1604-01-01", 6));
        
        // Insert books into the database
        for (Book book : books) {
            insertBook(book);
        }
    }

    // populate authors
    public static void populateAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Agatha Christie", generateNextAuthorID()));
        authors.add(new Author("Mark Haddon", generateNextAuthorID()));
        authors.add(new Author("Thomas Cormen", generateNextAuthorID()));
        authors.add(new Author("Cris Ibarra", generateNextAuthorID()));
        authors.add(new Author("Arthur Conan Doyle", generateNextAuthorID()));
        authors.add(new Author("George Orwell", generateNextAuthorID()));
        authors.add(new Author("Stephen King", generateNextAuthorID()));
        authors.add(new Author("JK Rowling", generateNextAuthorID()));
        authors.add(new Author("Harper Lee", generateNextAuthorID()));
        authors.add(new Author("Ernest Hemingway", generateNextAuthorID()));
        authors.add(new Author("Charles Dickens", generateNextAuthorID()));
        authors.add(new Author("Jane Austen", generateNextAuthorID()));
        authors.add(new Author("Leo Tolstoy", generateNextAuthorID()));
        authors.add(new Author("William Shakespeare", generateNextAuthorID()));
        

        for (Author author : authors) {
            insertAuthor(author);
        }
    }

    // populate members
    public static void populateMembers() {
        List<Member> members = new ArrayList<>();
        members.add(new Member("Ava Chen", generateNextMemberID()));
        members.add(new Member("Harumi Kitagawa", generateNextMemberID()));
        members.add(new Member("Haruma Von Brandt", generateNextMemberID()));
        
        for (Member member : members) {
            insertMember(member);
        }
    }
}
