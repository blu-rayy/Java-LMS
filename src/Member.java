import java.util.ArrayList;
import java.util.List;

public class Member extends Person {
    private List<Book> borrowedBooks;

    public Member(String name, String memberID) {
        super(name, memberID);
        this.borrowedBooks = new ArrayList<>();
    }

    // Method to get the list of borrowed books
    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public boolean borrowBook(Book book) {
        if (book.borrowBook()) {
            borrowedBooks.add(book);
            return true;
        }
        return false;
    }

    @Override
    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
        }
    }
}
