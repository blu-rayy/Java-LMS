public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");

        LibraryDatabase.createTables();
        LibraryDatabase.populateBooks();
        LibraryDatabase.populateAuthors();
        LibraryDatabase.populateMembers();
        
    }
}