package backend;
import GUI.ANPLMSGUI;

public class Main {
    public static void main(String[] args) {
        // only un-comment to re populate it
        /* 
         * LibraryDatabase.createTables();
         * LibraryDatabase.populateBooks();
         * LibraryDatabase.populateAuthors();
         * LibraryDatabase.populateMembers();
        */
        
        ANPLMSGUI gui = new ANPLMSGUI();
        gui.setVisible(true);
        
    }
}