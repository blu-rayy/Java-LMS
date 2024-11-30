package backend;

import java.util.ArrayList;
import java.util.List;

public class Member extends Person implements LibraryItem {
    private final List<Book> borrowedBooks;

    public Member(String memberID, String name, String username, String email, String phoneNumber, String registrationDate, String password, String userType) {
        super(memberID, name, username, email, phoneNumber, registrationDate, password, "Member");
        this.borrowedBooks = new ArrayList<>();
    }

    @Override
    public boolean borrowBook(Book book) {
        if (book.borrowBook(book)) {
            borrowedBooks.add(book);
            return true;
        }
        return false;
    }

    @Override
    public void returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook(book);
            borrowedBooks.remove(book);
        }
    }

    public Member(){
        super();
        this.borrowedBooks = new ArrayList<>();
    }

    public String getMemberID() {
        return super.getID(); 
    }
    
    public void setMemberID(String memberID) {
        super.setID(memberID);
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
}