package GUI;

import backend.Author;
import backend.LibraryDatabase;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;

public class LibrarianEditAuthor extends JFrame implements fontComponent {
    private JTable authorTable;
    private DefaultTableModel tableModel;

    public LibrarianEditAuthor() {
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
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Author Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\authorIconOrange.png"); // Replace with the path to your icon
        Image resizedTaskbarIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedTaskbarIcon));
        
        JLabel CountLabel = new JLabel("Total Authors: " + LibraryDatabase.countAuthors());
        CountLabel.setFont(TITLE_FONT14);
        CountLabel.setForeground(PRIMARY_COLOR);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(CountLabel, BorderLayout.EAST);

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
        JButton refreshBtn = createStyledButton("Refresh", e -> refreshAuthorList());
    
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
        JDialog addAuthorDialog = new JDialog(this, "Add New Author", true);
        addAuthorDialog.setSize(300, 180); // Adjusted size
        addAuthorDialog.setLocationRelativeTo(this);

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
            if (validateAuthorInput(authorField.getText(), countField.getText())) {
                String authorID = LibraryDatabase.generateNextAuthorID();
                Author newAuthor = new Author(authorID, authorField.getText(), Integer.parseInt(countField.getText()));

                LibraryDatabase.insertAuthor(newAuthor);
                refreshAuthorList();
                addAuthorDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> addAuthorDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialogPanel.add(buttonPanel, gbc);

        addAuthorDialog.add(dialogPanel);
        addAuthorDialog.setVisible(true);
    }

    private boolean validateAuthorInput(String author, String count) {
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

        try {
            Integer.valueOf(count);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Book Count must be a number", 
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

        String authorId = authorTable.getValueAt(selectedRow, 0).toString();
        String currentName = authorTable.getValueAt(selectedRow, 1).toString();
        String currentBookCount = authorTable.getValueAt(selectedRow, 2).toString();

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
        JTextField authorField = new JTextField(currentName, 15); // Adjusted length
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
            if (validateAuthorInput(authorField.getText(), countField.getText())) {
                Author updatedAuthor = new Author(authorId, authorField.getText(), Integer.parseInt(countField.getText()));

                LibraryDatabase.updateAuthor(updatedAuthor);
                refreshAuthorList();
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
            String authorId = authorTable.getValueAt(selectedRow, 0).toString();
            LibraryDatabase.deleteAuthor(authorId);
            refreshAuthorList();
        }
    }

    private void refreshAuthorList() {
        loadAuthorsFromDatabase();
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

        RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText, 1);
        sorter.setRowFilter(filter);
    }

    private void loadAuthorsFromDatabase() {
        tableModel.setRowCount(0);
        List<Author> authors = LibraryDatabase.getAllAuthors();

        for (Author author : authors) {
            tableModel.addRow(new Object[]{
                author.getAuthorID(),
                author.getName(),
                author.getBookCount()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarianEditAuthor().setVisible(true));
    }
}