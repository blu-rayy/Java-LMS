package backend;

public class Librarian extends Person {

    public Librarian(String id, String name, String username, String email, String phoneNumber, String registrationDate, String password) {
        super(id, name, username, email, phoneNumber, registrationDate, password, "Librarian"); // Set userType to "Librarian"
    }

    // Override the borrowBook method with Book parameter
    @Override
    public boolean borrowBook(Book book) {
        System.out.println("Librarian " + getName() + " is ready to assist with borrowing the book: " + book.getTitle());
        return false; // Default return value as Librarian doesn’t borrow books directly
    }

    // Override the returnBook method with Book parameter
    @Override
    public void returnBook(Book book) {
        System.out.println("Librarian " + getName() + " is ready to assist with returning the book: " + book.getTitle());
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
