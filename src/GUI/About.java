package GUI;

import java.awt.*;
import javax.swing.*;

public class About {
    // About JFrame
    public static class AboutPage extends JFrame implements fontComponent {
        public AboutPage() {
            // Taskbar Icon
            ImageIcon taskbarIcon = new ImageIcon("Logos\\ANP orange copy.png");
            Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
            setIconImage(resizedTaskbarIcon);

            // Frame settings
            setTitle("About the System");
            setSize(500, 320);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);

            // Main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(BACKGROUND_COLOR);

            // Title
            JLabel titleLabel = new JLabel("The ANP-LMS v.1.0.0");
            titleLabel.setFont(TITLE_FONT18);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(titleLabel);

            // Text description
            JLabel aboutLabel = new JLabel("<html><div style='text-align: center;'>"
                    + "The Access Navigation Platform-Library Management System (ANP-LMS) is designed to streamline"
                    + "library operations, manage book collections, and improve access to resources for students and staff.<br><br>"
                    + "This application is a project made in fulfillment of CCS0023L by Mr. Reginald Cheng. "
                    + "Any content seen in the application is purely for educational purposes only."
                    + "</div></html>");
            aboutLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            aboutLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            mainPanel.add(aboutLabel);

            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            closeButton.setBackground(PRIMARY_COLOR);
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(PLAIN_FONT);
            closeButton.addActionListener(e -> dispose());
            mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            mainPanel.add(closeButton);

            add(mainPanel);
        }
    }

    // Developers List JFrame
    public static class DevelopersList extends JFrame implements fontComponent {
        public DevelopersList() {
            // Taskbar Icon
            ImageIcon taskbarIcon = new ImageIcon("Logos\\profileIcon.png");
            Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            setIconImage(resizedTaskbarIcon);

            // Frame settings
            setTitle("Developers");
            setSize(500, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);

            // Main panel
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridLayout(4, 1, 10, 10));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(BACKGROUND_COLOR);

            // Developer details
            String[][] developers = {
                    {"Kristian Bautista", "UI/UX Designer, Frontend Developer"},
                    {"Angel Letada", "Database Specialist, Backend Developer"},
                    {"John Janiel Obmerga", "UI/UX Designer, Frontend Developer"},
                    {"Marianne Santos", "Database Specialist, Backend Developer"}
            };

            for (String[] dev : developers) {
                JPanel devPanel = new JPanel(new BorderLayout());
                devPanel.setBackground(BACKGROUND_COLOR);
                devPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

                // Left: Image
                JLabel imageLabel = new JLabel();
                imageLabel.setIcon(getDeveloperIcon());
                imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                devPanel.add(imageLabel, BorderLayout.WEST);

                // Right: Name and Role
                JPanel textPanel = new JPanel();
                textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
                textPanel.setBackground(BACKGROUND_COLOR);

                JLabel nameLabel = new JLabel(dev[0]);
                nameLabel.setFont(PLAIN_FONT);
                JLabel roleLabel = new JLabel(dev[1]);
                roleLabel.setFont(TITLE_FONT12);

                textPanel.add(nameLabel);
                textPanel.add(roleLabel);
                devPanel.add(textPanel, BorderLayout.CENTER);

                mainPanel.add(devPanel);
            }

            add(mainPanel);
        }

        private ImageIcon getDeveloperIcon() {
            ImageIcon icon = new ImageIcon("Logos\\profileIcon.png");
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AboutPage().setVisible(true);
            new DevelopersList().setVisible(true);
        });
    }
}
