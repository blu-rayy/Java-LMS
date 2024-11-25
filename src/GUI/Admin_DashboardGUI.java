package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Admin_DashboardGUI extends JFrame {
    private String adminName;
    
    public Admin_DashboardGUI(String username) {
        this.adminName = username;
        setupWindow();
    }
    
    private void setupWindow() {
        // Window setup
        setTitle("ANP-LMS Admin Dashboard");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // // Set icon
        // ImageIcon taskbarIcon = new ImageIcon("Logos/ANP orange copy.png");
        // Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        // setIconImage(resizedTaskbarIcon);
        
        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top navigation panel
        JPanel navPanel = createNavigationPanel();
        mainPanel.add(navPanel, BorderLayout.NORTH);
        
        // Center panel for statistics and buttons
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createNavigationPanel() {
        // ... (previous navigation panel code remains the same) ...
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setBackground(Color.WHITE);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Left side - Logo and nav items
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        leftNav.setBackground(Color.WHITE);
        
        // Logo
        ImageIcon logoIcon = new ImageIcon("Logos/ANP orange copy.png");
        Image resizedLogo = logoIcon.getImage().getScaledInstance(40, 27, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(resizedLogo));
        leftNav.add(logoLabel);
        
        // Navigation menus
        JComboBox<String> booksMenu = createNavMenu("Books", new String[]{"Title", "Author", "Genre"});
        JComboBox<String> manageUserMenu = createNavMenu("Manage User", new String[]{"Add User", "Remove User", "Edit User"});
        JButton inventoryBtn = createNavButton("Inventory");
        
        leftNav.add(booksMenu);
        leftNav.add(manageUserMenu);
        leftNav.add(inventoryBtn);
        
        // Search bar
        JTextField searchBar = new JTextField(20);
        searchBar.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 136, 0), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Right side - Search and profile
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        rightNav.setBackground(Color.WHITE);
        rightNav.add(searchBar);
        rightNav.add(createProfileButton());
        
        navPanel.add(leftNav, BorderLayout.WEST);
        navPanel.add(rightNav, BorderLayout.EAST);
        
        return navPanel;
    }
    
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Good Morning, Admin " + adminName + "!");
        welcomeLabel.setFont(new Font("Arimo", Font.BOLD, 24));
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 20, 40, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(welcomeLabel, gbc);
        
        // Statistics boxes - top row
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 10, 20);
        
        Object[][] topStats = {
            {"Users", "?", "Number of registered users", "UserStatsWindow"},
            {"Checked Out", "?", "Number of books currently checked out", "CheckedOutWindow"},
            {"Books Listed", "?", "Total number of books in system", "BooksListedWindow"},
            {"Authors Listed", "?", "Total number of authors", "AuthorsListedWindow"}
        };
        
        for (int i = 0; i < topStats.length; i++) {
            gbc.gridx = i;
            centerPanel.add(createStatBox(
                (String)topStats[i][0], 
                (String)topStats[i][1], 
                (String)topStats[i][2],
                (String)topStats[i][3]
            ), gbc);
        }
        
        // Bottom row buttons
        gbc.gridy = 2;
        Object[][] bottomButtons = {
            {"Audit Log", "AuditLogWindow"},
            {"?????", "PlaceholderWindow"},
            {"Manage Books", "ManageBooksWindow"},
            {"Manage Users", "ManageUsersWindow"}
        };
        
        for (int i = 0; i < bottomButtons.length; i++) {
            gbc.gridx = i;
            centerPanel.add(createActionButton(
                (String)bottomButtons[i][0],
                (String)bottomButtons[i][1]
            ), gbc);
        }
        
        return centerPanel;
    }
    
    private JComboBox<String> createNavMenu(String title, String[] items) {
        JComboBox<String> menu = new JComboBox<>(items);
        menu.insertItemAt(title, 0);
        menu.setSelectedIndex(0);
        menu.setBackground(Color.WHITE);
        menu.setFont(new Font("Arimo", Font.PLAIN, 14));
        return menu;
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFont(new Font("Arimo", Font.PLAIN, 14));
        return button;
    }
    
    private JButton createProfileButton() {
        JButton profileBtn = new JButton();
        profileBtn.setPreferredSize(new Dimension(40, 40));
        profileBtn.setBackground(new Color(255, 136, 0));
        profileBtn.setBorderPainted(false);
        return profileBtn;
    }
    
    private JPanel createStatBox(String title, String value, String tooltip, String windowClass) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 150));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 136, 0), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arimo", Font.BOLD, 18));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arimo", Font.BOLD, 36));
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);
        panel.setToolTipText(tooltip);
        
        // Add click functionality
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWindow(windowClass, title);
            }
            
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(255, 245, 238));
            }
            
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });
        
        return panel;
    }
    
    private JPanel createActionButton(String text, String windowClass) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 150));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(255, 136, 0), 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arimo", Font.BOLD, 18));
        label.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(label, BorderLayout.CENTER);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click functionality
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                openWindow(windowClass, text);
            }
            
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(255, 245, 238));
            }
            
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });
        
        return panel;
    }
    
    private void openWindow(String windowClass, String title) {
        JFrame newWindow = new JFrame(title);
        newWindow.setSize(800, 600);
        newWindow.setLocationRelativeTo(this);
        newWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Set icon
        ImageIcon taskbarIcon = new ImageIcon("Logos/ANP orange copy.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        newWindow.setIconImage(resizedTaskbarIcon);
        
        // Add a panel with basic content
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arimo", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Add placeholder content
        JLabel contentLabel = new JLabel("Content for " + title + " will be implemented here");
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentLabel.setFont(new Font("Arimo", Font.PLAIN, 16));
        panel.add(contentLabel, BorderLayout.CENTER);
        
        newWindow.add(panel);
        newWindow.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Admin_DashboardGUI("John");
        });
    }
}