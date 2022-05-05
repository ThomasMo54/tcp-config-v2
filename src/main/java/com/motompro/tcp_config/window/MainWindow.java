package com.motompro.tcp_config.window;

import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final String WINDOW_TITLE = "TCPConfig";
    private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(960, 540);

    private JPanel mainPanel;
    private ConfigListComponent configListComponent;
    private JPanel buttonsPanel;

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
        configListComponent.setConfigs(TCPConfig.getInstance().getConfigs());
        // Init buttons panel
        initButtonsPanel();
    }

    private void initButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        // New button
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        JButton newButton = new JButton("Nouveau");
        buttonsPanel.add(newButton, constraints);
        // Import button
        constraints.gridy = 1;
        JButton importButton = new JButton("Importer");
        buttonsPanel.add(importButton, constraints);
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
        mainPanel.add(configListComponent, constraints);
        // Add buttons panel
        constraints.gridx = 1;
        constraints.weightx = 0;
        mainPanel.add(buttonsPanel, constraints);
    }
}
