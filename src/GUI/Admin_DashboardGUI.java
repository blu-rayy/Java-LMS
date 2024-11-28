package GUI;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import javax.swing.*;

public class Admin_DashboardGUI extends JFrame implements fontComponent {
    private final String adminName;

    public Admin_DashboardGUI(String adminName) {
        this.adminName = adminName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Admin Dashboard");
        setSize(1366, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //icon for taskbar and yung sa top left corner
        ImageIcon taskbarIcon = new ImageIcon("Logos\\ANP orange copy.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTopNavigationPanel(), BorderLayout.NORTH);
        mainPanel.add(createDashboardContentPanel(), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTopNavigationPanel() {
        JPanel navPanel = new JPanel(new BorderLayout(10, 0));
        navPanel.setBackground(BACKGROUND_COLOR);

        // Left Navigation Buttons
        JPanel leftNav = createLeftNavigationButtons();

        // Search and Profile Area
        JPanel rightNav = createRightNavigationPanel();

        navPanel.add(leftNav, BorderLayout.WEST);
        navPanel.add(rightNav, BorderLayout.EAST);

        return navPanel;
    }

    private JPanel createLeftNavigationButtons() {
        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftNav.setBackground(BACKGROUND_COLOR);

        String[] navButtons = {"Books", "Users", "Inventory"};

        for (String buttonText : navButtons) {
            leftNav.add(createNavButton(buttonText));
        }

        return leftNav;
        }

        private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(BACKGROUND_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> openFeatureWindow(text));
        return button;
        }

        private JPanel createRightNavigationPanel() {
            JPanel rightNav = new JPanel();
            rightNav.setLayout(new BoxLayout(rightNav, BoxLayout.X_AXIS));
            rightNav.setBackground(BACKGROUND_COLOR);
        
            // Time section (closer together and lower)
            JPanel timePanel = new JPanel();
            timePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // Added vertical padding
            timePanel.setBackground(BACKGROUND_COLOR);
            
            ImageIcon timeIcon = new ImageIcon("Logos\\clockIcon.png"); 
            Image scaledTimeIcon = timeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            JLabel timeIconLabel = new JLabel(new ImageIcon(scaledTimeIcon));
            timeIconLabel.setVerticalAlignment(JLabel.BOTTOM); // Align to bottom
            
            JLabel timeLabel = new JLabel();
            timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            timeLabel.setForeground(PRIMARY_COLOR);
            timeLabel.setVerticalAlignment(JLabel.BOTTOM); // Align to bottom
            updateTime(timeLabel);
            
            timePanel.add(timeIconLabel);
            timePanel.add(timeLabel);
        
            // Greeting
            JLabel greetingLabel = new JLabel();
            greetingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            greetingLabel.setForeground(PRIMARY_COLOR);
            updateGreeting(greetingLabel);
        
            // Timer
            Timer timer = new Timer(1000, e -> {
                updateTime(timeLabel);
                updateGreeting(greetingLabel);
            });
            timer.start();
        
            // Profile button
            JLabel profileButton = createProfileButton();
        
            // Add components with rigid areas for spacing
            rightNav.add(timePanel);
            rightNav.add(Box.createRigidArea(new Dimension(30, 0))); // Spacing between time and greeting
            rightNav.add(greetingLabel);
            rightNav.add(Box.createRigidArea(new Dimension(30, 0))); // Spacing between greeting and profile
            rightNav.add(profileButton);
        
            return rightNav;
        }

    //i want to add a profile button icon(?) here
    private JLabel createProfileButton() {
        ImageIcon profileIcon = new ImageIcon("Logos\\profileIcon.png");
        Image scaledProfileIcon = profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel profileLabel = new JLabel(new ImageIcon(scaledProfileIcon));
        profileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        profileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openProfileSettings();
            }
        });
        return profileLabel;
    }

