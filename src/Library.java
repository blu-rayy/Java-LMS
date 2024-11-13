import java.util.ArrayList;
import java.util.List;

public class Library {
    private List<Book> books;
    private List<Member> members;

    public Library() {
        this.books = new ArrayList<>();
        this.members = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    // Add librarian's borrowing and returning functionality
    public boolean processBorrow(Librarian librarian, Member member, Book book) {
        return librarian.processBorrow(member, book);
    }

    public void processReturn(Librarian librarian, Member member, Book book) {
        librarian.processReturn(member, book);
    }
}
