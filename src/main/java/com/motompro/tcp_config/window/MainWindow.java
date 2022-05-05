package com.motompro.tcp_config.window;

import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final String WINDOW_TITLE = "TCPConfig v" + TCPConfig.VERSION;
    private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(960, 540);

    private JPanel mainPanel;
    private ConfigListComponent configListComponent;
    private JScrollPane configListScrollPane;
    private JPanel buttonsPanel;

    private JButton newButton;
    private JButton importButton;
    private JButton listButton;

    public MainWindow() {
        init();
        setMainLayout();
        this.setVisible(true);
    }

    private void init() {
        this.setTitle(WINDOW_TITLE);
        this.setSize(DEFAULT_WINDOW_DIMENSION);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Init main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        this.setContentPane(mainPanel);
        // Init config list component
        configListComponent = new ConfigListComponent();
        configListComponent.updateConfigs();
        configListScrollPane = new JScrollPane(configListComponent);
        configListScrollPane.setBackground(Color.MAGENTA);
        configListScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        configListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Init buttons
        initButtons();
        // Init buttons panel
        buttonsPanel = createButtonsPanel(newButton, importButton);
    }

    private void initButtons() {
        // New button
        newButton = new JButton("Nouveau");
        newButton.setFocusable(false);
        newButton.addActionListener(event -> setNewConfigLayout());
        // Import button
        importButton = new JButton("Importer");
        importButton.setFocusable(false);
        // List button
        listButton = new JButton("Liste");
        listButton.setFocusable(false);
    }

    private JPanel createButtonsPanel(JButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        for(JButton button : buttons) {
            panel.add(button, constraints);
            constraints.gridy++;
        }
        return panel;
    }

    private void setMainLayout() {
        mainPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        // Add config list
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        mainPanel.add(configListScrollPane, constraints);
        // Add buttons panel
        constraints.gridx = 1;
        constraints.weightx = 0;
        mainPanel.add(buttonsPanel, constraints);
        this.repaint();
    }

    private void setNewConfigLayout() {
        mainPanel.removeAll();
        this.repaint();
    }

    public void updateConfigs() {
        configListComponent.updateConfigs();
        configListComponent.revalidate();
    }
}
