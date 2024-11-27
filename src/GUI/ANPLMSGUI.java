package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ANPLMSGUI extends JFrame implements fontComponent {

    public ANPLMSGUI() {
        // sets taskbar icon
        ImageIcon taskbarIcon = new ImageIcon("Logos\\ANP orange copy.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);

        // Set up the frame
        setTitle("ANP-LMS Login");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout()); // Change to BorderLayout for control over panel width

        // Left panel with icon and text
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(PRIMARY_COLOR);
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(800, 720)); // Set custom width for the left panel

        // Load the image
        ImageIcon bookIcon = new ImageIcon("Logos\\ANP white copy.png");
        Image resizingBookIcon = bookIcon.getImage().getScaledInstance(250, 171, Image.SCALE_SMOOTH);
        ImageIcon resizedBookIcon = new ImageIcon(resizingBookIcon);
        JLabel imageLabel = new JLabel(resizedBookIcon, JLabel.CENTER);
        leftPanel.add(imageLabel, BorderLayout.CENTER);

        // Right panel for login form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(null);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);

        // Login title
        JLabel loginLabel = new JLabel("Login", JLabel.LEFT);
        loginLabel.setFont(new Font("Arimo", Font.BOLD, 32)); // Adjusted font size
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(loginLabel, gbc);

        // Signup link
        JLabel signupLabel = new JLabel("Don’t have an account? ");
        signupLabel.setForeground(Color.GRAY);
        signupLabel.setFont(new Font("Arimo", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        
        rightPanel.add(signupLabel, gbc);

        JLabel signupLink = new JLabel("Sign-up");
        signupLink.setForeground(PRIMARY_COLOR);
        signupLink.setFont(new Font("Arimo", Font.ITALIC, 16));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openSignUpWindow(); // Call method to open the sign-up window
            }
        });
        gbc.gridx = 1;
        rightPanel.add(signupLink, gbc);

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(TITLE_FONT18); // Larger font size
        rightPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(20); // Increased width
        usernameField.setFont(TITLE_FONT18); // Larger font size
        gbc.gridy = 3;
        rightPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(TITLE_FONT18); // Larger font size
        gbc.gridy = 4;
        rightPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(20); // Increased width
        passwordField.setFont(TITLE_FONT18); // Larger font size
        gbc.gridy = 5;
        rightPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(255, 136, 0));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arimo", Font.BOLD, 20)); // Larger font size for the button
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 0, 0, 0); // Add top padding between the password field and the button

        rightPanel.add(loginButton, gbc);

        // Action listener for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Validate username and password here (if needed)
            openDashboardWindow(username);  // Open the dashboard with the username
        });

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    // Method to open a new sign-up window
    private void openSignUpWindow() {
        JFrame signUpFrame = new JFrame("Sign-Up");
        signUpFrame.setSize(600, 400);
        signUpFrame.setResizable(false);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setLocationRelativeTo(this);
    
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
    
        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        signUpPanel.add(new JLabel("Username"), gbc);
    
        JTextField usernameField = new JTextField(15);
        usernameField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(usernameField, gbc);
    
        // Email field
        gbc.gridx = 0;
        gbc.gridy = 1;
        signUpPanel.add(new JLabel("Email"), gbc);
    
        JTextField emailField = new JTextField(15);
        emailField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(emailField, gbc);
    
        // Phone Number field
        gbc.gridx = 0;
        gbc.gridy = 2;
        signUpPanel.add(new JLabel("Phone Number"), gbc);
    
        JTextField phoneField = new JTextField(15);
        phoneField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(phoneField, gbc);
    
        // Password field
        gbc.gridx = 0;
        gbc.gridy = 3;
        signUpPanel.add(new JLabel("Password"), gbc);
    
        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(passwordField, gbc);
    
        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 4;
        signUpPanel.add(new JLabel("Confirm Password"), gbc);
    
        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(confirmPasswordField, gbc);
    
        // User Type Checkbox
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JCheckBox librarianCheckBox = new JCheckBox("Are you a Librarian?");
        signUpPanel.add(librarianCheckBox, gbc);
    
        // Sign-up button
        JButton signUpButton = new JButton("Sign-Up");
        signUpButton.setBackground(new Color(255, 136, 0));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arimo", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 6;
        signUpPanel.add(signUpButton, gbc);
    
        // Password mismatch label
        JLabel passwordMismatchLabel = new JLabel("*Passwords do not match!");
        passwordMismatchLabel.setForeground(Color.RED);
        passwordMismatchLabel.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        signUpPanel.add(passwordMismatchLabel, gbc);
    
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneField.getText();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            boolean isLibrarian = librarianCheckBox.isSelected();
    
            if (password.equals(confirmPassword)) {
                passwordMismatchLabel.setVisible(false);
    
                String userType = isLibrarian ? "Librarian" : "User";  
                JOptionPane.showMessageDialog(
                    signUpFrame, 
                    "Account Created Successfully!", 
                    "Account Created", 
                    JOptionPane.INFORMATION_MESSAGE
                );
    
                signUpFrame.dispose();
            } else {
                passwordMismatchLabel.setVisible(true);
                // Reset the text fields to their original size
                usernameField.setColumns(15);
                emailField.setColumns(15);
                phoneField.setColumns(15);
                passwordField.setColumns(15);
                confirmPasswordField.setColumns(15);
            }
    
            signUpPanel.revalidate();
            signUpPanel.repaint();
        });
    
        signUpFrame.add(signUpPanel);
        signUpFrame.setVisible(true);
    }
    
    private void openDashboardWindow(String username) {
        this.setVisible(false);
        new Admin_DashboardGUI(username);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ANPLMSGUI loginGUI = new ANPLMSGUI();
            loginGUI.setVisible(true);
        });

        
    }
}