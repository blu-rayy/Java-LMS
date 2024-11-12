public class Librarian extends Person {

    public Librarian(String name, String librarianID) {
        super(name, librarianID);
    }

    public void addBook(Library library, Book book) {
        library.addBook(book);
        System.out.println("Book titled '" + book.getTitle() + "' added by Librarian: " + this.getName());
    }

    public void removeBook(Library library, Book book) {
        library.removeBook(book);
        System.out.println("Book titled '" + book.getTitle() + "' removed by Librarian: " + this.getName());
    }

    public void addMember(Library library, Member member) {
        library.addMember(member);
        System.out.println("Member named '" + member.getName() + "' added by Librarian: " + this.getName());
    }

    public void removeMember(Library library, Member member) {
        library.removeMember(member);
        System.out.println("Member named '" + member.getName() + "' removed by Librarian: " + this.getName());
    }
}

//Polymorphism part is a bit uncertain as it says to override functions returnbook() and borrowbook() 
