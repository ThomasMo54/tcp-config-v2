package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

public class ConfigComponent extends JPanel implements MouseListener {

    private static final int INSETS = 20;
    private static final int BUTTONS_MARGIN = 20;

    private final ConfigListComponent configListComponent;
    private final Config config;
    private final Color backgroundColor;
    private final Set<JButton> buttons = new HashSet<>();

    public ConfigComponent(ConfigListComponent configListComponent, Config config, Color backgroundColor) {
        this.configListComponent = configListComponent;
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
        constraints.gridx++;
        constraints.weightx = 0;
        constraints.insets = new Insets(0, 0, 0, BUTTONS_MARGIN);
        this.add(useButton, constraints);
        // Edit button
        JButton editButton = new JButton("Modifier");
        editButton.addActionListener(event -> editConfig());
        buttons.add(editButton);
        constraints.gridx++;
        this.add(editButton, constraints);
        // Export button
        JButton exportButton = new JButton("Exporter");
        exportButton.addActionListener(event -> exportConfig());
        buttons.add(exportButton);
        constraints.gridx++;
        this.add(exportButton, constraints);
        // Delete button
        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(event -> deleteConfig());
        buttons.add(deleteButton);
        constraints.gridx++;
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
        int answer = JOptionPane.showConfirmDialog(TCPConfig.getInstance().getMainWindow(),
                "Êtes-vous sûr de vouloir supprimer la configuration " + config.getName() + " ?",
                "Confirmer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if(answer == 1 || answer == -1)
            return;
        TCPConfig.getInstance().deleteConfig(config.getName());
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
