package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ConfigComponent extends JPanel implements MouseListener {

    private static final int INSETS = 20;
    private static final int BUTTONS_MARGIN = 20;

    private final Config config;
    private final Color backgroundColor;
    private final Set<JButton> buttons = new HashSet<>();

    public ConfigComponent(Config config, Color backgroundColor) {
        this.config = config;
        this.backgroundColor = backgroundColor;
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        this.setBackground(backgroundColor);
        this.addMouseListener(this);
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
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(INSETS, INSETS, INSETS, INSETS);
        this.add(infosPanel, constraints);
        // Use button
        JButton useButton = new JButton("Utiliser");
        useButton.addActionListener(event -> useConfig());
        Border emptyBorder = BorderFactory.createEmptyBorder();
        buttons.add(useButton);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 1;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 0, BUTTONS_MARGIN);
        this.add(useButton, constraints);
        // Edit button
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(event -> editConfig());
        buttons.add(editButton);
        constraints.gridx = 2;
        this.add(editButton, constraints);
        // Export button
        JButton exportButton = new JButton("Exporter");
        exportButton.addActionListener(event -> exportConfig());
        buttons.add(exportButton);
        constraints.gridx = 3;
        this.add(exportButton, constraints);
        // Delete button
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(event -> deleteConfig());
        buttons.add(deleteButton);
        constraints.gridx = 4;
        // Set buttons properties
        buttons.forEach(button -> {
            button.setBorder(emptyBorder);
            button.setContentAreaFilled(false);
            button.setVisible(false);
            button.addMouseListener(this);
        });
        this.add(deleteButton, constraints);
    }

    private void useConfig() {

    }

    private void editConfig() {

    }

    private void exportConfig() {

    }

    private void deleteConfig() {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        buttons.forEach(button -> button.setVisible(true));
        if(e.getComponent() instanceof JButton)
            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        buttons.forEach(button -> button.setVisible(false));
        if(e.getComponent() instanceof JButton)
            e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}
