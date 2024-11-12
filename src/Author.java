import java.util.ArrayList;
import java.util.List;

public class Author {
    private String name;
    private String authorID;
    private List<Book> books;

    public Author(String name, String authorID) {
        this.name = name;
        this.authorID = authorID;
        this.books = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorID() {
        return authorID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
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
