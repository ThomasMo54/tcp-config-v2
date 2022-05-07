package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ConfigListComponent extends JPanel implements DocumentListener {

    private static final Color CONFIG_COMPONENT_COLOR_1 = new Color(240, 240, 240);
    private static final Color CONFIG_COMPONENT_COLOR_2 = new Color(225, 225, 225);
    private static final int SEARCH_BAR_HEIGHT = 30;
    private static final int SCROLL_SPEED = 4;

    private JPanel contentPanel;
    private JTextField searchInput;

    public ConfigListComponent() {
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.WHITE);
        // Search input
        searchInput = new JTextField();
        searchInput.setPreferredSize(new Dimension(0, SEARCH_BAR_HEIGHT));
        searchInput.setMinimumSize(new Dimension(0, SEARCH_BAR_HEIGHT));
        searchInput.setBorder(BorderFactory.createCompoundBorder(searchInput.getBorder(), BorderFactory.createEmptyBorder(0, ConfigComponent.INSETS - 3, 0, 0)));
        searchInput.getDocument().addDocumentListener(this);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        this.add(searchInput, constraints);
        // Config list
        JPanel listContentPane = new JPanel(new BorderLayout());
        listContentPane.setBackground(Color.WHITE);
        contentPanel = new JPanel(new GridBagLayout());
        listContentPane.add(contentPanel, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(listContentPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_SPEED);
        constraints.gridy = 1;
        constraints.weighty = 1;
        this.add(scrollPane, constraints);
    }

    public void updateConfigs(List<Config> configs) {
        contentPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.weightx = 1;
        AtomicInteger gridy = new AtomicInteger();
        configs.sort(Comparator.comparing(c -> c.getName().toLowerCase()));
        configs.forEach(config -> {
            ConfigComponent configComponent = new ConfigComponent(config, gridy.get() % 2 == 0 ? CONFIG_COMPONENT_COLOR_2 : CONFIG_COMPONENT_COLOR_1);
            constraints.gridy = gridy.getAndIncrement();
            contentPanel.add(configComponent, constraints);
        });
    }

    private List<Config> filterConfigs(List<Config> configs, String filter) {
        return configs.stream().filter(config -> config.getName().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateConfigs(filterConfigs(TCPConfig.getInstance().getConfigs(), searchInput.getText()));
        this.revalidate();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateConfigs(filterConfigs(TCPConfig.getInstance().getConfigs(), searchInput.getText()));
        this.revalidate();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateConfigs(filterConfigs(TCPConfig.getInstance().getConfigs(), searchInput.getText()));
        this.revalidate();
    }
}
