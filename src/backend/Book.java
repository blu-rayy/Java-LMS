package backend;
public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String publicationDate;
    private int availableCopies;

    public Book(String title, String author, String ISBN, String publicationDate, int availableCopies) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.availableCopies = availableCopies;
}

public String getTitle() {
    return title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getAuthor() {
    return author;
}

public void setAuthor(String author) {
    this.author = author;
}

public String getISBN() {
    return ISBN;
}

public void setISBN(String ISBN) {
    this.ISBN = ISBN;
}

public String getPublicationDate() {
    return publicationDate;
}

public void setPublicationDate(String publicationDate) {
    this.publicationDate = publicationDate;
}

public int getAvailableCopies() {
    return availableCopies;
}

public void setAvailableCopies(int availableCopies) {
    this.availableCopies = availableCopies;
}

public boolean borrowBook() {
    if (availableCopies > 0) {
        availableCopies--;
        return true;
    }
    return false;
}

public void returnBook() {
    availableCopies++;
}

@Override
public String toString() {
    return "Title: " + title + ", Author: " + author + ", ISBN: " + ISBN + 
           ", Publication Date: " + publicationDate + ", In Stock: " + availableCopies;
}
}
