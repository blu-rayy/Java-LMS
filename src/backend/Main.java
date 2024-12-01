package backend;
import GUI.ANPLMSGUI;

public class Main {
    public static void main(String[] args) {

        // this is used for populating the database incase it is empty
        // NOTE: when creating a Librarian Account, the Verification Password is: adminLibrary.147

        LibraryDatabase.createTables();

        //LibraryDatabase.populateBooks();
        //LibraryDatabase.populateAuthors();
        //LibraryDatabase.populateMembers();
        
        ANPLMSGUI gui = new ANPLMSGUI();
        gui.setVisible(true);
        
    }
}