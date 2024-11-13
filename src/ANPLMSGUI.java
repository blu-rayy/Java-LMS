import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ANPLMSGUI extends JFrame {
    public ANPLMSGUI() {
        // Set up the frame
        setTitle("ANP-LMS Login");
        setSize(1280, 720); // Set the desired size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
        
        // Left panel with icon and text
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(255, 136, 0));
        leftPanel.setLayout(new BorderLayout());

        // Load the image
        ImageIcon bookIcon = new ImageIcon("asdasdasdasdasdsadasdasdasd"); // Absolute path to your image file
        JLabel imageLabel = new JLabel(bookIcon, JLabel.CENTER);
        
        // Add icon and text to left panel
        JLabel iconLabel = new JLabel("ANP-LMS", JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        
        leftPanel.add(imageLabel, BorderLayout.CENTER);
        leftPanel.add(iconLabel, BorderLayout.SOUTH);

        // Right panel for login form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(168, 0, 255), 1));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Login title
        JLabel loginLabel = new JLabel("Login", JLabel.CENTER);
        loginLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(loginLabel, gbc);

        // Signup link
        JLabel signupLabel = new JLabel("Don’t have an account? ");
        signupLabel.setForeground(Color.GRAY);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        rightPanel.add(signupLabel, gbc);

        JLabel signupLink = new JLabel("Sign-up");
        signupLink.setForeground(new Color(255, 136, 0));
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
        rightPanel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridy = 3;
        rightPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        gbc.gridy = 4;
        rightPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridy = 5;
        rightPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("LOGIN");
        loginButton.setBackground(new Color(255, 136, 0));
        loginButton.setForeground(Color.WHITE);
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        rightPanel.add(loginButton, gbc);

        // Action listener for login button
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Validate username and password here (if needed)
            openDashboardWindow(username);  // Open the dashboard with the username
        });
        
        // Add panels to frame
        add(leftPanel);
        add(rightPanel);
    }

    // Method to open a new sign-up window
    private void openSignUpWindow() {
        JFrame signUpFrame = new JFrame("Sign-Up");
        signUpFrame.setSize(400, 250);
        signUpFrame.setResizable(false);
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Closes this window only
        signUpFrame.setLocationRelativeTo(this); // Center relative to main window

        // Sign-up form layout
        JPanel signUpPanel = new JPanel();
        signUpPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username field
        gbc.gridx = 0;
        gbc.gridy = 0;
        signUpPanel.add(new JLabel("Username"), gbc);

        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        signUpPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        signUpPanel.add(new JLabel("Password"), gbc);

        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        signUpPanel.add(passwordField, gbc);

        // Confirm Password field
        gbc.gridx = 0;
        gbc.gridy = 2;
        signUpPanel.add(new JLabel("Confirm Password"), gbc);

        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        signUpPanel.add(confirmPasswordField, gbc);

        // Sign-up button
        JButton signUpButton = new JButton("Sign-Up");
        signUpButton.setBackground(new Color(255, 136, 0));
        signUpButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        signUpPanel.add(signUpButton, gbc);

        // Add panel to frame and show it
        signUpFrame.add(signUpPanel);
        signUpFrame.setVisible(true);
    }

    private void openDashboardWindow(String username) {
        // Open the dashboard window after successful login
        this.setVisible(false); // Close the login window
        new DashboardGUI(username); // Pass username to the dashboard
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ANPLMSGUI loginGUI = new ANPLMSGUI();
            loginGUI.setVisible(true);
        });
    }
}
