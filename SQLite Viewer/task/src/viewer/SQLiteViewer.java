package viewer;

import javax.swing.*;
import java.awt.*;

public class SQLiteViewer extends JFrame {

    //TODO create a JPanel w/ UI designer, add to JFrame

    public SQLiteViewer() {
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
        JPanel mainPanel = new JPanel();
        mainPanel.setName("MainPanel");
        mainPanel.setBounds(0, 0, 700, 500);
        //mainPanel.setLayout(null);
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        JPanel topPanel= new JPanel();
        topPanel.setName("TopPanel");
        topPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JTextField fileNameTextField = new JTextField();
        fileNameTextField.setName("FileNameTextField");
        //fileNameTextField.setBounds(20, 20, 500, 30);
        //mainPanel.add(fileNameTextField);
        topPanel.add(fileNameTextField, BorderLayout.CENTER);
        fileNameTextField.setVisible(true);

        JButton openFileButton = new JButton("Open");
        openFileButton.setName("OpenFileButton");
        //openFileButton.setBounds(550, 20, 100, 30);
        //mainPanel.add(openFileButton);
        topPanel.add(openFileButton, BorderLayout.EAST);
        openFileButton.setVisible(true);

        setVisible(true);
    }
}
