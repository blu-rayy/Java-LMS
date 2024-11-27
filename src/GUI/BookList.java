package GUI;

import backend.Book;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class BookList extends JFrame {

    // Constructor
    public BookList() {
        setTitle("Book List");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Get books from the database
        ArrayList<Book> booksManual = getManualBooks();

        // Create DefaultListModel and add books to it
        DefaultListModel<Book> bookListModel = new DefaultListModel<>();
        for (Book book : booksManual) {
            bookListModel.addElement(book);
        }

        // Create a JList with the DefaultListModel
        JList<Book> bookJList = new JList<>(bookListModel);

        // Customize JList rendering using BookRenderer
        bookJList.setCellRenderer(new BookRenderer());

        // Add the JList to a JScrollPane and then add the JScrollPane to the frame
        JScrollPane scrollPane = new JScrollPane(bookJList);
        add(scrollPane, BorderLayout.CENTER);

        // Make the window visible
        setVisible(true);
    }

    // Method to fetch books manually for demonstration
    public ArrayList<Book> getManualBooks() {
        ArrayList<Book> books = new ArrayList<>();
        
        // Create and add some Book objects manually
        books.add(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", "1925-04-10", 5));
        books.add(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", "1960-07-11", 3));
        books.add(new Book("1984", "George Orwell", "9780451524935", "1949-06-08", 4));
        books.add(new Book("Pride and Prejudice", "Jane Austen", "9781503290563", "1813-01-28", 2));
        books.add(new Book("Moby Dick", "Herman Melville", "9781503280786", "1851-10-18", 6));
        
        return books;
    }

    // Custom renderer for Book
    class BookRenderer extends JPanel implements ListCellRenderer<Book> {
        private JLabel titleLabel = new JLabel();
        private JLabel authorLabel = new JLabel();
        private JLabel isbnLabel = new JLabel();
        private JLabel publicationDateLabel = new JLabel();
        private JLabel availableCopiesLabel = new JLabel();

        @Override
        public Component getListCellRendererComponent(JList<? extends Book> list, Book value, int index, boolean isSelected, boolean cellHasFocus) {
            // Set labels with book information
            isbnLabel.setText("ISBN: " + value.getISBN());
            titleLabel.setText("Title: " + value.getTitle());
            authorLabel.setText("Author: " + value.getAuthor());
            publicationDateLabel.setText("Published: " + value.getPublicationDate());
            availableCopiesLabel.setText("Available Copies: " + value.getAvailableCopies());

            // Set layout and style for each book
            setLayout(new GridLayout(1, 5));
            setBackground(list.getBackground()); // Transparent background for individual book rows

            // Add book labels to panel in the order: ISBN, Title, Author, Published Date, Available Copies
            add(isbnLabel);
            add(titleLabel);
            add(authorLabel);
            add(publicationDateLabel);
            add(availableCopiesLabel);

            // Set thin black border for each book row
            setBorder(new LineBorder(Color.BLACK, 1));

            // If item is selected, change background color
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }

    // Header row component (ISBN, TITLE, AUTHOR, PUBLISHED, AVAILABLE COPIES)
    class HeaderPanel extends JPanel {
        JLabel isbnLabel = new JLabel("ISBN");
        JLabel titleLabel = new JLabel("TITLE");
        JLabel authorLabel = new JLabel("AUTHOR");
        JLabel publicationDateLabel = new JLabel("PUBLISHED");
        JLabel availableCopiesLabel = new JLabel("AVAILABLE COPIES");

        public HeaderPanel() {
            // Set layout for header row
            setLayout(new GridLayout(1, 5));

            // Set the background color to orange
            setBackground(new Color(255, 136, 0));

            // Add all labels to the panel
            add(isbnLabel);
            add(titleLabel);
            add(authorLabel);
            add(publicationDateLabel);
            add(availableCopiesLabel);
        }
    }

    // Main method to test the BookList window
    public static void main(String[] args) {
        // Invoke the GUI in the Event Dispatch Thread (EDT) to ensure thread safety
        SwingUtilities.invokeLater(() -> new BookList());
    }
}
