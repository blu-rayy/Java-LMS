import java.util.ArrayList;
import java.util.List;

public class Member extends Person {
    private List<Book> borrowedBooks;

    public Member(String name, String memberID) {
        super(name, memberID);
        this.borrowedBooks = new ArrayList<>();
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    @Override
    public boolean borrowBook(Book book) {
        if (book.borrowBook()) {
            borrowedBooks.add(book);
            return true;
        }
        return false; // Return false if the book is not available
    }

    @Override
    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
        }
    }
}
