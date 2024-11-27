package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Admin_DashboardGUI extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(255, 136, 0);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

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
        JPanel rightNav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightNav.setBackground(BACKGROUND_COLOR);

        JTextField searchField = new JTextField(20);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setFont(BUTTON_FONT);

        JButton profileButton = createProfileButton();

        rightNav.add(searchField);
        rightNav.add(profileButton);

        return rightNav;
    }

    //i want to add a profile button icon(?) here
    private JButton createProfileButton() {
        JButton profileBtn = new JButton("Profile");
        profileBtn.setPreferredSize(new Dimension(80, 33));
        profileBtn.setBackground(PRIMARY_COLOR);
        profileBtn.setForeground(Color.WHITE);
        profileBtn.setBorderPainted(false);
        profileBtn.addActionListener(e -> openProfileSettings());
        return profileBtn;
    }

    private JPanel createDashboardContentPanel() {
        JPanel contentPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        contentPanel.setBackground(BACKGROUND_COLOR);

        // Top Row: Statistics
        String[][] statsData = {
            {"Users", "UserStats"},
            {"Checked Out", "CheckedOutBooks"},
            {"Total Books", "BookInventory"},
            {"Active Authors", "AuthorList"}
        };

        for (String[] stat : statsData) {
            contentPanel.add(createStatisticsCard(stat[0], stat[1]));
        }

        // Bottom Row: Action Buttons
        String[][] actionData = {
            {"Audit Log", "AuditLog"},
            {"System Reports", "SystemReports"},
            {"Manage Books", "BookManagement"},
            {"User Management", "UserManagement"}
        };

        for (String[] action : actionData) {
            contentPanel.add(createActionCard(action[0], action[1]));
        }

        return contentPanel;
    }

    private JPanel createStatisticsCard(String title, String windowClass) {
        return createInteractivePanel(title, "", windowClass);
    }

    private JPanel createActionCard(String title, String windowClass) {
        return createInteractivePanel(title, null, windowClass);
    }

    private JPanel createInteractivePanel(String title, String value, String windowClass) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JLabel valueLabel = new JLabel(value != null ? value : "");
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(valueLabel, BorderLayout.CENTER);

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
        JFrame featureWindow = new JFrame(feature);
        featureWindow.setSize(800, 600);
        featureWindow.setLocationRelativeTo(this);
        featureWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        JLabel contentLabel = new JLabel("Implementing " + feature + " functionality...");
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(contentLabel, BorderLayout.CENTER);

        featureWindow.add(contentPanel);
        featureWindow.setVisible(true);
    }

    private void openProfileSettings() {
        JOptionPane.showMessageDialog(this, 
            "Profile Settings for " + adminName, 
            "Profile", 
            JOptionPane.INFORMATION_MESSAGE
        );
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> 
            new Admin_DashboardGUI("Administrator")
        );
    }
}