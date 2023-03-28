package viewer;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

//TODO refactor:
// - move action listener code to separate helper functions/class
// - rename JPanels more descriptively
public class SQLiteViewer extends JFrame {

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

    private void initComponents() {
        // top-level app UI container
        JPanel mainPanel = new JPanel();
        mainPanel.setName("MainPanel");
        mainPanel.setBounds(0, 0, 700, 500);
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel midPanel= new JPanel(); // container for DB query UI elements
        midPanel.setName("MidPanel");
        midPanel.setLayout(new BorderLayout());
        mainPanel.add(midPanel, BorderLayout.CENTER);

        JTextArea queryTextArea = new JTextArea();
        queryTextArea.setName("QueryTextArea");
        midPanel.add(queryTextArea, BorderLayout.CENTER);
        queryTextArea.setVisible(true);

        JPanel topPanel= new JPanel(); // container for file and DB object selection operation UI elements
        topPanel.setName("TopPanel");
        topPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        topPanel.add(fileNameTextField, BorderLayout.CENTER);
        fileNameTextField.setVisible(true);

        JComboBox<String> tablesComboBox = new JComboBox<>();
        tablesComboBox.setName("TablesComboBox");
        topPanel.add(tablesComboBox, BorderLayout.SOUTH);
        tablesComboBox.addActionListener(actionEvent ->
                queryTextArea.setText(String.format("SELECT * from %s;", tablesComboBox.getSelectedItem())));
        tablesComboBox.setVisible(true);

        JButton openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        topPanel.add(openFileButton, BorderLayout.EAST);
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

        JButton executeQueryButton = new JButton("Execute");
        executeQueryButton.setName("ExecuteQueryButton");
        midPanel.add(executeQueryButton, BorderLayout.EAST);
        executeQueryButton.setVisible(true);

        openFileButton.setVisible(true);

        setVisible(true);
    }
}
