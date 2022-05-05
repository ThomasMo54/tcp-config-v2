package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConfigListComponent extends JPanel {

    private static final Color CONFIG_COMPONENT_COLOR_1 = new Color(240, 240, 240);
    private static final Color CONFIG_COMPONENT_COLOR_2 = new Color(225, 225, 225);

    private JPanel contentPanel;
    private List<Config> configs;

    public ConfigListComponent() {
        init();
    }

    private void init() {
        this.setLayout(new BorderLayout());
        contentPanel = new JPanel(new GridBagLayout());
        this.add(contentPanel, BorderLayout.NORTH);
        this.setBackground(Color.WHITE);
    }

    public void setConfigs(List<Config> configList) {
        this.configs = configList;
        contentPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.weightx = 1;
        AtomicInteger gridy = new AtomicInteger();
        configs.forEach(config -> {
            ConfigComponent configComponent = new ConfigComponent(config, gridy.get() % 2 == 0 ? CONFIG_COMPONENT_COLOR_1 : CONFIG_COMPONENT_COLOR_2);
            constraints.gridy = gridy.getAndIncrement();
            contentPanel.add(configComponent, constraints);
        });
    }
}
