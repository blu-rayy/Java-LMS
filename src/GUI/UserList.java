package GUI;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class UserList extends JFrame implements fontComponent {
    private JTable userTable;
    private DefaultTableModel tableModel;

    public UserList() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Registered Users");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createUserTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSearchPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        }

        private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\usersListOrange.png"); // Replace with the path to your icon
        Image resizedTaskbarIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedTaskbarIcon));

        JLabel titleLabel = new JLabel("Registered Users");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        return titlePanel;
        }

        private JPanel createUserTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        // Define column names
        String[] columnNames = {
            "User ID", "Full Name", "Email", "Contact Number", 
            "Registration Date", "User Type"
        };

        // Sample data (in a real application, this would come from a database)
        Object[][] data = {
            {"U001", "John Doe", "john.doe@example.com", "0912-345-6789", "2024-01-15", "Student"},
            {"U002", "Jane Smith", "jane.smith@example.com", "0923-456-7890", "2024-01-20", "Faculty"},
            {"U003", "Mike Johnson", "mike.johnson@example.com", "0934-567-8901", "2024-02-01", "Staff"},
            {"U004", "Sarah Williams", "sarah.w@example.com", "0945-678-9012", "2024-02-10", "Student"},
            {"U005", "Robert Brown", "robert.b@example.com", "0956-789-0123", "2024-02-15", "Librarian"}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        userTable = new JTable(tableModel);
        userTable.setFont(TABLE_FONT);
        userTable.setRowHeight(30);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getTableHeader().setReorderingAllowed(false);

        JTableHeader header = userTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Custom column widths
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // User ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Full Name
        userTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Email
        userTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Contact Number
        userTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Registration Date
        userTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // User Type

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);

        // Search field
        JTextField searchField = new JTextField(20);
        searchField.setFont(TABLE_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Name or Email");

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(TABLE_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));

        // Filter dropdown
        String[] filterOptions = {"All", "Student", "Faculty", "Staff", "Librarian"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterComboBox.setFont(TABLE_FONT);
        filterComboBox.addActionListener(e -> filterUsers((String) filterComboBox.getSelectedItem()));

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterComboBox);

        return searchPanel;
    }

    private void performSearch(String searchText) {
        // Basic search implementation
        if (searchText.trim().isEmpty()) {
            // Reset to show all rows if search is empty
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
            userTable.setRowSorter(sorter);
            sorter.setRowFilter(null);
            return;
        }

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);
        
        // Search across Name and Email columns
        RowFilter<DefaultTableModel, Object> filter = RowFilter.orFilter(
            Arrays.asList(
                RowFilter.regexFilter("(?i)" + searchText, 1), // Name column
                RowFilter.regexFilter("(?i)" + searchText, 2)  // Email column
            )
        );
        
        sorter.setRowFilter(filter);
    }

    private void filterUsers(String filterOption) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        userTable.setRowSorter(sorter);

        if ("All".equals(filterOption)) {
            sorter.setRowFilter(null);
            return;
        }

        // Filter based on the selected option
        RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter(filterOption, 5); // User Type column

        sorter.setRowFilter(filter);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new UserList().setVisible(true);
        });
    }
}