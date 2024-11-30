package GUI;

import backend.Book;
import backend.LibraryDatabase;
import backend.SQLiteDatabase;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

public class MemberBorrowBooks extends JFrame implements fontComponent {
    private final String userName;
    private JTable availableBooksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    public MemberBorrowBooks(String userName) {
        this.userName = userName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Borrow Books");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set icon for taskbar
        ImageIcon taskbarIcon = new ImageIcon("Logos\\orangeIcons\\borrowBookIconOrange.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createBookTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSearchAndBorrowPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Borrow Books");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));

        JLabel bookCountLabel = new JLabel("Available Books: " + countAvailableBooks());
        bookCountLabel.setFont(TITLE_FONT14);
        bookCountLabel.setForeground(PRIMARY_COLOR);

        // Icon
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\borrowBookIconOrange.png");
        Image resizedIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedIcon));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(bookCountLabel, BorderLayout.EAST);

        return titlePanel;
    }

    private JPanel createBookTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        // Define column names
        String[] columnNames = {
            "ISBN", "Title", "Author", "Publication Date", "Available Copies"
        };

        // Fetch available books from the database
        ArrayList<Book> availableBooks = fetchAvailableBooksFromDatabase();

        // Convert Book objects to table data
        Object[][] data = new Object[availableBooks.size()][5];
        for (int i = 0; i < availableBooks.size(); i++) {
            Book book = availableBooks.get(i);
            data[i] = new Object[]{
                book.getISBN(), 
                book.getTitle(), 
                book.getAuthor(), 
                book.getPublicationDate(), 
                book.getAvailableCopies()
            };
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        availableBooksTable = new JTable(tableModel);
        availableBooksTable.setFont(PLAIN_FONT);
        availableBooksTable.setRowHeight(35);
        availableBooksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Customize table header
        JTableHeader header = availableBooksTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(availableBooksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createSearchAndBorrowPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);

        searchField = new JTextField(20);
        searchField.setFont(PLAIN_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Title, Author, or ISBN");

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(PLAIN_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Borrow Button Panel
        JPanel borrowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        borrowPanel.setBackground(BACKGROUND_COLOR);

        JButton borrowButton = new JButton("Borrow Selected Book");
        borrowButton.setBackground(PRIMARY_COLOR);
        borrowButton.setForeground(Color.WHITE);
        borrowButton.setFont(PLAIN_FONT);
        borrowButton.addActionListener(this::borrowSelectedBook);

        borrowPanel.add(borrowButton);

        // Combine search and borrow panels
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(borrowPanel, BorderLayout.EAST);

        return panel;
    }

    private void performSearch(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        availableBooksTable.setRowSorter(sorter);

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter = RowFilter.orFilter(
            java.util.Arrays.asList(
                RowFilter.regexFilter("(?i)" + searchText, 1), // Title column
                RowFilter.regexFilter("(?i)" + searchText, 2), // Author column
                RowFilter.regexFilter("(?i)" + searchText, 0)  // ISBN column
            )
        );

        sorter.setRowFilter(filter);
    }

    private void borrowSelectedBook(ActionEvent e) {
        int selectedRow = availableBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a book to borrow.", 
                "No Book Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convert view row index to model row index
        int modelRow = availableBooksTable.convertRowIndexToModel(selectedRow);
        String isbn = (String) tableModel.getValueAt(modelRow, 0);
        String title = (String) tableModel.getValueAt(modelRow, 1);

        try {
            // Check borrowing limit and perform borrow operation
            if (canBorrowBook(userName)) {
                if (borrowBook(userName, isbn)) {
                    JOptionPane.showMessageDialog(this, 
                        "Book '" + title + "' borrowed successfully!", 
                        "Borrow Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh the table
                    refreshBookTable();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to borrow the book. Please try again.", 
                        "Borrow Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "You have reached the maximum number of borrowed books.", 
                    "Borrow Limit Exceeded", 
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Database error: " + ex.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Book> fetchAvailableBooksFromDatabase() {
        ArrayList<Book> availableBooks = new ArrayList<>();
        String query = "SELECT ISBN, title, author, publicationDate, availableCopies " +
                       "FROM books WHERE availableCopies > 0";

        try (Connection connection = SQLiteDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publicationDate = resultSet.getString("publicationDate");
                int availableCopies = resultSet.getInt("availableCopies");

                availableBooks.add(new Book(title, author, isbn, publicationDate, availableCopies));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching available books: " + e.getMessage());
        }

        return availableBooks;
    }

    private int countAvailableBooks() {
        String query = "SELECT COUNT(*) AS availableCount FROM books WHERE availableCopies > 0";
        
        try (Connection connection = SQLiteDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                return resultSet.getInt("availableCount");
            }

        } catch (SQLException e) {
            System.out.println("Error counting available books: " + e.getMessage());
        }

        return 0;
    }

    private boolean canBorrowBook(String username) throws SQLException {
        String query = "SELECT COUNT(*) AS borrowedCount FROM transactions " +
                       "WHERE username = ? AND returned_date IS NULL";
        
        try (Connection connection = SQLiteDatabase.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("borrowedCount") < 3;
                }
            }
        }
        
        return false;
    }

    private boolean borrowBook(String username, String isbn) {
        String insertBorrowQuery = "INSERT INTO book_borrowings " +
                                   "(username, ISBN, borrow_date, due_date, returned_date) " +
                                   "VALUES (?, ?, date('now'), date('now', '+14 days'), NULL)";
        
        String updateBookQuery = "UPDATE books SET availableCopies = availableCopies - 1 " +
                                 "WHERE ISBN = ? AND availableCopies > 0";
        
        try (Connection connection = SQLiteDatabase.connect()) {
            // Begin transaction
            connection.setAutoCommit(false);
            
            try (PreparedStatement borrowStmt = connection.prepareStatement(insertBorrowQuery);
                 PreparedStatement updateStmt = connection.prepareStatement(updateBookQuery)) {
                
                // Insert borrowing record
                borrowStmt.setString(1, username);
                borrowStmt.setString(2, isbn);
                borrowStmt.executeUpdate();
                
                // Update book availability
                updateStmt.setString(1, isbn);
                updateStmt.executeUpdate();
                
                // Commit transaction
                connection.commit();
                return true;
                
            } catch (SQLException e) {
                // Rollback in case of error
                connection.rollback();
                System.out.println("Borrow book error: " + e.getMessage());
                return false;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return false;
        }
    }

    private void refreshBookTable() {
        // Fetch updated available books
        ArrayList<Book> availableBooks = fetchAvailableBooksFromDatabase();
        
        // Clear existing table data
        tableModel.setRowCount(0);
        
        // Repopulate table
        for (Book book : availableBooks) {
            tableModel.addRow(new Object[]{
                book.getISBN(), 
                book.getTitle(), 
                book.getAuthor(), 
                book.getPublicationDate(), 
                book.getAvailableCopies()
            });
        }
        
        // Update book count label
        for (ActionListener al : ((JPanel)getContentPane().getComponent(0)).getComponents()[0].getListeners(ActionListener.class)) {
            // Find the book count label
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberBorrowBooks("TestUser").setVisible(true));
    }
}