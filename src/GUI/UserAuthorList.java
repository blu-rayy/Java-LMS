package GUI;

import backend.SQLiteDatabase;
import java.awt.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class UserAuthorList extends JFrame implements fontComponent {
    private JTable authorTable;
    private DefaultTableModel tableModel;

    public UserAuthorList() {
        initializeUI();
        loadAuthorsFromDatabase();
    }

    private void initializeUI() {
        setTitle("ANP LMS - Author List");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
    
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createAuthorTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createSearchAndActionPanel(), BorderLayout.SOUTH); // Combined panel here
    
        add(mainPanel);
    }
 

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(BACKGROUND_COLOR);

        // Add icon beside title
        ImageIcon icon = new ImageIcon("Logos\\orangeIcons\\authorIconOrange.png"); // Replace with the path to your icon
        Image resizedTaskbarIcon = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        setIconImage(resizedTaskbarIcon);
        JLabel iconLabel = new JLabel(new ImageIcon(resizedTaskbarIcon));

        JLabel titleLabel = new JLabel("Author List");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createAuthorTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
    
        String[] columnNames = {"Author ID", "Author Name", "Book Count"};
    
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
    
        authorTable = new JTable(tableModel);
        authorTable.setFont(TABLE_FONT);
        authorTable.setRowHeight(30);
        authorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        JTableHeader header = authorTable.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    
        JScrollPane scrollPane = new JScrollPane(authorTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 1));
    
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }
    

    private JPanel createSearchAndActionPanel() {
        JPanel searchAndActionPanel = new JPanel(new BorderLayout());
        searchAndActionPanel.setBackground(BACKGROUND_COLOR);
    
        // Add search panel and action buttons panel to the same container
        searchAndActionPanel.add(createSearchPanel(), BorderLayout.WEST);
    
        return searchAndActionPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BACKGROUND_COLOR);
    
        JTextField searchField = new JTextField(20);
        searchField.setFont(TABLE_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        searchField.setToolTipText("Search by Author Name");
    
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_COLOR);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(TABLE_FONT);
        searchButton.addActionListener(e -> performSearch(searchField.getText()));
    
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
    
        return searchPanel;
    }

    private void performSearch(String searchText) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        authorTable.setRowSorter(sorter);

        if (searchText.trim().isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + searchText, 0);
        sorter.setRowFilter(filter);
    }

    private void loadAuthorsFromDatabase() {
        tableModel.setRowCount(0);
        Map<String, Integer> authorCountMap = fetchAuthorsFromDatabase();

        for (Map.Entry<String, Integer> entry : authorCountMap.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }

    private Map<String, Integer> fetchAuthorsFromDatabase() {
        Map<String, Integer> authorCountMap = new HashMap<>();

        String query = "SELECT author, COUNT(*) AS book_count FROM books GROUP BY author";

        try (Connection connection = SQLiteDatabase.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String author = resultSet.getString("author");
                int bookCount = resultSet.getInt("book_count");
                authorCountMap.put(author, bookCount);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching authors from database: " + e.getMessage());
        }

        return authorCountMap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserAuthorList().setVisible(true));
    }
}
