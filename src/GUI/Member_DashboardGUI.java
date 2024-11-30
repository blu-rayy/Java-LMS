package GUI;

import GUI.About.AboutPage;
import GUI.About.DevelopersList;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Member_DashboardGUI extends JFrame implements fontComponent {
    private final String userName;

    public Member_DashboardGUI(String userName) {
        this.userName = userName;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - User Dashboard");
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

        // Add logo to the left
        ImageIcon logoIcon = new ImageIcon("Logos\\ANP orange copy.png");
        Image scaledLogoIcon = logoIcon.getImage().getScaledInstance(32, 22, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(scaledLogoIcon));
        leftNav.add(logoLabel);

        // Mapping of button text to corresponding class names
        String[] navButtons = {"Books", "Authors"};
        String[] classNames = {"BookList", "AuthorList"};

        for (int i = 0; i < navButtons.length; i++) {
            JButton button = createNavButton(navButtons[i], classNames[i]);
            leftNav.add(button);
        }

        return leftNav;
    }

    private JButton createNavButton(String text, String className) {
        JButton button = new JButton(text);
        button.setFont(TITLE_FONT14);
        button.setBackground(BACKGROUND_COLOR);
        button.setForeground(PRIMARY_COLOR); // Set font color to PRIMARY_COLOR
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> openFeatureWindow(className));
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

    private JLabel createProfileButton() {
        ImageIcon profileIcon = new ImageIcon("Logos\\profileIcon.png");
        Image scaledProfileIcon = profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel profileLabel = new JLabel(new ImageIcon(scaledProfileIcon));
        profileLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Create custom dropdown menu
        JPopupMenu dropdownMenu = new JPopupMenu() {
            @Override
            public void show(Component invoker, int x, int y) {
                // Calculate position to show from the left side of the profile icon
                super.show(invoker, -getPreferredSize().width + invoker.getWidth(), invoker.getHeight());
            }
        };
        dropdownMenu.setBorder(new LineBorder(PRIMARY_COLOR, 1, true));
        
        // Custom menu item with orange highlight
        class CustomMenuItem extends JMenuItem {
            CustomMenuItem(String text) {
                super(text);
                setFont(PLAIN_FONT16);
                setPreferredSize(new Dimension(250, 40));
                setBorderPainted(false); // Ensure border is disabled for a clean look
                
                setForeground(new Color(52, 50, 49)); // Set initial text color to off black
        
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        setBackground(PRIMARY_COLOR); // Set background color for hover
                        setOpaque(true);
                        setForeground(Color.WHITE); // Change text color to white
                        repaint(); // Repaint to apply hover effect
                    }
                    
                    @Override
                    public void mouseExited(MouseEvent e) {
                        setBackground(null); // Reset background color when not hovered
                        setOpaque(false);
                        setForeground(new Color(52, 50, 49)); // Reset text color to off black
                        repaint(); // Repaint to revert hover effect
                    }
                });
            }
        
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Ensure the default painting happens first
                
                // Paint the custom background only if opaque
                if (isOpaque()) {
                    g.setColor(getBackground());
                    g.fillRect(0, 0, getWidth(), getHeight()); // Fill the background with the custom color
                }
                g.setColor(getForeground()); //pag eto lang, gumagana yung color pero walang text
                super.paintComponent(g); //pag eto lang, walang color yung hover pero may text
            }
        }
        
        // Account Menu Item
        CustomMenuItem accountMenuItem = new CustomMenuItem("Account");
        accountMenuItem.addActionListener(e -> showAccountDetails());
        dropdownMenu.add(accountMenuItem);
        
        // About Menu Item
        CustomMenuItem aboutMenuItem = new CustomMenuItem("About");
        aboutMenuItem.addActionListener(e -> showAboutDialog());
        dropdownMenu.add(aboutMenuItem);
        
        // Developers Menu Item
        CustomMenuItem developersMenuItem = new CustomMenuItem("Developers");
        developersMenuItem.addActionListener(e -> showDeveloperDialog());
        dropdownMenu.add(developersMenuItem);
        
        // Logout Menu Item
        CustomMenuItem logoutMenuItem = new CustomMenuItem("Log Out");
        logoutMenuItem.addActionListener(e -> confirmLogout());
        dropdownMenu.add(logoutMenuItem);
        
        profileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dropdownMenu.show(profileLabel, e.getX(), e.getY());
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
            {"Books Borrowed","Logos\\booksBorrowedIcon.png", "MemberBorrowedBooks"},
            {"Authors", "Logos\\authorIcon.png", "MemberAuthorList"},
        };
    
        for (String[] stat : statsData) {
            contentPanel.add(createStatisticsCard(stat[0], stat[1], stat.length > 2 ? stat[2] : null));
        }
    
        // Bottom Row: Action Buttons
        String[][] actionData = {
            {"Borrow Books", "Logos\\borrowBookIcon.png", "MemberBorrowBooks"},
            {" ", " ", " "},
            {" ", " ", " "},
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
                className = "GUI." + feature;  
            }
    
            // Load the class dynamically
            Class<?> clazz = Class.forName(className);  // Fully qualified name (e.g., "GUI.MemberBorrowBooks")
    
            // Check if the class requires the userName parameter in the constructor
            Object featureInstance;
            if (className.equals("GUI.MemberBorrowBooks") || className.equals("GUI.MemberBorrowedBooks")) {
                featureInstance = clazz.getDeclaredConstructor(String.class).newInstance(userName);
            } else {
                featureInstance = clazz.getDeclaredConstructor().newInstance();
            }
    
            JFrame featureWindow = (JFrame) featureInstance;
            featureWindow.setSize(1200, 700);
            featureWindow.setLocationRelativeTo(this);
            featureWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            featureWindow.setVisible(true);
    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Feature not implemented or class not found: " + feature, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to show account details
    private void showAccountDetails() {
        try {
            Class<?> accountDetailsClass = Class.forName("GUI.AccountDetails");
            JFrame accountDetailsFrame = (JFrame) accountDetailsClass.getDeclaredConstructor(String.class).newInstance(userName);
            accountDetailsFrame.setSize(500, 600);
            accountDetailsFrame.setLocationRelativeTo(this);
            accountDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            accountDetailsFrame.setVisible(true);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            JOptionPane.showMessageDialog(this, "Error opening account details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

  // Method to show About dialog
private void showAboutDialog() {
    // Create an instance of AboutPage
    AboutPage aboutPage = new AboutPage();

    // Set the AboutPage properties
    aboutPage.setSize(500, 320); 
    aboutPage.setLocationRelativeTo(this); 
    aboutPage.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    aboutPage.setVisible(true); 
}


private void showDeveloperDialog() {
    // Create an instance of DevelopersList
    DevelopersList developersList = new DevelopersList();

    // Set properties for the DevelopersList JFrame
    developersList.setVisible(true); // Make the JFrame visible
}

    // Method to confirm logout
    private void confirmLogout() {
        int response = JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to log out?", 
            "Confirm Logout", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            // Close current user dashboard
            this.dispose();
            
            // Open login screen (ANPLMSGUI)
            SwingUtilities.invokeLater(() -> {
                try {
                    Class<?> loginClass = Class.forName("GUI.ANPLMSGUI");
                    JFrame loginFrame = (JFrame) loginClass.getDeclaredConstructor().newInstance();
                    loginFrame.setVisible(true);
                } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                    JOptionPane.showMessageDialog(
                        null, 
                        "Error opening login screen: " + e.getMessage(), 
                        "Logout Error", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            });
        }
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
            greeting = "Good Morning, " + userName + "!";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            greeting = "Good Afternoon, " + userName + "!";

        } else {
            greeting = "Good Evening, " + userName + "!";
        }

        greetingLabel.setText(greeting);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> 
            new Member_DashboardGUI("User")
        );
    }
}
