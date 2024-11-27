package backend;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDatabase {
    private static final String DATABASE_URL = "jdbc:sqlite:library.db";

    // Establish a connection to the SQLite database
    public static Connection connect() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            return connection;
        } catch (SQLException e) {
            System.out.println("Connection to SQLite failed: " + e.getMessage());
            return null;
        }
    }

    // Close the database connection
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Failed to close the connection: " + e.getMessage());
        }
    }
}