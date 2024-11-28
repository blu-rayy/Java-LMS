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
    
        JButton addAuthorBtn = createStyledButton("Add Author", e -> addNewAuthor());
        JButton editAuthorBtn = createStyledButton("Edit Author", e -> editSelectedAuthor());
        JButton deleteAuthorBtn = createStyledButton("Delete Author", e -> deleteSelectedAuthor());
        JButton refreshBtn = createStyledButton("Refresh", e -> refreshBookList());
    
        actionPanel.add(addAuthorBtn);
        actionPanel.add(editAuthorBtn);
        actionPanel.add(deleteAuthorBtn);
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
        addBookDialog.setSize(300, 180); // Adjusted size
        addBookDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridBagLayout());
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialogPanel.add(new JLabel("Author Name:"), gbc);
        gbc.gridx = 1;
        JTextField authorField = new JTextField(15); // Adjusted length
        dialogPanel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialogPanel.add(new JLabel("Book Count:"), gbc);
        gbc.gridx = 1;
        JTextField countField = new JTextField(15); // Adjusted length
        dialogPanel.add(countField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Validate inputs
            if (validateauthorInput(authorField.getText(), countField.getText())) {
            Object[] newAuthor = {
                "A" + (tableModel.getRowCount() + 1),
                authorField.getText(),
                Integer.valueOf(countField.getText())
            };
            tableModel.addRow(newAuthor);
            addBookDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> addBookDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialogPanel.add(buttonPanel, gbc);

        addBookDialog.add(dialogPanel);
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
        
            // Make sure currentBookDetails[2] is not null before using it
            String currentBookCount = (currentBookDetails[2] != null) ? currentBookDetails[2].toString() : "";
        
            JDialog editAuthorDialog = new JDialog(this, "Edit Author", true);
            editAuthorDialog.setSize(350, 200); // Adjusted size
            editAuthorDialog.setLocationRelativeTo(this);
        
            JPanel dialogPanel = new JPanel(new GridBagLayout());
            dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
        
            gbc.gridx = 0;
            gbc.gridy = 0;
            dialogPanel.add(new JLabel("Author Name:"), gbc);
            gbc.gridx = 1;
            JTextField authorField = new JTextField(currentBookDetails[1].toString(), 15); // Adjusted length
            dialogPanel.add(authorField, gbc);
        
            gbc.gridx = 0;
            gbc.gridy = 1;
            dialogPanel.add(new JLabel("Book Count:"), gbc);
            gbc.gridx = 1;
            JTextField countField = new JTextField(currentBookCount, 15); // Adjusted length
            dialogPanel.add(countField, gbc);
        
            // Buttons
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.EAST;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
        
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
        
            dialogPanel.add(buttonPanel, gbc);
        
            editAuthorDialog.add(dialogPanel);
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

        int id = 1;
        for (Map.Entry<String, Integer> entry : authorCountMap.entrySet()) {
            tableModel.addRow(new Object[]{"A" + id++, entry.getKey(), entry.getValue()});
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
