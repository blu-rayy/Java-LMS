package GUI;

import backend.SQLiteDatabase;
import backend.Book;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class BookList extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(255, 136, 0);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private JTable bookTable;
    private DefaultTableModel tableModel;

    public BookList() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Book List");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createBookTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSearchPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Book List");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createBookTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        // Define column names
        String[] columnNames = {
            "ISBN", "Title", "Author", "Publication Date", "Available Copies"
        };

        // Fetch books from the database
        ArrayList<Book> booksFromDB = fetchBooksFromDatabase();

        // Convert Book objects to table data
        Object[][] data = new Object[booksFromDB.size()][5];
        for (int i = 0; i < booksFromDB.size(); i++) {
            Book book = booksFromDB.get(i);
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

        bookTable = new JTable(tableModel);
        bookTable.setFont(TABLE_FONT);
        bookTable.setRowHeight(35);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Customize table header
        JTableHeader header = bookTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JTextField searchField = new JTextField(20);
        searchField.setFont(TABLE_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Title, Author, or ISBN");

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(TABLE_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private void performSearch(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter = RowFilter.orFilter(
            Arrays.asList(
                RowFilter.regexFilter("(?i)" + searchText, 1), // Title column
                RowFilter.regexFilter("(?i)" + searchText, 2), // Author column
                RowFilter.regexFilter("(?i)" + searchText, 0)  // ISBN column
            )
        );

        sorter.setRowFilter(filter);
    }

    private ArrayList<Book> fetchBooksFromDatabase() {
        ArrayList<Book> books = new ArrayList<>();
        String query = "SELECT ISBN, title, author, publicationDate, availableCopies FROM books";

        try (Connection connection = SQLiteDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publicationDate = resultSet.getString("publicationDate");
                int availableCopies = resultSet.getInt("availableCopies");

                books.add(new Book(title, author, isbn, publicationDate, availableCopies));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching books from database: " + e.getMessage());
        }

        return books;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookList().setVisible(true));
    }
}
