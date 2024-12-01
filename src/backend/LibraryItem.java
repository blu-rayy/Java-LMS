package backend;
public interface LibraryItem {
    boolean borrowBook(Book book);
    void returnBook(Book book);
}

