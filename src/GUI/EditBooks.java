package GUI;

import backend.Book;
import backend.LibraryDatabase;
import backend.SQLiteDatabase;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class EditBooks extends JFrame implements fontComponent {
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public EditBooks() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Book Management");
        setSize(1200, 700);
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

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\managebookIconOrange.png");
        Image resizedTaskbarIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedTaskbarIcon));

        JLabel titleLabel = new JLabel("Book Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));

        titlePanel.add(iconLabel);
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
        bookTable.setFont(PLAIN_FONT);
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
        button.setFont(PLAIN_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void addNewBook() {
        JDialog addBookDialog = new JDialog(this, "Add New Book", true);
        addBookDialog.setSize(350, 280);
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
                };
                tableModel.addRow(newBook);
                addBookDialog.dispose();
            }
        });

        // Insert the new book into the database
        Book newBook = new Book(
            isbnField.getText(),
            titleField.getText(),
            authorField.getText(),
            publicationDateField.getText(),
            (int) copiesSpinner.getValue()
        );
        LibraryDatabase.insertBook(newBook);

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
        editBookDialog.setSize(350, 280);
        editBookDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialogPanel.add(new JLabel("ISBN:"));
        JTextField isbnField = new JTextField(currentBookDetails[0].toString());
        dialogPanel.add(isbnField);

        dialogPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField(currentBookDetails[1].toString());
        dialogPanel.add(titleField);

        dialogPanel.add(new JLabel("Author:"));
        JTextField authorField = new JTextField(currentBookDetails[2].toString());
        dialogPanel.add(authorField);

        dialogPanel.add(new JLabel("Publication Date:"));
        JTextField publicationDateField = new JTextField(currentBookDetails[3].toString());
        dialogPanel.add(publicationDateField);

        dialogPanel.add(new JLabel("Available Copies:"));
        JSpinner copiesSpinner = new JSpinner(new SpinnerNumberModel(
            Integer.parseInt(currentBookDetails[4].toString()), 
            0, 1000, 1
        ));
        dialogPanel.add(copiesSpinner);

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
        SwingUtilities.invokeLater(() -> {
            new EditBooks().setVisible(true);
        });
    }
}