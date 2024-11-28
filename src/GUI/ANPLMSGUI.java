package GUI;

import backend.LibraryDatabase;
import backend.Member;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;
import javax.swing.*;

public class ANPLMSGUI extends JFrame implements fontComponent {

    private JTextField usernameField;
    private JPasswordField passwordField;

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
        loginLabel.setFont(new Font("Arimo", Font.BOLD, 32)); 
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

        usernameField = new JTextField(20); // Increased width
        usernameField.setFont(TITLE_FONT18); // Larger font size
        gbc.gridy = 3;
        rightPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(TITLE_FONT18); // Larger font size
        gbc.gridy = 4;
        rightPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20); // Increased width
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
            openDashboardWindow(username, password);  // Open the dashboard with the username and password
        });

        // Add panels to frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    // Method to open a new sign-up window
    private void openSignUpWindow() {
        JFrame signUpFrame = new JFrame("Sign-Up");
        signUpFrame.setSize(600, 500);
        signUpFrame.setResizable(false);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setLocationRelativeTo(this);

        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username field with validation label
        gbc.gridx = 0;
        gbc.gridy = 0;
        signUpPanel.add(new JLabel("Username"), gbc);

        JTextField signUpUsernameField = new JTextField(15);
        signUpUsernameField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(signUpUsernameField, gbc);

        JLabel usernameErrorLabel = new JLabel("Username cannot be empty");
        usernameErrorLabel.setForeground(Color.RED);
        usernameErrorLabel.setVisible(false);
        gbc.gridy = 1;
        signUpPanel.add(usernameErrorLabel, gbc);

        // Email field with validation label
        gbc.gridx = 0;
        gbc.gridy = 2;
        signUpPanel.add(new JLabel("Email"), gbc);

        JTextField emailField = new JTextField(15);
        emailField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(emailField, gbc);

        JLabel emailErrorLabel = new JLabel("Email must end with @gmail.com");
        emailErrorLabel.setForeground(Color.RED);
        emailErrorLabel.setVisible(false);
        gbc.gridy = 3;
        signUpPanel.add(emailErrorLabel, gbc);

        // Phone Number field with validation label
        gbc.gridx = 0;
        gbc.gridy = 4;
        signUpPanel.add(new JLabel("Phone Number"), gbc);

        JTextField phoneField = new JTextField(15);
        phoneField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(phoneField, gbc);

        JLabel phoneErrorLabel = new JLabel("Phone number must be 11 digits");
        phoneErrorLabel.setForeground(Color.RED);
        phoneErrorLabel.setVisible(false);
        gbc.gridy = 5;
        signUpPanel.add(phoneErrorLabel, gbc);

        // Password field with validation label
        gbc.gridx = 0;
        gbc.gridy = 6;
        signUpPanel.add(new JLabel("Password"), gbc);

        JPasswordField signUpPasswordField = new JPasswordField(15);
        signUpPasswordField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(signUpPasswordField, gbc);

        JLabel passwordErrorLabel = new JLabel("Password must be 8+ chars, include special char and number");
        passwordErrorLabel.setForeground(Color.RED);
        passwordErrorLabel.setVisible(false);
        gbc.gridy = 7;
        signUpPanel.add(passwordErrorLabel, gbc);

        // Confirm Password field with validation label
        gbc.gridx = 0;
        gbc.gridy = 8;
        signUpPanel.add(new JLabel("Confirm Password"), gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        confirmPasswordField.setFont(PLAIN_FONT);
        gbc.gridx = 1;
        signUpPanel.add(confirmPasswordField, gbc);

        JLabel confirmPasswordErrorLabel = new JLabel("Passwords do not match");
        confirmPasswordErrorLabel.setForeground(Color.RED);
        confirmPasswordErrorLabel.setVisible(false);
        gbc.gridy = 9;
        signUpPanel.add(confirmPasswordErrorLabel, gbc);

        // Librarian Checkbox
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        JCheckBox librarianCheckBox = new JCheckBox("Are you a Librarian?");
        signUpPanel.add(librarianCheckBox, gbc);

        // Librarian Password Panel (hidden by default) - when box is ticked, it will appear for librarian verification
        JPanel librarianPasswordPanel = new JPanel(new GridBagLayout());
        GridBagConstraints libGBC = new GridBagConstraints();
        libGBC.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField librarianPasswordField = new JPasswordField(15);
        librarianPasswordField.setFont(PLAIN_FONT);
        JLabel librarianPasswordLabel = new JLabel("Verification Password");
        JLabel librarianPasswordErrorLabel = new JLabel("Incorrect librarian password");
        librarianPasswordErrorLabel.setForeground(Color.RED);
        librarianPasswordErrorLabel.setVisible(false);

        libGBC.gridx = 0;
        libGBC.gridy = 0;
        librarianPasswordPanel.add(librarianPasswordLabel, libGBC);
        libGBC.gridx = 1;
        librarianPasswordPanel.add(librarianPasswordField, libGBC);
        libGBC.gridx = 0;
        libGBC.gridy = 1;
        libGBC.gridwidth = 2;
        librarianPasswordPanel.add(librarianPasswordErrorLabel, libGBC);

        librarianPasswordPanel.setVisible(false);
        gbc.gridy = 11;
        signUpPanel.add(librarianPasswordPanel, gbc);

        // Sign-up button
        JButton signUpButton = new JButton("Sign-Up");
        signUpButton.setBackground(new Color(255, 136, 0));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arimo", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        signUpPanel.add(signUpButton, gbc);

        // Librarian Checkbox Listener
        librarianCheckBox.addActionListener(e -> {
            librarianPasswordPanel.setVisible(librarianCheckBox.isSelected());
            signUpFrame.pack();
        });

        signUpButton.addActionListener(e -> {
            // Reset error labels
            usernameErrorLabel.setVisible(false);
            emailErrorLabel.setVisible(false);
            phoneErrorLabel.setVisible(false);
            passwordErrorLabel.setVisible(false);
            confirmPasswordErrorLabel.setVisible(false);
            librarianPasswordErrorLabel.setVisible(false);

            // Validate inputs
            boolean isValid = true;
            String username = signUpUsernameField.getText().trim();
            String email = emailField.getText().trim();
            String phoneNumber = phoneField.getText().trim();
            String password = new String(signUpPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Username validation
            if (username.isEmpty()) {
                usernameErrorLabel.setVisible(true);
                isValid = false;
            }

            // Email validation
            if (!email.endsWith("@gmail.com")) {
                emailErrorLabel.setVisible(true);
                isValid = false;
            }

            // Phone number validation
            if (!phoneNumber.matches("^\\d{11}$")) {
                phoneErrorLabel.setVisible(true);
                isValid = false;
            }

            // Password validation
            if (!isValidPassword(password)) {
                passwordErrorLabel.setVisible(true);
                isValid = false;
            }

            // Confirm password validation
            if (!password.equals(confirmPassword)) {
                confirmPasswordErrorLabel.setVisible(true);
                isValid = false;
            }

            // Librarian verification
            boolean isLibrarian = librarianCheckBox.isSelected();
            if (isLibrarian) {
                String librarianPassword = new String(librarianPasswordField.getPassword());
                if (!librarianPassword.equals("adminLibrary.147")) {
                    librarianPasswordErrorLabel.setVisible(true);
                    isValid = false;
                }
            }

            // If all validations pass
            if (isValid) {
                // Create a new Member object and insert into database
                Member member = new Member();
                member.setName(username); 
                member.setUsername(username);
                member.setEmail(email);
                member.setPhoneNumber(phoneNumber);
                member.setPassword(password);
                member.setUserType(isLibrarian ? "Librarian" : "User");

                LibraryDatabase.insertMember(member);

                JOptionPane.showMessageDialog(
                    signUpFrame, 
                    "Account Created Successfully!", 
                    "Account Created", 
                    JOptionPane.INFORMATION_MESSAGE
                );

                signUpFrame.dispose();
            }

            signUpPanel.revalidate();
            signUpPanel.repaint();
        });

        signUpFrame.add(signUpPanel);
        signUpFrame.setVisible(true);
    }

    // Password validation method
    private boolean isValidPassword(String password) {
        // Check length, special character, and number
        return password.length() >= 8 && 
               Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]").matcher(password).find() &&
               Pattern.compile("\\d").matcher(password).find();
    }

    private void openDashboardWindow(String username, String password) {
        // Validate login credentials
        Member member = LibraryDatabase.loginMember(username, password);
        if (member != null) {
            // Get user type to determine dashboard
            String userType = member.getUserType();
            this.setVisible(false);

            if ("Librarian".equals(userType)) {
                new Admin_DashboardGUI(username);
            } else {
                new User_DashboardGUI(username);
            }
        } else {
            // Show error message for incorrect login
            JOptionPane.showMessageDialog(
                this, 
                "Incorrect username or password", 
                "Login Failed", 
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ANPLMSGUI loginGUI = new ANPLMSGUI();
            loginGUI.setVisible(true);
        });
    }
}