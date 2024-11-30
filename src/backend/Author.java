package backend;
import java.util.ArrayList;
import java.util.List;

public class Author {
    private String name;
    private String authorID;
    private final List<Book> books;

    // added a suppress warning to remove yellow line
    @SuppressWarnings("FieldMayBeFinal")
    private int bookCount;

    public Author(String authorID, String name, int bookCount) {
        this.authorID = authorID;
        this.name = name;
        this.bookCount = bookCount;
        this.books = new ArrayList<>();
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBookCount() {
        return bookCount;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }
}
