package backend;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                        + "name TEXT NOT NULL, "
                        + "bookCount INTEGER)";
                stmt.execute(createAuthorsTable);
    
                // Create Members table
                String createMembersTable = "CREATE TABLE IF NOT EXISTS members ("
                        + "memberID TEXT PRIMARY KEY, "
                        + "name TEXT NOT NULL, "
                        + "username TEXT NOT NULL, "
                        + "email TEXT NOT NULL, "
                        + "phoneNumber TEXT NOT NULL, "
                        + "registrationDate TEXT NOT NULL, "
                        + "password TEXT NOT NULL, "
                        + "userType TEXT NOT NULL)";
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
                if (maxID != null && !maxID.isEmpty()) {
                    int nextID = Integer.parseInt(maxID.substring(1)) + 1;
                    return "A" + String.format("%03d", nextID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error generating authorID: " + e.getMessage());
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
            //e.printStackTrace();
        }
        return "M001";
    }

    // Get member details by name
    public static Member getMemberDetails(String name) {
        String sql = "SELECT * FROM members WHERE username = ?";
        try (Connection conn = SQLiteDatabase.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberID(rs.getString("memberID"));
                    member.setName(rs.getString("name"));
                    member.setUsername(rs.getString("username"));
                    member.setEmail(rs.getString("email"));
                    member.setPhoneNumber(rs.getString("phoneNumber"));
                    member.setRegistrationDate(rs.getString("registrationDate"));
                    member.setPassword(rs.getString("password"));
                    member.setUserType(rs.getString("userType"));
                    return member;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting member details by name: " + e.getMessage());
            //e.printStackTrace();
        }
        return null;
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
        String sql = "INSERT INTO authors(authorID, name, bookCount) VALUES(?, ?, ?)";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String authorID = generateNextAuthorID();
            author.setAuthorID(authorID);
            
            pstmt.setString(1, authorID);
            pstmt.setString(2, author.getName());
            pstmt.setInt(3, author.getBookCount());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Author added: " + author.getName() + " with ID: " + authorID);
            } else {
                System.out.println("No rows were inserted for author: " + author.getName());
            }
        } catch (SQLException e) {
            System.out.println("Error inserting author: " + e.getMessage());
        }
    }

    public static void updateAuthor(Author author) {
        String sql = "UPDATE authors SET name = ?, bookCount = ? WHERE authorID = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, author.getName());
            pstmt.setInt(2, author.getBookCount());
            pstmt.setString(3, author.getAuthorID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating author: " + e.getMessage());
        }
    }

    public static void deleteAuthor(String authorID) {
        String sql = "DELETE FROM authors WHERE authorID = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, authorID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting author: " + e.getMessage());
        }
    }
    
    public static List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        String sql = "SELECT * FROM authors";
        try (Connection conn = SQLiteDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Author author = new Author(
                    rs.getString("authorID"),
                    rs.getString("name"),
                    rs.getInt("bookCount")
                );
                authors.add(author);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching authors: " + e.getMessage());
        }
        return authors;
    }

    public static boolean validateLogin(String username, String password) {
        String sql = "SELECT * FROM members WHERE username = ? AND password = ?";
        try (Connection conn = SQLiteDatabase.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // User found, return user type
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error validating login: " + e.getMessage());
            //e.printStackTrace();
        }
        return false;
    }
    
    public static String getUserType(String username) {
        String sql = "SELECT userType FROM members WHERE username = ?";
        try (Connection conn = SQLiteDatabase.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("userType");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting user type: " + e.getMessage());
            //e.printStackTrace();
        }
        return null;
    }

    public static Member loginMember(String username, String password) {
        String sql = "SELECT * FROM members WHERE username = ? AND password = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Member member = new Member();
                    member.setMemberID(rs.getString("memberID"));
                    member.setName(rs.getString("name"));
                    member.setUsername(rs.getString("username"));
                    member.setEmail(rs.getString("email"));
                    member.setPhoneNumber(rs.getString("phoneNumber"));
                    member.setRegistrationDate(rs.getString("registrationDate"));
                    member.setPassword(rs.getString("password"));
                    member.setUserType(rs.getString("userType"));
                    return member;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error logging in member: " + e.getMessage());
        }
        return null;
    }

    // Insert a new member into the database
    public static void insertMember(Member member) {
        String sql = "INSERT INTO members(memberID, name, username, email, phoneNumber, registrationDate, password, userType) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String memberID = generateNextMemberID();
            member.setMemberID(memberID);
            String registrationDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            member.setRegistrationDate(registrationDate);
    
            pstmt.setString(1, memberID);
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getUsername());
            pstmt.setString(4, member.getEmail());
            pstmt.setString(5, member.getPhoneNumber());
            pstmt.setString(6, registrationDate);
            pstmt.setString(7, member.getPassword());
            pstmt.setString(8, member.getUserType());
    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting member: " + e.getMessage());
        }
    }

    public static void updateMember(Member member) {
        String sql = "UPDATE members SET name = ?, username = ?, email = ?, phoneNumber = ?, userType = ? WHERE memberID = ?";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, member.getName());
            pstmt.setString(2, member.getUsername());
            pstmt.setString(3, member.getEmail());
            pstmt.setString(4, member.getPhoneNumber());
            pstmt.setString(5, member.getUserType());
            pstmt.setString(6, member.getMemberID());
            pstmt.executeUpdate();
            System.out.println("Member updated: " + member.getName());
        } catch (SQLException e) {
            System.out.println("Error updating member: " + e.getMessage());
        }
    }

    public static Member getMemberById(String memberId) {
        String sql = "SELECT * FROM members WHERE memberID = ?";
        try (Connection conn = SQLiteDatabase.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, memberId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Member member = new Member();
                member.setMemberID(rs.getString("memberID"));
                member.setName(rs.getString("name"));
                member.setUsername(rs.getString("username"));
                member.setEmail(rs.getString("email"));
                member.setPhoneNumber(rs.getString("phoneNumber"));
                member.setUserType(rs.getString("userType"));
                member.setRegistrationDate(rs.getString("registrationDate"));
                
                return member;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching member: " + e.getMessage());
        }
        return null;
    }
    
    // Delete a member
    public static void deleteMember(String memberID) {
        String sql = "DELETE FROM members WHERE memberID = ?";
        try (Connection conn = SQLiteDatabase.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, memberID);
            pstmt.executeUpdate();
            System.out.println("Member deleted: " + memberID);
        } catch (SQLException e) {
            System.out.println("Error deleting member: " + e.getMessage());
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

    public static List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Connection conn = SQLiteDatabase.connect(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Member member = new Member(
                    rs.getString("memberID"),  
                    rs.getString("name"),      
                    rs.getString("username"),
                    rs.getString("email"),     
                    rs.getString("phoneNumber"),
                    rs.getString("registrationDate"),  
                    rs.getString("password"),
                    rs.getString("userType") 
                );
                members.add(member);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching members: " + e.getMessage());
        }
        return members;
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
        authors.add(new Author(generateNextAuthorID(), "Agatha Christie", 5));
        authors.add(new Author(generateNextAuthorID(), "Mark Haddon", 3));
        authors.add(new Author(generateNextAuthorID(), "Thomas Cormen", 3));
        authors.add(new Author(generateNextAuthorID(), "Cris Ibarra", 3));
        authors.add(new Author(generateNextAuthorID(), "Arthur Conan Doyle", 4));
        authors.add(new Author(generateNextAuthorID(), "George Orwell", 4));
        authors.add(new Author(generateNextAuthorID(), "Stephen King", 4));
        authors.add(new Author(generateNextAuthorID(), "JK Rowling", 4));
        authors.add(new Author(generateNextAuthorID(), "Harper Lee", 2));
        authors.add(new Author(generateNextAuthorID(), "Ernest Hemingway", 3));
        authors.add(new Author(generateNextAuthorID(), "Charles Dickens", 4));
        authors.add(new Author(generateNextAuthorID(), "Jane Austen", 4));
        authors.add(new Author(generateNextAuthorID(), "Leo Tolstoy", 2));
        authors.add(new Author(generateNextAuthorID(), "William Shakespeare", 3));
    
        for (Author author : authors) {
            insertAuthor(author);
        }
    }    

    // populate members
    public static void populateMembers() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(generateNextMemberID(), "Ava Chen", "avah", "ava.chen@gmail.com", "09123456789", "2023-01-01", "oh.4artepa?", "Faculty"));
        members.add(new Member(generateNextMemberID(), "Harumi Kitagawa", "miru", "harumi.kitagawa@gmail.com", "09234567890", "2023-01-01", "3lemental.pr1ncess", "Student"));
        members.add(new Member(generateNextMemberID(), "Haruma Von Brandt", "maru", "haruma.vonbrandt@gmail.com", "09345678901", "2023-01-01", "blond3.boi", "Student"));
        members.add(new Member(generateNextMemberID(), "Levi Nerissa Angeles", "levia", "levi.angeles@gmail.com", "09123456789", "2023-01-10", "ang3l!p@ss", "Student"));
        members.add(new Member(generateNextMemberID(), "Serhana Ceres Fernandez", "shera", "serhana.fernandez@gmail.com", "09234567890", "2023-02-05", "S3rh!C@res", "Faculty"));
        members.add(new Member(generateNextMemberID(), "Tammarah Xaine Molina", "tammx", "tammarah.molina@gmail.com", "09345678901", "2023-03-12", "xain3.p@ss", "Staff"));
        members.add(new Member(generateNextMemberID(), "Ivo Chiara Siason", "ivos", "ivo.siason@gmail.com", "09456789012", "2023-04-25", "Chi@ra45!", "Librarian"));
        members.add(new Member(generateNextMemberID(), "Shea Gementia Yuzon", "sheay", "shea.yuzon@gmail.com", "09567890123", "2023-05-30", "SheaGem@7", "Student"));
        members.add(new Member(generateNextMemberID(), "Marguerite Cortez", "margec", "marguerite.cortez@gmail.com", "09678901234", "2023-06-15", "Margu3!c", "Faculty"));
        members.add(new Member(generateNextMemberID(), "Florah Elina Chavez", "florahc", "florah.chavez@gmail.com", "09789012345", "2023-07-07", "Fl0r@Hchz", "Staff"));
        members.add(new Member(generateNextMemberID(), "Kerianne Abellie Salazar", "kerrys", "kerianne.salazar@gmail.com", "09890123456", "2023-08-21", "KerriA@b5", "Librarian"));
        members.add(new Member(generateNextMemberID(), "Yesania Aszelle Chua", "yesch", "yesania.chua@gmail.com", "09901234567", "2023-09-12", "Asz3!Chua", "Student"));
        members.add(new Member(generateNextMemberID(), "Shreya Fellize Kenan", "shrek", "shreya.kenan@gmail.com", "09102345678", "2023-10-05", "Shr3y@fK", "Faculty"));
        members.add(new Member(generateNextMemberID(), "Alisa Keith Demers", "alisak", "alisa.demers@gmail.com", "09203456789", "2023-11-01", "A1!is@dK", "Staff"));
        members.add(new Member(generateNextMemberID(), "Dominique Zareah Lee", "domz", "dominique.lee@gmail.com", "09304567890", "2023-11-15", "D0mzLee@5", "Librarian"));
        members.add(new Member(generateNextMemberID(), "Paige Hershell Yoon", "paigeh", "paige.yoon@gmail.com", "09405678901", "2023-12-01", "P@yoon8!", "Student"));
        members.add(new Member(generateNextMemberID(), "Keziah Nyxx Ramirez", "keznyx", "keziah.ramirez@gmail.com", "09506789012", "2024-01-10", "Nyxx8@pass", "Faculty"));
        members.add(new Member(generateNextMemberID(), "Maurice Lauren Dela Vega", "mauricev", "maurice.vega@gmail.com", "09607890123", "2024-02-05", "Laur!8DelV", "Staff"));

        for (Member member : members) {
            insertMember(member);
        }
    }
}
