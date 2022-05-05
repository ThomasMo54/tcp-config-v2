package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;

import javax.swing.*;
import java.awt.*;

public class ConfigComponent extends JPanel {

    private static final int INSETS = 20;

    private final Config config;
    private final Color backgroundColor;

    public ConfigComponent(Config config, Color backgroundColor) {
        this.config = config;
        this.backgroundColor = backgroundColor;
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        this.setBackground(backgroundColor);
        // Infos panel
        JPanel infosPanel = new JPanel();
        infosPanel.setBackground(backgroundColor);
        infosPanel.setLayout(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel(config.getName());
        infosPanel.add(nameLabel);
        JLabel adapterLabel = new JLabel(config.getAdapter());
        adapterLabel.setForeground(Color.GRAY);
        infosPanel.add(adapterLabel);
        GridBagConstraints constraints = new GridBagConstraints();
        // Add config list
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        this.add(infosPanel, constraints);
    }
}
