package GUI; 
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import javax.swing.*;

public class User_DashboardGUI extends JFrame {
    public User_DashboardGUI(String username) {
        // sets taskbar
        ImageIcon taskbarIcon = new ImageIcon("Logos/ANP orange copy.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        
        setTitle("ANP-LMS Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(242, 240, 239)); // Light gray background

        // Top navigation panel
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo and menu
        JPanel logoMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoMenuPanel.setBackground(Color.WHITE);
        JLabel logoLabel = new JLabel(new ImageIcon("asdasdsad")); // Replace with your logo image path
        JLabel booksLabel = new JLabel("Books");
        JLabel borrowLabel = new JLabel("Borrow");
        JLabel returnLabel = new JLabel("Return");
        
        // Make the labels clickable by adding MouseListeners
        booksLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor on hover
        borrowLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        returnLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        booksLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle "Books" click
                JOptionPane.showMessageDialog(null, "Books clicked!");
            }
        });

        borrowLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle "Borrow" click
                JOptionPane.showMessageDialog(null, "Borrow clicked!");
            }
        });

        returnLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Handle "Return" click
                JOptionPane.showMessageDialog(null, "Return clicked!");
            }
        });
        
        logoMenuPanel.add(logoLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(10)); // Add space between elements
        logoMenuPanel.add(booksLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(20));
        logoMenuPanel.add(borrowLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(20));
        logoMenuPanel.add(returnLabel);
        logoMenuPanel.add(Box.createHorizontalStrut(20));

        // Search and profile icon
        JPanel searchProfilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchProfilePanel.setBackground(Color.WHITE);
        
        JTextField searchField = new JTextField(15);
        searchProfilePanel.add(searchField);

        navPanel.add(logoMenuPanel, BorderLayout.WEST);
        navPanel.add(searchProfilePanel, BorderLayout.EAST);

        // Greeting panel
        JPanel greetingPanel = new JPanel();
        greetingPanel.setBackground(new Color(242, 240, 239));
        JLabel greetingLabel = new JLabel(getGreeting() + ", " + username + "!");
        greetingLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        greetingPanel.add(greetingLabel);

        // Summary panel for Borrowed, Due, and Balance with padding
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 10));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add left and right padding to each summary card
        JPanel borrowedPanel = createSummaryCard("BORROWED", "8");
        JPanel duePanel = createSummaryCard("DUE", "1");
        JPanel balancePanel = createSummaryCard("BALANCE", "0.00");
        
        // Wrap each panel in a container with left-right padding
        JPanel borrowedContainer = new JPanel();
        borrowedContainer.setLayout(new BorderLayout());
        borrowedContainer.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20)); // Left and right padding
        borrowedContainer.add(borrowedPanel, BorderLayout.CENTER);

        JPanel dueContainer = new JPanel();
        dueContainer.setLayout(new BorderLayout());
        dueContainer.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20)); // Left and right padding
        dueContainer.add(duePanel, BorderLayout.CENTER);

        JPanel balanceContainer = new JPanel();
        balanceContainer.setLayout(new BorderLayout());
        balanceContainer.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20)); // Left and right padding
        balanceContainer.add(balancePanel, BorderLayout.CENTER);

        summaryPanel.add(borrowedContainer);
        summaryPanel.add(dueContainer);
        summaryPanel.add(balanceContainer);

        // Recently Added section
        JPanel recentlyAddedPanel = new JPanel();
        recentlyAddedPanel.setLayout(new BorderLayout());
        recentlyAddedPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JLabel recentlyAddedLabel = new JLabel("Recently Added");
        recentlyAddedLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel bookPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        for (int i = 0; i < 5; i++) {
            JLabel bookIcon = new JLabel("📕", JLabel.CENTER); // Placeholder for book icon
            bookIcon.setBorder(BorderFactory.createLineBorder(new Color(255, 136, 0)));
            bookPanel.add(bookIcon);
        }
        
        recentlyAddedPanel.add(recentlyAddedLabel, BorderLayout.NORTH);
        recentlyAddedPanel.add(bookPanel, BorderLayout.CENTER);

        // Create a central panel to hold both the summary and recently added sections
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Vertical layout to stack components
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the entire content
        
        // Add summary panel and recently added panel to the center panel
        centerPanel.add(summaryPanel);
        centerPanel.add(Box.createVerticalStrut(20)); // Add space between the panels
        centerPanel.add(recentlyAddedPanel);

        // Create a main container for the greeting, centered content, and space
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add padding above greeting and centered content to the main panel
        mainPanel.add(Box.createVerticalStrut(30)); // Padding above greeting
        mainPanel.add(greetingPanel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add space between greeting and content
        mainPanel.add(centerPanel);

        // Add the panels to the main frame
        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER); // Add mainPanel to center position

        setVisible(true);
    }

    // Helper method to create summary cards
    private JPanel createSummaryCard(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(255, 136, 0), 2));

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
        SwingUtilities.invokeLater(() -> new User_DashboardGUI("John Doe")); // Replace "John Doe" with the actual username
    }
}
