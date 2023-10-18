package core;

import main.Runner;

import javax.swing.*;
import java.awt.*;

public class Infographic extends JFrame {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 600;

    private DefaultListModel<String> checkingListModel;
    private DefaultListModel<String> aliveListModel;
    private DefaultListModel<String> liveWithInfoListModel;

    private JLabel counterLabel; // Counter label to display the live count
    private long counter = 0; // Counter variable

    public Infographic() {
        initializeUI();
        testMethods();
    }

    private void initializeUI() {
        setTitle("Minecraft Server Checker");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a JTabbedPane to hold the tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create the list models for each tab
        checkingListModel = new DefaultListModel<>();
        aliveListModel = new DefaultListModel<>();
        liveWithInfoListModel = new DefaultListModel<>();

        // Add the IP addresses currently being checked tab
        JList<String> checkingList = new JList<>(checkingListModel);
        JScrollPane checkingScrollPane = new JScrollPane(checkingList);
        checkingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scroll bar
        JPanel checkingPanel = new JPanel(new BorderLayout());
        checkingPanel.add(checkingScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Checking", checkingPanel);

        // Add the historically alive IP addresses tab
        JList<String> aliveList = new JList<>(aliveListModel);
        JScrollPane aliveScrollPane = new JScrollPane(aliveList);
        aliveScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scroll bar
        JPanel alivePanel = new JPanel(new BorderLayout());
        alivePanel.add(aliveScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("History", alivePanel);

        aliveList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedIP = aliveList.getSelectedValue();
                if (selectedIP != null) {
                    displayIPInformation(Runner.map.getOrDefault(selectedIP, "That IP is not stored properly, or does not have significant data attached to it."));
                    System.out.println(selectedIP);
                    aliveList.clearSelection();
                }
            }
        });

        // Add the live IP addresses with server information tab
        JList<String> liveWithInfoList = new JList<>(liveWithInfoListModel);
        JScrollPane liveWithInfoScrollPane = new JScrollPane(liveWithInfoList);
        liveWithInfoScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Enable vertical scroll bar
        JPanel livePanel = new JPanel(new BorderLayout());
        livePanel.add(liveWithInfoScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Live with Info", livePanel);

        liveWithInfoList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedIP = liveWithInfoList.getSelectedValue();
                if (selectedIP != null) {
                    displayIPInformation(Runner.map.getOrDefault(selectedIP, "That IP is not stored."));
                    System.out.println(selectedIP);
                    liveWithInfoList.clearSelection(); // Clear the selection after displaying the information
                }
            }
        });

        JPanel settingsPanel = new JPanel(new BorderLayout());

        JCheckBox showCurrentServersCheckBox = new JCheckBox("Show Current Servers");
        showCurrentServersCheckBox.setSelected(false);
        showCurrentServersCheckBox.addActionListener(e -> {
            main.Runner.settings1 = showCurrentServersCheckBox.isSelected();
        });

        JTextArea performanceInfoTextArea = new JTextArea();
        performanceInfoTextArea.setEditable(false);
        performanceInfoTextArea.setText("Keeping this option enabled will likely negatively impact application performance.");

        settingsPanel.add(showCurrentServersCheckBox, BorderLayout.NORTH);
        settingsPanel.add(performanceInfoTextArea, BorderLayout.CENTER);

        tabbedPane.addTab("Settings", settingsPanel);

        // Add the counter label to the main frame
        counterLabel = new JLabel("Counter: " + counter);
        getContentPane().add(counterLabel, BorderLayout.SOUTH);

        // Create the kill button
        JButton killButton = new JButton("Kill");
        killButton.addActionListener(e -> System.exit(0));

        // Create a panel to hold the kill button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(killButton);

        // Add the button panel to the main frame
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Add the tabbed pane to the main frame
        getContentPane().add(tabbedPane);

        // Display the main frame
        setVisible(true);
    }

    public void addToCheckingTab(String ipAddress) {
        SwingUtilities.invokeLater(() -> {
            checkingListModel.addElement(ipAddress);
        });
    }

    public void addToAliveTab(String ipAddress) {
        SwingUtilities.invokeLater(() -> {
            aliveListModel.addElement(ipAddress);
        });
    }

    public void addToLiveWithInfoTab(String ipAddress) {
        SwingUtilities.invokeLater(() -> {
            liveWithInfoListModel.addElement(ipAddress);
        });
    }

    public void updateCounter() {
        counter++;
        SwingUtilities.invokeLater(() -> counterLabel.setText("Counter: " + counter));
    }

    private void testMethods() {
        addToCheckingTab("SYSTEM TEST");
        addToCheckingTab("SYSTEM TEST");
        addToCheckingTab("SYSTEM TEST");
        addToAliveTab("SYSTEM TEST");
        addToAliveTab("SYSTEM TEST");
        addToLiveWithInfoTab("SYSTEM TEST");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println(e.getLocalizedMessage());
        }

        SwingUtilities.invokeLater(() -> {
            checkingListModel.removeElement("SYSTEM TEST");
            checkingListModel.removeElement("SYSTEM TEST");
            checkingListModel.removeElement("SYSTEM TEST");
            aliveListModel.removeElement("SYSTEM TEST");
            aliveListModel.removeElement("SYSTEM TEST");
            liveWithInfoListModel.removeElement("SYSTEM TEST");
            updateCounter(); // Update counter after removing elements
        });
    }

    private void displayIPInformation(String text) {
        JFrame infoFrame = new JFrame("IP Address Information");
        infoFrame.setSize(300, 200);
        infoFrame.setLocationRelativeTo(this); // Center the frame relative to the main frame

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText(text);

        JScrollPane scrollPane = new JScrollPane(textArea);
        infoFrame.getContentPane().add(scrollPane);

        infoFrame.setVisible(true);
    }
}