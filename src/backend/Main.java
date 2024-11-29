package backend;
import GUI.ANPLMSGUI;

public class Main {
    public static void main(String[] args) {
        LibraryDatabase.createTables();
        LibraryDatabase.populateBooks();
        LibraryDatabase.populateAuthors();
        LibraryDatabase.populateMembers();
        
        ANPLMSGUI gui = new ANPLMSGUI();
        gui.setVisible(true);
        
    }
}