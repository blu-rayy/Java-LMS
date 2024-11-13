import javax.swing.*;
import java.awt.*;

public class ANPLMSGUI extends JFrame {
    public ANPLMSGUI() {
        // Set up the frame
        setTitle("ANP-LMS Login");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));
        
        // Left panel with icon and text
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(255, 136, 0));
        leftPanel.setLayout(new BorderLayout());

        // Icon and text in the center of the left panel
        JLabel iconLabel = new JLabel("ANP-LMS", JLabel.CENTER);
        iconLabel.setFont(new Font("Arial", Font.BOLD, 24));
        iconLabel.setForeground(Color.WHITE);
        ImageIcon bookIcon = new ImageIcon("path_to_your_book_icon.png"); // Add a path to your book icon image
        JLabel imageLabel = new JLabel(bookIcon, JLabel.CENTER);

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

        // Add panels to frame
        add(leftPanel);
        add(rightPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ANPLMSGUI loginGUI = new ANPLMSGUI();
            loginGUI.setVisible(true);
        });
    }
}
