package GUI;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;

public class DashboardGUI extends JFrame {
    public DashboardGUI(String username) {
        setTitle("ANP-LMS Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Top navigation panel
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo and menu
        JPanel logoMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoMenuPanel.setBackground(Color.WHITE);
        JLabel logoLabel = new JLabel(new ImageIcon("asdasdsad")); // Replace with your logo image path
        JLabel booksLabel = new JLabel("Books ▼");
        JLabel borrowLabel = new JLabel("Borrow");
        JLabel returnLabel = new JLabel("Return");
        
        logoMenuPanel.add(logoLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(10)); // Add space between elements
        logoMenuPanel.add(booksLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(20));
        logoMenuPanel.add(borrowLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(20));
        logoMenuPanel.add(returnLabel);

        // Search and profile icon
        JPanel searchProfilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchProfilePanel.setBackground(Color.WHITE);
        
        JTextField searchField = new JTextField(15);
        JLabel searchIcon = new JLabel("🔍"); // Placeholder for search icon
        JLabel profileIcon = new JLabel("👤"); // Placeholder for profile icon
        searchProfilePanel.add(searchField);
        searchProfilePanel.add(searchIcon);
        searchProfilePanel.add(profileIcon);

        navPanel.add(logoMenuPanel, BorderLayout.WEST);
        navPanel.add(searchProfilePanel, BorderLayout.EAST);

        // Greeting panel
        JPanel greetingPanel = new JPanel();
        greetingPanel.setBackground(Color.WHITE);
        JLabel greetingLabel = new JLabel(getGreeting() + ", " + username + "!");
        greetingLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        greetingPanel.add(greetingLabel);

        // Summary panel for Borrowed, Due, and Balance
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        summaryPanel.add(createSummaryCard("BORROWED", "8"));
        summaryPanel.add(createSummaryCard("DUE", "1"));
        summaryPanel.add(createSummaryCard("BALANCE", "0.00"));

        // Recently Added section
        JPanel recentlyAddedPanel = new JPanel();
        recentlyAddedPanel.setLayout(new BorderLayout());
        recentlyAddedPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel recentlyAddedLabel = new JLabel("Recently Added");
        recentlyAddedLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel bookPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        for (int i = 0; i < 5; i++) {
            JLabel bookIcon = new JLabel("📕", JLabel.CENTER); // Placeholder for book icon
            bookIcon.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            bookPanel.add(bookIcon);
        }
        
        recentlyAddedPanel.add(recentlyAddedLabel, BorderLayout.NORTH);
        recentlyAddedPanel.add(bookPanel, BorderLayout.CENTER);

        // Add components to the main frame
        add(navPanel, BorderLayout.NORTH);
        add(greetingPanel, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);
        add(recentlyAddedPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Helper method to create summary cards
    private JPanel createSummaryCard(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(255, 136, 0));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    // Helper method to get appropriate greeting based on the time of day
    private String getGreeting() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        if (hour < 12) {
            return "Good Morning";
        } else if (hour < 18) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardGUI("John Doe")); // Replace "John Doe" with the actual username
    }
}
