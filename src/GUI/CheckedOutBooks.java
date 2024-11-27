package GUI;

import javax.swing.*;

public class CheckedOutBooks extends JFrame implements fontComponent {
    public CheckedOutBooks() {
            setTitle("Book List");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            // Your implementation for the BookList window
            JLabel label = new JLabel("Displaying Checked Out Books...");
            add(label);
        }
    }

