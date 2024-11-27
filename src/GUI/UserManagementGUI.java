package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class UserManagementGUI extends JFrame {
    private static final Color PRIMARY_COLOR = new Color(255, 136, 0);
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserManagementGUI() {
        initializeUI();
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
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createUserTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        // Define column names
        String[] columnNames = {
            "User ID", "Name", "Email", "Role", "Status", "Last Login"
        };

        // Sample data (in a real application, this would come from a database)
        Object[][] data = {
            {"U001", "John Doe", "john.doe@example.com", "Student", "Active", "2024-02-15"},
            {"U002", "Jane Smith", "jane.smith@example.com", "Librarian", "Active", "2024-02-16"},
            {"U003", "Mike Johnson", "mike.johnson@example.com", "Admin", "Inactive", "2024-01-20"}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);

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
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    private void addNewUser() {
        JDialog addUserDialog = new JDialog(this, "Add New User", true);
        addUserDialog.setSize(400, 300);
        addUserDialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        dialogPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        dialogPanel.add(nameField);

        dialogPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        dialogPanel.add(emailField);

        dialogPanel.add(new JLabel("Role:"));
        String[] roles = {"Student", "Librarian", "Admin"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        dialogPanel.add(roleCombo);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            // In a real application, validate and save to database
            Object[] newUser = {
                "U" + (tableModel.getRowCount() + 1),
                nameField.getText(),
                emailField.getText(),
                roleCombo.getSelectedItem(),
                "Active",
                new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())
            };
            tableModel.addRow(newUser);
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
            JOptionPane.showMessageDialog(this, 
                "Please select a user to edit", 
                "No User Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        // Placeholder for edit user functionality
        JOptionPane.showMessageDialog(this, 
            "Edit functionality will be implemented in future updates", 
            "Edit User", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a user to delete", 
                "No User Selected", 
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmDelete = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this user?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmDelete == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
        }
    }

    private void refreshUserList() {
        // In a real application, this would reload data from the database
        JOptionPane.showMessageDialog(this, 
            "User list refreshed", 
            "Refresh", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserManagementGUI().setVisible(true);
        });
    }
}