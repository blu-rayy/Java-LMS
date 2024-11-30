package GUI;

import backend.Book;
import backend.SQLiteDatabase;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.*;

public class MemberBorrowedBooks extends JFrame implements fontComponent {
    private final String userName;
    private JTable borrowedBooksTable;
    private DefaultTableModel tableModel;
    private JLabel bookCountLabel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    public MemberBorrowedBooks(String userName) {
        this.userName = userName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Borrowed Books");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set icon for taskbar
        ImageIcon taskbarIcon = new ImageIcon("Logos\\orangeIcons\\bookIconOrange.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createBookTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSearchAndActionPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Borrowed Books");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));

        bookCountLabel = new JLabel("Total Borrowed Books: " + countBorrowedBooks());
        bookCountLabel.setFont(TITLE_FONT14);
        bookCountLabel.setForeground(PRIMARY_COLOR);

        // Icon
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\bookIconOrange.png");
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
            "ISBN", "Title", "Author", "Borrow Date", "Due Date", "Status"
        };

        // Fetch borrowed books from the database
        ArrayList<Book> borrowedBooks = fetchBorrowedBooksFromDatabase();

        // Convert Book objects to table data
        Object[][] data = new Object[borrowedBooks.size()][6];
        for (int i = 0; i < borrowedBooks.size(); i++) {
            Book book = borrowedBooks.get(i);
            data[i] = new Object[]{
                book.getISBN(), 
                book.getTitle(), 
                book.getAuthor(), 
                //book.getBorrowDate(),
                //book.getDueDate(),
                //book.getStatus()
            };
        }

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        borrowedBooksTable = new JTable(tableModel);
        borrowedBooksTable.setFont(PLAIN_FONT);
        borrowedBooksTable.setRowHeight(35);
        borrowedBooksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create row sorter
        rowSorter = new TableRowSorter<>(tableModel);
        borrowedBooksTable.setRowSorter(rowSorter);

        // Customize table header
        JTableHeader header = borrowedBooksTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createSearchAndActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JTextField searchField = new JTextField(20);
        searchField.setFont(PLAIN_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Title, Author, ISBN, or Status");

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(PLAIN_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));

        // Action Panel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(BACKGROUND_COLOR);

        JButton returnButton = new JButton("Return Selected Book");
        returnButton.setBackground(PRIMARY_COLOR);
        returnButton.setForeground(Color.WHITE);
        returnButton.setFont(PLAIN_FONT);
        //returnButton.addActionListener(this::returnSelectedBook);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(PRIMARY_COLOR);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(PLAIN_FONT);
        refreshButton.addActionListener(e -> refreshBookTable());

        // Add components to panels
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        actionPanel.add(refreshButton);
        actionPanel.add(returnButton);

        // Combine search and action panels
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.EAST);

        return panel;
    }

    private void performSearch(String searchText) {
        if (searchText.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter = RowFilter.orFilter(
            Arrays.asList(
                RowFilter.regexFilter("(?i)" + searchText, 0), // ISBN
                RowFilter.regexFilter("(?i)" + searchText, 1), // Title
                RowFilter.regexFilter("(?i)" + searchText, 2), // Author
                RowFilter.regexFilter("(?i)" + searchText, 4), // Due Date
                RowFilter.regexFilter("(?i)" + searchText, 5)  // Status
            )
        );

        rowSorter.setRowFilter(filter);
    }

    private ArrayList<Book> fetchBorrowedBooksFromDatabase() {
        ArrayList<Book> availableBooks = new ArrayList<>();
        String query = "SELECT ISBN, title, author, publicationDate, availableCopies " +
                       "FROM book_borrowing WHERE availableCopies > 0";

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

    private int countBorrowedBooks() {
        // Implement the logic to count borrowed books from the database
        ArrayList<Book> borrowedBooks = fetchBorrowedBooksFromDatabase();
        return borrowedBooks.size();
    }

    private void refreshBookTable() {
        // Fetch borrowed books from the database
        ArrayList<Book> borrowedBooks = fetchBorrowedBooksFromDatabase();

        // Clear existing data
        tableModel.setRowCount(0);

        // Add new data
        for (Book book : borrowedBooks) {
            tableModel.addRow(new Object[]{
                book.getISBN(),
                book.getTitle(),
                book.getAuthor(),
                //book.getBorrowDate(),
                //book.getDueDate(),
                //book.getStatus()
            });
        }

        // Update book count label
        bookCountLabel.setText("Total Borrowed Books: " + borrowedBooks.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemberBorrowedBooks("TestUser").setVisible(true));
    }
}