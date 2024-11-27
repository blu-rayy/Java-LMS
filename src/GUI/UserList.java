package GUI;

import javax.swing.*;

public class UserList extends JFrame {
    public UserList() {
            setTitle("Book List");
            setSize(800, 600);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
            // Your implementation for the BookList window
            JLabel label = new JLabel("Displaying Book List...");
            add(label);
        }
    }

