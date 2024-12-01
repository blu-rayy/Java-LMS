package GUI;

import backend.LibraryDatabase;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class LibrarianCheckedOutBooks extends JFrame implements fontComponent {
    private JTable borrowedBooksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JLabel counterLabel;

    private static final String DB_URL = "jdbc:sqlite:library.db";

    public LibrarianCheckedOutBooks() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Checked Out Books");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon taskbarIcon = new ImageIcon("Logos\\ANP orange copy.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Table Panel with Search
        JPanel tablePanel = createBorrowedBooksTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = createActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));
        
        searchField = new JTextField(20);
        searchField.setFont(PLAIN_FONT16);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            String text = searchField.getText().trim();
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\circulationIconOrange.png");
        Image resizedIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedIcon));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8)); //padding
        
        //counter label top right
        counterLabel = new JLabel();
        counterLabel.setFont(TITLE_FONT14);
        counterLabel.setForeground(PRIMARY_COLOR);

        //title panel
        JPanel titleContentPanel = new JPanel(new BorderLayout());
        titleContentPanel.setBackground(BACKGROUND_COLOR);
        titleContentPanel.add(iconLabel, BorderLayout.WEST);
        titleContentPanel.add(titleLabel, BorderLayout.CENTER);
        titleContentPanel.add(counterLabel, BorderLayout.EAST);
        titlePanel.add(titleContentPanel, BorderLayout.CENTER);
        
        return titlePanel;
    }

    private JPanel createBorrowedBooksTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
    
        // Updated column names to match the new query
        String[] columnNames = {"Transaction ID", "Title", "Member Name", "Transaction Date", "Transaction Type"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };
    
        borrowedBooksTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        borrowedBooksTable.setRowSorter(rowSorter);
    
        borrowedBooksTable.setFont(PLAIN_FONT16);
        borrowedBooksTable.setRowHeight(30);
        borrowedBooksTable.getTableHeader().setFont(TITLE_FONT14);
        borrowedBooksTable.getTableHeader().setBackground(PRIMARY_COLOR);
        borrowedBooksTable.getTableHeader().setForeground(Color.WHITE);
    
        // Load the data from the database into the table
        fetchCheckedOutBooks();
    
        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
    
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
        }
        
        private JPanel createActionPanel() {
            JPanel actionPanel = new JPanel(new BorderLayout(10, 10));
            actionPanel.setBackground(BACKGROUND_COLOR);

            JPanel searchPanel = createSearchPanel();
            actionPanel.add(searchPanel, BorderLayout.WEST);
            
            // Modify and Refresh buttons on bottom right
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            JButton modifyButton = createStyledButton("Modify Status", "Logos\\editIcon.png");
            modifyButton.setPreferredSize(new Dimension(150, 40));
            modifyButton.addActionListener(e -> modifyBookStatus());
            buttonPanel.add(modifyButton);

            JButton refreshButton = createStyledButton("Refresh", "Logos\\refreshIcon.png");
            refreshButton.setPreferredSize(new Dimension(150, 40));
            refreshButton.addActionListener(e -> refreshTable());
            buttonPanel.add(refreshButton);

            actionPanel.add(buttonPanel, BorderLayout.EAST);
            
            return actionPanel;
            }

        private JPanel createSearchPanel() {
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            searchPanel.setBackground(BACKGROUND_COLOR);
        
            JTextField localSearchField = new JTextField(20);
            localSearchField.setFont(PLAIN_FONT);
            localSearchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            localSearchField.setToolTipText("Search by Title, Author, or ISBN");
        
            JButton searchButton = new JButton("Search");
            searchButton.setBackground(PRIMARY_COLOR);
            searchButton.setForeground(Color.WHITE);
            searchButton.setFont(PLAIN_FONT);
            searchButton.addActionListener(e -> performSearch(localSearchField.getText()));
        
            searchPanel.add(new JLabel("Search:"));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);
        
            return searchPanel;
        }

        private void performSearch(String searchText) {
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            borrowedBooksTable.setRowSorter(sorter);

            if (searchText.trim().isEmpty()) {
                sorter.setRowFilter(null);
                return;
            }

            RowFilter<DefaultTableModel, Object> filter = RowFilter.orFilter(
                Arrays.asList(
                    RowFilter.regexFilter("(?i)" + searchText, 1), // Title column
                    RowFilter.regexFilter("(?i)" + searchText, 2), // Member Name column
                    RowFilter.regexFilter("(?i)" + searchText, 0)  // Transaction ID column
                )
            );

            sorter.setRowFilter(filter);
        }

        private void refreshTable() {
        fetchCheckedOutBooks();
        }

        private void fetchCheckedOutBooks() {
        String query = "SELECT transactionID, memberID, isbn, transactionType, transactionDate " +
                       "FROM transactions";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            // Clear any existing data in the table
            tableModel.setRowCount(0);
    
            // Iterate through the result set and populate the table
            while (rs.next()) {
                String transactionID = rs.getString("transactionID");
                String memberID = rs.getString("memberID");
                String isbn = rs.getString("isbn");
                String transactionType = rs.getString("transactionType");
                String transactionDate = rs.getString("transactionDate");
    
                String bookTitle = fetchBookTitle(conn, isbn);
                String memberName = fetchMemberName(conn, memberID);
    
                if (bookTitle != null && memberName != null) {
                    tableModel.addRow(new Object[]{transactionID, bookTitle, memberName, transactionDate, transactionType});
                }
            }
    
            // Update the counter label with the number of rows
            counterLabel.setText("Total Transactions: " + tableModel.getRowCount());
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching checked out books: " + e.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String fetchBookTitle(Connection conn, String isbn) throws SQLException {
        String bookQuery = "SELECT title FROM books WHERE isbn = ?";
        try (PreparedStatement bookStmt = conn.prepareStatement(bookQuery)) {
            bookStmt.setString(1, isbn);
            try (ResultSet bookRs = bookStmt.executeQuery()) {
                if (bookRs.next()) {
                    return bookRs.getString("title");
                }
            }
        }
        return null;
    }
    
    private String fetchMemberName(Connection conn, String memberID) throws SQLException {
        String memberQuery = "SELECT name FROM members WHERE memberID = ?";
        try (PreparedStatement memberStmt = conn.prepareStatement(memberQuery)) {
            memberStmt.setString(1, memberID);
            try (ResultSet memberRs = memberStmt.executeQuery()) {
                if (memberRs.next()) {
                    return memberRs.getString("name");
                }
            }
        }
        return null;
        }
        
        private void modifyBookStatus() {
            int selectedRow = borrowedBooksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                "Please select a book to modify.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            String[] statusOptions = {"Returned", "Borrow", "Overdue"};
            String currentStatus = (String) tableModel.getValueAt(selectedRow, 4);
            
            String newStatus = (String) JOptionPane.showInputDialog(
                this, 
                "Change Status:", 
                "Modify Book Status", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                statusOptions, 
                currentStatus
            );
        
            if (newStatus != null && !newStatus.equals(currentStatus)) {
                String transactionID = (String) tableModel.getValueAt(selectedRow, 0);
                updateTransactionStatus(transactionID, newStatus);
                refreshTable(); // Refresh the table to reflect changes from the database
            }
        }
        
        private void updateTransactionStatus(String transactionID, String newTransactionType) {
            try {
                // Generate a new transaction ID
                String newTransactionID = LibraryDatabase.generateNextTransactionID();
        
                // Delegate the transaction status update to LibraryDatabase
                boolean updateSuccess = LibraryDatabase.updateTransactionStatus(transactionID, newTransactionType, newTransactionID);
        
                if (updateSuccess) {
                    JOptionPane.showMessageDialog(this, 
                        "Transaction status updated successfully", 
                        "Update Successful", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to update transaction status", 
                        "Update Failed", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error updating transaction record: " + e.getMessage(),
                    "Database Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
        

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(TITLE_FONT14);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        if (iconPath != null && !iconPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
        }

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibrarianCheckedOutBooks().setVisible(true);
        });
    }
}
