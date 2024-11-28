package GUI;

import backend.SQLiteDatabase;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class AuthorList extends JFrame implements fontComponent {
    private JTable authorTable;
    private DefaultTableModel tableModel;

    public AuthorList() {
        initializeUI();
        loadAuthorsFromDatabase();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Author List");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
    
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createAuthorTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSearchAndActionPanel(), BorderLayout.SOUTH); // Combined panel here
    
        add(mainPanel);
    }
 

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\authorIconOrange.png"); // Replace with the path to your icon
        Image resizedTaskbarIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedTaskbarIcon));

        JLabel titleLabel = new JLabel("Author List");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createAuthorTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
    
        String[] columnNames = {"Author ID", "Author Name", "Book Count"};
    
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
    
        authorTable = new JTable(tableModel);
        authorTable.setFont(PLAIN_FONT);
        authorTable.setRowHeight(30);
        authorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        JTableHeader header = authorTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    
        JScrollPane scrollPane = new JScrollPane(authorTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
    
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }
    

    private JPanel createSearchAndActionPanel() {
        JPanel searchAndActionPanel = new JPanel(new BorderLayout());
        searchAndActionPanel.setBackground(BACKGROUND_COLOR);
    
        // Add search panel and action buttons panel to the same container
        searchAndActionPanel.add(createSearchPanel(), BorderLayout.WEST);
        searchAndActionPanel.add(createActionButtonPanel(), BorderLayout.EAST);
    
        return searchAndActionPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);
    
        JTextField searchField = new JTextField(20);
        searchField.setFont(PLAIN_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Author Name");
    
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(PLAIN_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
    
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
    
        return searchPanel;
    }
    
    private JPanel createActionButtonPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);
    
        JButton addBookBtn = createStyledButton("Add Author", e -> addNewAuthor());
        JButton editBookBtn = createStyledButton("Edit Author", e -> editSelectedAuthor());
        JButton deleteBookBtn = createStyledButton("Delete Author", e -> deleteSelectedAuthor());
        JButton refreshBtn = createStyledButton("Refresh", e -> refreshBookList());
    
        actionPanel.add(addBookBtn);
        actionPanel.add(editBookBtn);
        actionPanel.add(deleteBookBtn);
        actionPanel.add(refreshBtn);
    
        return actionPanel;
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(PLAIN_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void addNewAuthor() {
        JDialog addBookDialog = new JDialog(this, "Add New Author", true);
        addBookDialog.setSize(350, 280);
        addBookDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input fields
        dialogPanel.add(new JLabel("Author Name:"));
        JTextField authorField = new JTextField();
        dialogPanel.add(authorField);

        dialogPanel.add(new JLabel("Book Count:"));
        JTextField countField = new JTextField();
        dialogPanel.add(countField);

        // Buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Validate inputs
            if (validateauthorInput(authorField.getText(), countField.getText())) {
                Object[] newBook = {
                    "B" + (tableModel.getRowCount() + 1),
                    authorField.getText(),
                    countField.getText(),
                };
                tableModel.addRow(newBook);
                addBookDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> addBookDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to dialog
        addBookDialog.setLayout(new BorderLayout());
        addBookDialog.add(dialogPanel, BorderLayout.CENTER);
        addBookDialog.add(buttonPanel, BorderLayout.SOUTH);

        addBookDialog.setVisible(true);
    }

    private boolean validateauthorInput(String author, String count) {
        if (author.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Author Name cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (count.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Book Count cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }
    private void editSelectedAuthor() {
        int selectedRow = authorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an Author to edit", 
                "No Author Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Get current book details
        Object[] currentBookDetails = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            currentBookDetails[i] = tableModel.getValueAt(selectedRow, i);
        }

        JDialog editAuthorDialog = new JDialog(this, "Edit Author", true);
        editAuthorDialog.setSize(350, 280);
        editAuthorDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialogPanel.add(new JLabel("Author Name:"));
        JTextField authorField = new JTextField(currentBookDetails[0].toString());
        dialogPanel.add(authorField);

        dialogPanel.add(new JLabel("Book Count:"));
        JTextField countField = new JTextField(currentBookDetails[1].toString());
        dialogPanel.add(countField);

        
        // Buttons
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Validate inputs
            if (validateauthorInput(authorField.getText(), countField.getText())) {
                // Update the selected row
                tableModel.setValueAt(authorField.getText(), selectedRow, 1);
                tableModel.setValueAt(countField.getText(), selectedRow, 2);
                editAuthorDialog.dispose();
            }
        });

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> editAuthorDialog.dispose());

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    // Add components to dialog
    editAuthorDialog.setLayout(new BorderLayout());
    editAuthorDialog.add(dialogPanel, BorderLayout.CENTER);
    editAuthorDialog.add(buttonPanel, BorderLayout.SOUTH);

    editAuthorDialog.setVisible(true);
    }

    private void deleteSelectedAuthor() {
        int selectedRow = authorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select an Author to delete", 
                "No Author Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmDelete = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this author?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmDelete == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
        }
    }

    private void refreshBookList() {
        // In a real application, this would reload data from the database
        JOptionPane.showMessageDialog(this, 
            "Author list refreshed", 
            "Refresh", 
            JOptionPane.INFORMATION_MESSAGE
        );
    } 

    private void performSearch(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        authorTable.setRowSorter(sorter);

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText, 0);
        sorter.setRowFilter(filter);
    }

    private void loadAuthorsFromDatabase() {
        tableModel.setRowCount(0);
        Map<String, Integer> authorCountMap = fetchAuthorsFromDatabase();

        for (Map.Entry<String, Integer> entry : authorCountMap.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private Map<String, Integer> fetchAuthorsFromDatabase() {
        Map<String, Integer> authorCountMap = new HashMap<>();

        String query = "SELECT author, COUNT(*) AS book_count FROM books GROUP BY author";

        try (Connection connection = SQLiteDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String author = resultSet.getString("author");
                int bookCount = resultSet.getInt("book_count");
                authorCountMap.put(author, bookCount);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching authors from database: " + e.getMessage());
        }

        return authorCountMap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AuthorList().setVisible(true));
    }
}