    private JPanel createDashboardContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);
    
        // Top Row: Statistics
        // very important on third row; determines the name of the .java
        String[][] statsData = {
            {"Books", "Logos\\bookIcon.png", "BookList"},
            {"Circulation","Logos\\circulationIcon.png", "CheckedOutBooks"},
            {"Users", "Logos\\usersIcon.png", "UserList"},
        };
    
        for (String[] stat : statsData) {
            contentPanel.add(createStatisticsCard(stat[0], stat[1], stat.length > 2 ? stat[2] : null));
        }
    
        // Bottom Row: Action Buttons
        String[][] actionData = {
            {"Manage Books", "Logos\\managebookIcon.png", "EditBooks"},
            {"Authors", "Logos\\authorIcon.png", "AuthorList"},
            {"Manage Users", "Logos\\manageusersIcon.png", "EditUsers"},
        };
    
        for (String[] action : actionData) {
            contentPanel.add(createActionCard(action[0], action[1], action[2]));
        }
    
        return contentPanel;
    }
    
    private JPanel createStatisticsCard(String title, String iconPath, String windowClass) {
        return createInteractivePanel(title, iconPath, windowClass);
    }
    
    private JPanel createActionCard(String title, String iconPath, String windowClass) {
        return createInteractivePanel(title, iconPath, windowClass);
    }
    
    private JPanel createInteractivePanel(String title, String iconPath, String windowClass) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 0, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(BACKGROUND_COLOR);
    
        // Add image if the path is provided
        if (iconPath != null && !iconPath.isEmpty()) {
            JLabel iconLabel = new JLabel();
            iconLabel.setHorizontalAlignment(JLabel.CENTER);
    
            // Load the image and scale it
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
    
            panel.add(iconLabel, BorderLayout.CENTER);
        }
    
        // Add title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);
    
        // Mouse listener to open feature window on click
        panel.addMouseListener(new HoverEffectListener(panel));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openFeatureWindow(windowClass);
            }
        });
    
        return panel;
    }
    

    private void openFeatureWindow(String feature) {
        try {
            String className = feature;
            if (!feature.contains(".")) {
                // opens a .java file based on which dashboard content pannel is sent
                className = "GUI." + feature;  
            }
            
            // Load the class dynamically
            Class<?> clazz = Class.forName(className);  // Fully qualified name (e.g., "GUI.BookList")
            
            // Instantiate the class (assuming a no-argument constructor)
            Object featureInstance = clazz.getDeclaredConstructor().newInstance();
            
            if (featureInstance instanceof JFrame) {
                JFrame featureWindow = (JFrame) featureInstance;
                featureWindow.setSize(1200, 700);
                featureWindow.setLocationRelativeTo(this);
                featureWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                featureWindow.setVisible(true);
            } else {
                // Handle case where class doesn't extend JFrame
                System.out.println("Error: Class does not extend JFrame.");
            }
            
        } catch (Exception e) {
            // Handle errors (e.g., class not found, instantiation issues)
            JOptionPane.showMessageDialog(this, "Feature not implemented or class not found: " + feature, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    

    private void openProfileSettings() {
        JOptionPane.showMessageDialog(this, 
            "Profile Settings for " + adminName, 
            "Profile", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateTime(JLabel timeLabel) {
        timeLabel.setText(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
        
    private static class HoverEffectListener extends MouseAdapter {
        private final JPanel panel;

        public HoverEffectListener(JPanel panel) {
            this.panel = panel;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            panel.setBackground(new Color(255, 245, 238));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            panel.setBackground(BACKGROUND_COLOR);
        }
    }
    
    //greeting message based on time of day
    private void updateGreeting(JLabel greetingLabel) {
        LocalTime now = LocalTime.now();
        String greeting;

        if (now.isBefore(LocalTime.NOON)) {
            greeting = "Good Morning, " + adminName + "!";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            greeting = "Good Afternoon, " + adminName + "!";

        } else {
            greeting = "Good Evening, " + adminName + "!";
        }

        greetingLabel.setText(greeting);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> 
            new Admin_DashboardGUI("Administrator")
        );
    }
}