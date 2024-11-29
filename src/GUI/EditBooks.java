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
import java.util.List;

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
                // Insert the new book into the database
                Book newBook = new Book(
                    titleField.getText(),               // Correctly map Title
                    authorField.getText(),              // Correctly map Author
                    isbnField.getText(),                // Correctly map ISBN
                    publicationDateField.getText(),     // Correctly map Publication Date
                    (int) copiesSpinner.getValue()      // Correctly map Available Copies
                );
    
                LibraryDatabase.insertBook(newBook);
    
                // Update the table model to reflect the new book
                Object[] row = {
                    isbnField.getText(),
                    titleField.getText(),
                    authorField.getText(),
                    publicationDateField.getText(),
                    copiesSpinner.getValue()
                };
                tableModel.addRow(row);
    
                addBookDialog.dispose();

                JOptionPane.showMessageDialog(this, 
                "Book added successfully", 
                "Add Book", 
                JOptionPane.INFORMATION_MESSAGE
            );
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
            // Log values before updating
            System.out.println("ISBN to update: " + isbnField.getText());
            System.out.println("Title to update: " + titleField.getText());
            System.out.println("Author to update: " + authorField.getText());
            System.out.println("Publication Date to update: " + publicationDateField.getText());
            System.out.println("Available Copies to update: " + copiesSpinner.getValue());
    
            // Validate inputs
            if (validateBookInput(isbnField.getText(), titleField.getText(), authorField.getText())) {
                // Create a Book object with updated values
                Book updatedBook = new Book(
                    titleField.getText(),              // title
                    authorField.getText(),             // author
                    isbnField.getText(),               // ISBN
                    publicationDateField.getText(),    // publicationDate
                    (int) copiesSpinner.getValue()     // availableCopies
                );
                
                // Log the details of the book being updated
                System.out.println("Updating book with ISBN: '" + updatedBook.getISBN() + "'");
                System.out.println("Title to update: " + updatedBook.getTitle());
                System.out.println("Author to update: " + updatedBook.getAuthor());
                System.out.println("Publication Date to update: " + updatedBook.getPublicationDate());
                System.out.println("Available Copies to update: " + updatedBook.getAvailableCopies());
        
                // Call the method to update the book in the database
                LibraryDatabase.updateBook(updatedBook);
        
                // Close the edit dialog
                editBookDialog.dispose();

                JOptionPane.showMessageDialog(this, 
                "Book details updated successfully", 
                "Update Book Details", 
                JOptionPane.INFORMATION_MESSAGE
                );
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
    
        // Get the ISBN of the selected book
        String isbnToDelete = bookTable.getValueAt(selectedRow, 0).toString(); // Assuming ISBN is in column 0
    
        int confirmDelete = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this book?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );
    
        if (confirmDelete == JOptionPane.YES_OPTION) {
            // Delete the book from the database
            try {
                long isbn = Long.parseLong(isbnToDelete); // Parse ISBN to long if needed
                LibraryDatabase.deleteBook(isbn); // Call to delete from DB
    
                // Remove the book from the table model
                tableModel.removeRow(selectedRow);
    
                // Show success message
                JOptionPane.showMessageDialog(this, 
                    "Book deleted successfully", 
                    "Delete Book", 
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid ISBN format", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void refreshBookList() {
        // Clear the current table data
        tableModel.setRowCount(0);
        
        // Get the updated list of books from the database
        try {
            List<Book> books = LibraryDatabase.getAllBooks(); // Assuming you have a method to fetch all books
            
            // Add books to the table model
            for (Book book : books) {
                Object[] rowData = new Object[5];
                rowData[0] = book.getISBN();
                rowData[1] = book.getTitle();
                rowData[2] = book.getAuthor();
                rowData[3] = book.getPublicationDate();
                rowData[4] = book.getAvailableCopies();
                tableModel.addRow(rowData);
            }
            
            // Show confirmation message
            JOptionPane.showMessageDialog(this, 
                "Book list refreshed successfully", 
                "Refresh", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error refreshing book list: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE
            );
        }
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