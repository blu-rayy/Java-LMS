package GUI;

import backend.LibraryDatabase;
import backend.Member;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class LibrarianEditUsers extends JFrame implements fontComponent {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public LibrarianEditUsers() {
        initializeUI();
        refreshUserList(); // Load data from the database
    }

    private void initializeUI() {
        setTitle("ANP LMS - User Management");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createUserTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createActionButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\manageusersIconOrange.png");
        Image resizedTaskbarIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedTaskbarIcon));

        JLabel CountLabel = new JLabel("Total Users: " + LibraryDatabase.countMembers());
        CountLabel.setFont(TITLE_FONT14);
        CountLabel.setForeground(PRIMARY_COLOR);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(iconLabel);
        leftPanel.add(titleLabel);

        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(CountLabel, BorderLayout.EAST);

        return titlePanel;
    }

    private JPanel createUserTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        String[] columnNames = {"User ID", "Name", "Username", "Email", "Role", "Status", "Registration Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = userTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createActionButtonPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);

        JButton addUserBtn = createStyledButton("Add User", e -> addNewUser());
        JButton editUserBtn = createStyledButton("Edit User", e -> editSelectedUser());
        JButton deleteUserBtn = createStyledButton("Delete User", e -> deleteSelectedUser());
        JButton refreshBtn = createStyledButton("Refresh", e -> refreshUserList());

        actionPanel.add(addUserBtn);
        actionPanel.add(editUserBtn);
        actionPanel.add(deleteUserBtn);
        actionPanel.add(refreshBtn);

        return actionPanel;
    }

    private JButton createStyledButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(PLAIN_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void addNewUser() {
        JDialog addUserDialog = new JDialog(this, "Add New User", true);
        addUserDialog.setSize(400, 400);
        addUserDialog.setLocationRelativeTo(this);
    
        JPanel dialogPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
        dialogPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        dialogPanel.add(nameField);
    
        dialogPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        dialogPanel.add(usernameField);
    
        dialogPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        dialogPanel.add(emailField);
    
        dialogPanel.add(new JLabel("Phone Number:"));
        JTextField phoneNumberField = new JTextField();
        dialogPanel.add(phoneNumberField);
    
        dialogPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        dialogPanel.add(passwordField);
    
        dialogPanel.add(new JLabel("Role:"));
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Librarian"});
        dialogPanel.add(roleCombo);
    
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            if (nameField.getText().isEmpty() || usernameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || new String(passwordField.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name, Username, Email, Phone Number, and Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            Member newMember = new Member();
            newMember.setName(nameField.getText());
            newMember.setUsername(usernameField.getText());
            newMember.setEmail(emailField.getText());
            newMember.setPhoneNumber(phoneNumberField.getText());
            newMember.setPassword(new String(passwordField.getPassword()));
            newMember.setUserType(roleCombo.getSelectedItem().toString());
    
            LibraryDatabase.insertMember(newMember);
            refreshUserList();
            addUserDialog.dispose();
        });
    
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> addUserDialog.dispose());
    
        dialogPanel.add(saveButton);
        dialogPanel.add(cancelButton);
    
        addUserDialog.add(dialogPanel);
        addUserDialog.setVisible(true);
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        String userId = userTable.getValueAt(selectedRow, 0).toString();
        String currentName = userTable.getValueAt(selectedRow, 1).toString();
        String currentUsername = userTable.getValueAt(selectedRow, 2).toString();
        String currentEmail = userTable.getValueAt(selectedRow, 3).toString();
        String currentRole = userTable.getValueAt(selectedRow, 4).toString();
    
        JDialog editUserDialog = new JDialog(this, "Edit User", true);
        editUserDialog.setSize(400, 350); // Increased height to accommodate phone number field
    
        JPanel dialogPanel = new JPanel(new GridLayout(7, 2, 10, 10)); // Increased rows
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
        dialogPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField(currentName);
        dialogPanel.add(nameField);
    
        dialogPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField(currentUsername);
        dialogPanel.add(usernameField);
    
        dialogPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(currentEmail);
        dialogPanel.add(emailField);
    
        // Add phone number field
        dialogPanel.add(new JLabel("Phone Number:"));
        JTextField phoneNumberField = new JTextField(); // You'll need to fetch and populate the current phone number
        dialogPanel.add(phoneNumberField);
    
        dialogPanel.add(new JLabel("Role:"));
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Student", "Librarian", "Admin"});
        roleCombo.setSelectedItem(currentRole);
        dialogPanel.add(roleCombo);
    
        // Fetch the current phone number for the selected user
        Member currentMember = LibraryDatabase.getMemberById(userId);
        if (currentMember != null) {
            phoneNumberField.setText(currentMember.getPhoneNumber());
        }
    
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // Validate input fields
            if (nameField.getText().isEmpty() || 
                usernameField.getText().isEmpty() || 
                emailField.getText().isEmpty() || 
                phoneNumberField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Name, Username, Email, and Phone Number cannot be empty.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            Member updatedMember = new Member();
            updatedMember.setMemberID(userId);
            updatedMember.setName(nameField.getText());
            updatedMember.setUsername(usernameField.getText());
            updatedMember.setEmail(emailField.getText());
            updatedMember.setPhoneNumber(phoneNumberField.getText());
            updatedMember.setUserType(roleCombo.getSelectedItem().toString());
    
            LibraryDatabase.updateMember(updatedMember);
            refreshUserList();
            editUserDialog.dispose();
        });
    
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> editUserDialog.dispose());
    
        dialogPanel.add(saveButton);
        dialogPanel.add(cancelButton);
    
        editUserDialog.add(dialogPanel);
        editUserDialog.setLocationRelativeTo(this);
        editUserDialog.setVisible(true);
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmDelete = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
        if (confirmDelete != JOptionPane.YES_OPTION) return;

        String userId = userTable.getValueAt(selectedRow, 0).toString();
        LibraryDatabase.deleteMember(userId);
        refreshUserList();
    }

    private void refreshUserList() {
        List<Member> members = LibraryDatabase.getAllMembers();
        tableModel.setRowCount(0);
        for (Member member : members) {
            tableModel.addRow(new Object[]{
                    member.getMemberID(),
                    member.getName(),
                    member.getUsername(),
                    member.getEmail(),
                    member.getUserType(),
                    "Active", // Placeholder
                    member.getRegistrationDate()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarianEditUsers().setVisible(true));
    }
}