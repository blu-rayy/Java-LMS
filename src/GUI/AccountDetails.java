package GUI;

import backend.LibraryDatabase;
import backend.Member;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class AccountDetails extends JFrame implements fontComponent {
    public AccountDetails(String username) {
        // Fetch member details from database -- for testing purposes, i set a name
        Member member = LibraryDatabase.getMemberDetails("erichiii");
        
        // Set up frame
        setTitle("Account Details");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("Account Details");
        titleLabel.setFont(TITLE_FONT18);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Profile Icon
        ImageIcon profileIcon = new ImageIcon("Logos\\profileIcon.png");
        Image scaledProfileIcon = profileIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel profileIconLabel = new JLabel(new ImageIcon(scaledProfileIcon));
        profileIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(profileIconLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        // Taskbar icon
        Image resizedTaskbarIcon = profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        
        // Account Details Table
        String[][] accountData = {
            {"Member ID", member != null ? String.valueOf(member.getMemberID()) : "N/A"},
            {"Name", member != null ? member.getName() : "N/A"},
            {"Username", member != null ? member.getUsername() : "N/A"},
            {"Email", member != null ? member.getEmail() : "N/A"},
            {"Phone Number", member != null ? member.getPhoneNumber() : "N/A"},
            {"Account Creation Date", member != null ? formatCreationDate(member.getRegistrationDate()) : "N/A"},
            {"User Type", member != null ? member.getUserType() : "N/A"}
        };
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new GridLayout(accountData.length, 2, 10, 10));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        
        for (String[] detail : accountData) {
            JLabel keyLabel = new JLabel(detail[0] + ":");
            keyLabel.setFont(PLAIN_FONT);
            
            JLabel valueLabel = new JLabel(detail[1]);
            valueLabel.setFont(PLAIN_FONT);
            
            detailsPanel.add(keyLabel);
            detailsPanel.add(valueLabel);
        }
        
        mainPanel.add(detailsPanel);
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        closeButton.setBackground(PRIMARY_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFont(PLAIN_FONT);
        closeButton.addActionListener(e -> dispose());
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 0)));
        mainPanel.add(closeButton);
        
        add(mainPanel);
    }
    
    // Helper method to format creation date
    private String formatCreationDate(String creationDate) {
            if (creationDate == null) return "N/A";
            return LocalDate.parse(creationDate).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
    }
    
    // Optional: Main method for standalone testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AccountDetails("").setVisible(true);
        });
    }
}