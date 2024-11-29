package GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CheckedOutBooks extends JFrame implements fontComponent {
    private JTable borrowedBooksTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JLabel counterLabel;

    public CheckedOutBooks() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Checked Out Books");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon taskbarIcon = new ImageIcon("Logos\\ANP orange copy.png");
        Image resizedTaskbarIcon = taskbarIcon.getImage().getScaledInstance(64, 43, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Table Panel with Search
        JPanel tablePanel = createBorrowedBooksTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = createActionPanel();
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        add(mainPanel);
        updateCounter();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Checked Out Books");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setPreferredSize(new Dimension(300, 30));
        
        searchField = new JTextField(20);
        searchField.setFont(PLAIN_FONT16);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            String text = searchField.getText().trim();
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\circulationIconOrange.png");
        Image resizedIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedIcon));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8)); //padding
        
        //counter label top right
        counterLabel = new JLabel();
        counterLabel.setFont(TITLE_FONT14);
        counterLabel.setForeground(PRIMARY_COLOR);

        //title panel
        JPanel titleContentPanel = new JPanel(new BorderLayout());
        titleContentPanel.setBackground(BACKGROUND_COLOR);
        titleContentPanel.add(iconLabel, BorderLayout.WEST);
        titleContentPanel.add(titleLabel, BorderLayout.CENTER);
        titleContentPanel.add(counterLabel, BorderLayout.EAST);
        titlePanel.add(titleContentPanel, BorderLayout.CENTER);
        
        return titlePanel;
    }

    private JPanel createBorrowedBooksTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        String[] columnNames = {"Transaction ID", "Member Name", "Book Title", "Transaction Type", "Transaction Date"};

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //all cells are non-editable
            }
        };

        borrowedBooksTable = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        borrowedBooksTable.setRowSorter(rowSorter);

        borrowedBooksTable.setFont(PLAIN_FONT16);
        borrowedBooksTable.setRowHeight(30);
        borrowedBooksTable.getTableHeader().setFont(TITLE_FONT14);
        borrowedBooksTable.getTableHeader().setBackground(PRIMARY_COLOR);
        borrowedBooksTable.getTableHeader().setForeground(Color.WHITE);

        addSampleBorrowedBooks();

        JScrollPane scrollPane = new JScrollPane(borrowedBooksTable);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void addSampleBorrowedBooks() {
        Object[][] sampleData = {
            {"T001", "Alice Brown", "Java Programming", "Borrow", "2024-02-15"},
            {"T002", "Bob Wilson", "Database Systems", "Borrow", "2024-02-20"},
            {"T003", "Carol Davis", "Software Engineering", "Borrow", "2024-02-25"}
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new BorderLayout(10, 10));
        actionPanel.setBackground(BACKGROUND_COLOR);
    
        // Search panel now added to bottom left
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        actionPanel.add(searchPanel, BorderLayout.WEST);
    
        // Modify button on bottom right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        JButton modifyButton = createStyledButton("Modify Status", "Logos\\editIcon.png");
        modifyButton.setPreferredSize(new Dimension(150, 40));
        modifyButton.addActionListener(e -> modifyBookStatus());
        buttonPanel.add(modifyButton);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
    
        return actionPanel;
    }

    private void modifyBookStatus() {
        int selectedRow = borrowedBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a book to modify.", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] statusOptions = {"Active", "Overdue", "Returned"};
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 3);
        
        String newStatus = (String) JOptionPane.showInputDialog(
            this, 
            "Change Status:", 
            "Modify Book Status", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            statusOptions, 
            currentStatus
        );

        if (newStatus != null && !newStatus.equals(currentStatus)) {
            tableModel.setValueAt(newStatus, selectedRow, 3);
        }
    }

    //update the counter label with the number of checked out and available books
    private void updateCounter() {
        int checkedOutCount = 0;
        int availableCount = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String status = (String) tableModel.getValueAt(i, 3);
            if ("Active".equalsIgnoreCase(status) || "Overdue".equalsIgnoreCase(status)) {
                checkedOutCount++;
            } else if ("Returned".equalsIgnoreCase(status)) {
                availableCount++;
            }
        }

        counterLabel.setText("Checked Out: " + checkedOutCount + " | Available: " + availableCount);
    }

    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(TITLE_FONT14);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        if (iconPath != null && !iconPath.isEmpty()) {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
        }

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CheckedOutBooks().setVisible(true);
        });
    }
}
