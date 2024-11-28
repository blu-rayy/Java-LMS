package backend;
import GUI.ANPLMSGUI;

public class Main {
    public static void main(String[] args) {


        //remove comment if you want to populate
        /*
        LibraryDatabase.createTables();
        LibraryDatabase.populateBooks();
        LibraryDatabase.populateAuthors();
        LibraryDatabase.populateMembers();
         */

        ANPLMSGUI gui = new ANPLMSGUI();
        gui.setVisible(true);
        
    }
}