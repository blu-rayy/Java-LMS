package GUI;

import backend.Book;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class EditBooks extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(255, 136, 0);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private JTable bookTable;
    private DefaultTableModel tableModel;

    public EditBooks() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Book Management");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createBookTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createActionButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Book Management");
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
            "Book ID", "ISBN", "Title", "Author", "Publication Date", 
            "Available Copies", "Category", "Location", "Status"
        };

        // Sample data 
        Object[][] data = {
            {"B001", "9780743273565", "The Great Gatsby", "F. Scott Fitzgerald", "1925-04-10", 5, "Fiction", "Shelf A1", "Available"},
            {"B002", "9780061120084", "To Kill a Mockingbird", "Harper Lee", "1960-07-11", 3, "Fiction", "Shelf B2", "Available"},
            {"B003", "9780451524935", "1984", "George Orwell", "1949-06-08", 4, "Dystopian", "Shelf C3", "Available"},
            {"B004", "9781503290563", "Pride and Prejudice", "Jane Austen", "1813-01-28", 2, "Classic", "Shelf D4", "Limited Stock"},
            {"B005", "9781503280786", "Moby Dick", "Herman Melville", "1851-10-18", 0, "Adventure", "Shelf E5", "Out of Stock"}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(30);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createActionButtonPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);

        JButton addBookBtn = createStyledButton("Add Book", e -> addNewBook());
        JButton editBookBtn = createStyledButton("Edit Book", e -> editSelectedBook());
        JButton deleteBookBtn = createStyledButton("Delete Book", e -> deleteSelectedBook());
        JButton refreshBtn = createStyledButton("Refresh", e -> refreshBookList());

        actionPanel.add(addBookBtn);
        actionPanel.add(editBookBtn);
        actionPanel.add(deleteBookBtn);
        actionPanel.add(refreshBtn);

        return actionPanel;
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void addNewBook() {
        JDialog addBookDialog = new JDialog(this, "Add New Book", true);
        addBookDialog.setSize(500, 400);
        addBookDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input fields
        dialogPanel.add(new JLabel("ISBN:"));
        JTextField isbnField = new JTextField();
        dialogPanel.add(isbnField);

        dialogPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        dialogPanel.add(titleField);

        dialogPanel.add(new JLabel("Author:"));
        JTextField authorField = new JTextField();
        dialogPanel.add(authorField);

        dialogPanel.add(new JLabel("Publication Date:"));
        JTextField publicationDateField = new JTextField();
        dialogPanel.add(publicationDateField);

        dialogPanel.add(new JLabel("Available Copies:"));
        JSpinner copiesSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1000, 1));
        dialogPanel.add(copiesSpinner);

        dialogPanel.add(new JLabel("Category:"));
        String[] categories = {"Fiction", "Non-Fiction", "Classic", "Sci-Fi", "Biography"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        dialogPanel.add(categoryCombo);

        dialogPanel.add(new JLabel("Location:"));
        JTextField locationField = new JTextField();
        dialogPanel.add(locationField);

        // Buttons
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Validate inputs
            if (validateBookInput(isbnField.getText(), titleField.getText(), authorField.getText())) {
                Object[] newBook = {
                    "B" + (tableModel.getRowCount() + 1),
                    isbnField.getText(),
                    titleField.getText(),
                    authorField.getText(),
                    publicationDateField.getText(),
                    copiesSpinner.getValue(),
                    categoryCombo.getSelectedItem(),
                    locationField.getText(),
                    "Available"
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

    private boolean validateBookInput(String isbn, String title, String author) {
        if (isbn.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "ISBN cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Title cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if (author.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Author cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        return true;
    }

    private void editSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a book to edit", 
                "No Book Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Get current book details
        Object[] currentBookDetails = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            currentBookDetails[i] = tableModel.getValueAt(selectedRow, i);
        }

        JDialog editBookDialog = new JDialog(this, "Edit Book", true);
        editBookDialog.setSize(500, 400);
        editBookDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input fields with current values
        dialogPanel.add(new JLabel("Book ID:"));
        JLabel bookIdLabel = new JLabel(currentBookDetails[0].toString());
        dialogPanel.add(bookIdLabel);

        dialogPanel.add(new JLabel("ISBN:"));
        JTextField isbnField = new JTextField(currentBookDetails[1].toString());
        dialogPanel.add(isbnField);

        dialogPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField(currentBookDetails[2].toString());
        dialogPanel.add(titleField);

        dialogPanel.add(new JLabel("Author:"));
        JTextField authorField = new JTextField(currentBookDetails[3].toString());
        dialogPanel.add(authorField);

        dialogPanel.add(new JLabel("Publication Date:"));
        JTextField publicationDateField = new JTextField(currentBookDetails[4].toString());
        dialogPanel.add(publicationDateField);

        dialogPanel.add(new JLabel("Available Copies:"));
        JSpinner copiesSpinner = new JSpinner(new SpinnerNumberModel(
            Integer.parseInt(currentBookDetails[5].toString()), 
            0, 1000, 1
        ));
        dialogPanel.add(copiesSpinner);

        dialogPanel.add(new JLabel("Category:"));
        String[] categories = {"Fiction", "Non-Fiction", "Classic", "Sci-Fi", "Biography"};
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        categoryCombo.setSelectedItem(currentBookDetails[6]);
        dialogPanel.add(categoryCombo);

        dialogPanel.add(new JLabel("Location:"));
        JTextField locationField = new JTextField(currentBookDetails[7].toString());
        dialogPanel.add(locationField);

        // Buttons
        JButton saveButton = new JButton("Save Changes");
        saveButton.addActionListener(e -> {
            // Validate inputs
            if (validateBookInput(isbnField.getText(), titleField.getText(), authorField.getText())) {
                // Update the selected row
                tableModel.setValueAt(isbnField.getText(), selectedRow, 1);
                tableModel.setValueAt(titleField.getText(), selectedRow, 2);
                tableModel.setValueAt(authorField.getText(), selectedRow, 3);
                tableModel.setValueAt(publicationDateField.getText(), selectedRow, 4);
                tableModel.setValueAt(copiesSpinner.getValue(), selectedRow, 5);
                tableModel.setValueAt(categoryCombo.getSelectedItem(), selectedRow, 6);
                tableModel.setValueAt(locationField.getText(), selectedRow, 7);
                
                editBookDialog.dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> editBookDialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add components to dialog
        editBookDialog.setLayout(new BorderLayout());
        editBookDialog.add(dialogPanel, BorderLayout.CENTER);
        editBookDialog.add(buttonPanel, BorderLayout.SOUTH);

        editBookDialog.setVisible(true);
    }

    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a book to delete", 
                "No Book Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmDelete = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this book?", 
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
            "Book list refreshed", 
            "Refresh", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new EditBooks().setVisible(true);
        });
    }
}