package backend;

public class BorrowedBook extends Book {
    private String borrowDate;
    private String dueDate;

    public BorrowedBook(String title, String author, String ISBN, String publicationDate, int availableCopies, String borrowDate, String dueDate) {
        super(title, author, ISBN, publicationDate, availableCopies);
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
