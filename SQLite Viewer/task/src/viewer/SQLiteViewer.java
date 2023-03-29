package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SQLiteViewer extends JFrame {

    private JPanel mainPanel; // top-level container for all UI elements of main app window
    private JTextField fileNameTextField;
    private JComboBox<String> tablesComboBox;
    private JTextArea queryTextArea;
    private JTable table;


    public SQLiteViewer() {
        // main app window
        super("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);

        initComponents();

        setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setName("MainPanel");
        mainPanel.setBounds(0, 0, 700, 500);
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
    }

    private void initQueryPanel() {
        // container for DB query UI elements
        JPanel queryPanel = new JPanel(); // container for DB query UI elements
        queryPanel.setName("MidPanel");
        queryPanel.setLayout(new BorderLayout());
        mainPanel.add(queryPanel, BorderLayout.CENTER);

        queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        queryPanel.add(queryTextArea, BorderLayout.CENTER);
        queryTextArea.setVisible(true);

        JButton executeQueryButton = new JButton("Execute");
        executeQueryButton.setName("ExecuteQueryButton");
        executeQueryButton.addActionListener(actionEvent -> setQueryResultsScrollPane());
        queryPanel.add(executeQueryButton, BorderLayout.EAST);
        executeQueryButton.setVisible(true);
    }

    private void initInputSelectionPanel() {
        // container for file and DB object selection operation UI elements
        JPanel inputSelectionPanel = new JPanel(); // container for file and DB object selection operation UI elements
        inputSelectionPanel.setName("TopPanel");
        inputSelectionPanel.setLayout(new BorderLayout());
        mainPanel.add(inputSelectionPanel, BorderLayout.NORTH);

        fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        inputSelectionPanel.add(fileNameTextField, BorderLayout.CENTER);
        fileNameTextField.setVisible(true);

        tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");
        inputSelectionPanel.add(tablesComboBox, BorderLayout.SOUTH);
        tablesComboBox.addActionListener(actionEvent -> setQueryText());
        tablesComboBox.setVisible(true);

        JButton openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        inputSelectionPanel.add(openFileButton, BorderLayout.EAST);
        openFileButton.addActionListener(actionEvent -> {
            String dbFileName = fileNameTextField.getText();
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName)){
                String sql = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%';";
                try (PreparedStatement stmt = connection.prepareStatement(sql);
                     ResultSet resultSet = stmt.executeQuery()) {
                    tablesComboBox.removeAllItems();
                    while (resultSet.next()) {
                        tablesComboBox.addItem(resultSet.getString("name"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        openFileButton.setVisible(true);
    }

    private void initQueryResultsPanel() {
        // container for DB query results
        JPanel queryResultsPanel = new JPanel(); // container for DB query results
        queryResultsPanel.setName("LowPanel");
        queryResultsPanel.setLayout(new BorderLayout());
        mainPanel.add(queryResultsPanel, BorderLayout.SOUTH);

        table = new JTable();
        table.setName("Table");
        JScrollPane queryResultsScrollPane = new JScrollPane(table);
        queryResultsScrollPane.setPreferredSize(new Dimension(700, 350));
        queryResultsPanel.add(queryResultsScrollPane, BorderLayout.SOUTH);
    }

    private void initComponents() {
        initMainPanel();
        initQueryPanel();
        initInputSelectionPanel();
        initQueryResultsPanel();
        setVisible(true);
    }

    private void setQueryText() {
        queryTextArea.setText(String.format("SELECT * from %s;", tablesComboBox.getSelectedItem()));
    }

    private void setQueryResultsScrollPane() {
        String sql = queryTextArea.getText();
        String dbFileName = fileNameTextField.getText();

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName)){
            try (PreparedStatement stmt = connection.prepareStatement(sql);
                 ResultSet resultSet = stmt.executeQuery()) {
                table.removeAll();
                DefaultTableModel tableModel = new DefaultTableModel();
                int columnCount = resultSet.getMetaData().getColumnCount();
                String[] columnNames = new String[columnCount];
                for (int i = 1; i <= columnNames.length; i++) {
                    columnNames[i-1] = resultSet.getMetaData().getColumnName(i);
                }
                tableModel.setColumnIdentifiers(columnNames);
                table.setModel(tableModel);
                table.setEnabled(false); // disables editing
                //resultSet.next(); // 1st row is (interestingly) column names/metadata
                while (resultSet.next()) {
                    String[] row = new String[columnCount];
                    for (int i = 1; i <= row.length; i++) {
                        row[i-1] = resultSet.getString(i);
                    }
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
