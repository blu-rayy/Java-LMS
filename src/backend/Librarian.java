package backend;

public class Librarian extends Person implements LibraryItem {

    public Librarian(String id, String name, String username, String email, String phoneNumber, String registrationDate, String password) {
        super(id, name, username, email, phoneNumber, registrationDate, password, "Librarian"); // Set userType to "Librarian"
    }

    // performs a different behavior compared to Member.java 
    @Override
    public boolean borrowBook(Book book) {

        System.out.println("The Librarian does not need to borrow a book directly!");
                return false;
    }

    @Override
    public void returnBook(Book book) {
        System.out.println("The Librarian does not need to return a book directly!");
    }

    // Method to process borrowing on behalf of a specific member
    public boolean processBorrow(Member member, Book book) {
        if (member.borrowBook(book)) {
            System.out.println("Librarian " + getName() + " processed borrowing of '" + book.getTitle() + "' for member " + member.getName());
            return true;
        } else {
            System.out.println("Librarian " + getName() + ": No available copies of '" + book.getTitle() + "' for member " + member.getName());
            return false;
        }
    }

    // Method to process returning on behalf of a specific member
    public void processReturn(Member member, Book book) {
        if (member.getBorrowedBooks().contains(book)) {
            member.returnBook(book);
            System.out.println("Librarian " + getName() + " processed returning of '" + book.getTitle() + "' for member " + member.getName());
        } else {
            System.out.println("Librarian " + getName() + ": Member " + member.getName() + " does not have the book '" + book.getTitle() + "' to return.");
        }
    }
}