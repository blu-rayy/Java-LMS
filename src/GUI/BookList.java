package GUI;

import backend.Book;
import java.awt.*;
import java.awt.event.*;
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
            "ISBN", "Title", "Author", "Publication Date", "Available Copies", "Category"
        };

        // Get books from the method
        ArrayList<Book> booksManual = getManualBooks();

        // Convert Book objects to table data
        Object[][] data = new Object[booksManual.size()][6];
        for (int i = 0; i < booksManual.size(); i++) {
            Book book = booksManual.get(i);
            data[i] = new Object[]{
                book.getISBN(), 
                book.getTitle(), 
                book.getAuthor(), 
                book.getPublicationDate(), 
                book.getAvailableCopies(), 
                "Fiction" // Added a category for demonstration
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

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(TABLE_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Title, Author, or ISBN");

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(TABLE_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));

        // Filter dropdown
        String[] filterOptions = {"All", "Fiction", "Non-Fiction", "Available", "Out of Stock"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setFont(TABLE_FONT);
        filterComboBox.addActionListener(e -> filterBooks((String) filterComboBox.getSelectedItem()));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterComboBox);

        return searchPanel;
    }

    private void performSearch(String searchText) {
        // Basic search implementation
        if (searchText.trim().isEmpty()) {
            // Reset to show all rows if search is empty
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            bookTable.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);
        
        // Search across Title, Author, and ISBN columns
        RowFilter<DefaultTableModel, Object> filter = RowFilter.orFilter(
            Arrays.asList(
                RowFilter.regexFilter("(?i)" + searchText, 1), // Title column
                RowFilter.regexFilter("(?i)" + searchText, 2), // Author column
                RowFilter.regexFilter("(?i)" + searchText, 0)  // ISBN column
            )
        );
        
        sorter.setRowFilter(filter);
    }

    private void filterBooks(String filterOption) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);

        if ("All".equals(filterOption)) {
            sorter.setRowFilter(null);
            return;
        }

        // Filter based on the selected option
        RowFilter<DefaultTableModel, Object> filter;
        switch (filterOption) {
            case "Fiction":
            case "Non-Fiction":
                filter = RowFilter.regexFilter(filterOption, 5); // Category column
                break;
            case "Available":
                filter = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, 0, 4); // Available Copies column
                break;
            case "Out of Stock":
                filter = RowFilter.numberFilter(RowFilter.ComparisonType.EQUAL, 0, 4); // Available Copies column
                break;
            default:
                filter = null;
        }

        if (filter != null) {
            sorter.setRowFilter(filter);
        }
    }

    // Method to fetch books manually for demonstration
    public ArrayList<Book> getManualBooks() {
        ArrayList<Book> books = new ArrayList<>();
        
        // Create and add some Book objects manually
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", "1925-04-10", 5));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", "1960-07-11", 3));
        books.add(new Book("1984", "George Orwell", "9780451524935", "1949-06-08", 4));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "9781503290563", "1813-01-28", 2));
        books.add(new Book("Moby Dick", "Herman Melville", "9781503280786", "1851-10-18", 0));
        
        return books;
    }

    public static void main(String[] args) {
        // Invoke the GUI in the Event Dispatch Thread (EDT) to ensure thread safety
        SwingUtilities.invokeLater(() -> new BookList().setVisible(true));
    }
}