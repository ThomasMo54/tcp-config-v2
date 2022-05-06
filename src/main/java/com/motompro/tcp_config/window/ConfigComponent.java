package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.NetworkAdapter;
import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ConfigComponent extends JPanel implements MouseListener {

    private static final int INFOS_MARGIN = 5;
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
        GridLayout gridLayout = new GridLayout(2, 1);
        gridLayout.setVgap(INFOS_MARGIN);
        JPanel infosPanel = new JPanel(gridLayout);
        infosPanel.setBackground(backgroundColor);
        JLabel nameLabel = new JLabel(config.getName());
        infosPanel.add(nameLabel);
        JLabel infosLabel = new JLabel(config.getIp() + "  |  " + config.getAdapter());
        infosLabel.setForeground(Color.GRAY);
        infosPanel.add(infosLabel);
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
            button.setFocusable(false);
            button.setVisible(false);
            button.addMouseListener(this);
        });
        this.add(deleteButton, constraints);
    }

    private void useConfig() {
        int answer = JOptionPane.showConfirmDialog(TCPConfig.getInstance().getMainWindow(),
                "Êtes-vous sûr de vouloir utiliser la configuration " + config.getName() + " ?",
                "Confirmer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
        if(answer != JOptionPane.OK_OPTION)
            return;
        if(TCPConfig.getInstance().getNetworkAdapters() == null) {
            JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                    "Aucune interface réseau n'a été trouvée",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Optional<NetworkAdapter> adapter = TCPConfig.getInstance().getNetworkAdapters().stream()
                .filter(a -> a.getName().equals(config.getAdapter()))
                .findFirst();
        if(!adapter.isPresent()) {
            JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                    "L'interface réseau " + config.getAdapter() + " n'est pas connectée",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            boolean success = adapter.get().setIPAddress(config.getIp(), config.getMask(), config.getGateway(), config.getFavDNS(), config.getAuxDNS());
            if(!success) {
                JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                        "Une erreur est survenue. La configuration réseau n'a pas pu être changée",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                    "La configuration réseau a bien été changée",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editConfig() {
        TCPConfig.getInstance().getMainWindow().setEditConfigLayout(config);
    }

    private void exportConfig() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Sélectionnez un fichier");
        fileChooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                else
                    return f.getName().toLowerCase().endsWith("." + TCPConfig.TCPC_FILE_EXTENSION);
            }
            @Override
            public String getDescription() {
                return "TCP Config (*." + TCPConfig.TCPC_FILE_EXTENSION + ")";
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setSelectedFile(new File(config.getName() + "." + TCPConfig.TCPC_FILE_EXTENSION));
        int answer = fileChooser.showSaveDialog(TCPConfig.getInstance().getMainWindow());
        if(answer != JFileChooser.APPROVE_OPTION)
            return;
        File fileToSave = fileChooser.getSelectedFile();
        if(!fileToSave.getName().endsWith(TCPConfig.TCPC_FILE_EXTENSION)) {
            JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                    "L'extension du fichier est incorrecte, \"*." + TCPConfig.TCPC_FILE_EXTENSION + "\" attendu",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        TCPConfig.getInstance().exportConfig(config, fileToSave);
    }

    private void deleteConfig() {
        int answer = JOptionPane.showConfirmDialog(TCPConfig.getInstance().getMainWindow(),
                "Êtes-vous sûr de vouloir supprimer la configuration " + config.getName() + " ?",
                "Confirmer",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if(answer != JOptionPane.OK_OPTION)
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
