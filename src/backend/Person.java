package backend;
public class Person {
    private String name;
    private String id;

    public Person(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getID() {
        return id;
    }

    public boolean borrowBook(Book book) {
        return false;
    }

    public void returnBook(Book book) {
    }
}


