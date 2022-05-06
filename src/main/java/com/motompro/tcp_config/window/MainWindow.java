package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class MainWindow extends JFrame implements KeyListener {

    private static final String WINDOW_TITLE = "TCPConfig v" + TCPConfig.VERSION;
    private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(960, 600);

    private JPanel mainPanel;
    private ConfigListComponent configListComponent;
    private NewConfigComponent newConfigComponent;
    private EditConfigComponent editConfigComponent;

    private JButton newButton;
    private JButton importButton;
    private JButton listButton;

    public MainWindow() {
        init();
        setMainLayout();
        this.setVisible(true);
    }

    private void init() {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Window settings
        this.setTitle(WINDOW_TITLE);
        this.setSize(DEFAULT_WINDOW_DIMENSION);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        // Init main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        this.setContentPane(mainPanel);
        // Init config list component
        configListComponent = new ConfigListComponent();
        configListComponent.updateConfigs(TCPConfig.getInstance().getConfigs());
        // Init new config component
        newConfigComponent = new NewConfigComponent("Nouvelle configuration");
        // Init edit config component
        editConfigComponent = new EditConfigComponent("Modifier la configuration");
        // Init buttons
        initButtons();
    }

    private void initButtons() {
        // New button
        newButton = new JButton("Nouveau");
        newButton.setFocusable(false);
        newButton.addActionListener(event -> setNewConfigLayout());
        // Import button
        importButton = new JButton("Importer");
        importButton.setFocusable(false);
        importButton.addActionListener(event -> importConfig());
        // List button
        listButton = new JButton("Liste");
        listButton.setFocusable(false);
        listButton.addActionListener(event -> setMainLayout());
    }

    private JPanel createButtonsPanel(JButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
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

    public void setMainLayout() {
        mainPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        // Add config list
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        mainPanel.add(configListComponent, constraints);
        // Add buttons panel
        constraints.gridx = 1;
        constraints.weightx = 0;
        mainPanel.add(createButtonsPanel(newButton, importButton), constraints);
        this.repaint();
        this.setVisible(true);
    }

    private void setNewConfigLayout() {
        mainPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        // Add new config component
        newConfigComponent.clear();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        mainPanel.add(newConfigComponent, constraints);
        // Add buttons panel
        constraints.gridx = 1;
        constraints.weightx = 0;
        mainPanel.add(createButtonsPanel(listButton, importButton), constraints);
        this.repaint();
        this.setVisible(true);
    }

    public void setEditConfigLayout(Config config) {
        mainPanel.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();
        // Add edit config component
        editConfigComponent.setConfig(config);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        mainPanel.add(editConfigComponent, constraints);
        // Add buttons panel
        constraints.gridx = 1;
        constraints.weightx = 0;
        mainPanel.add(createButtonsPanel(listButton, newButton, importButton), constraints);
        this.repaint();
        this.setVisible(true);
    }

    private void importConfig() {
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
        int answer = fileChooser.showOpenDialog(TCPConfig.getInstance().getMainWindow());
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
        TCPConfig.getInstance().importConfig(fileToSave);
    }

    public void updateConfigs() {
        configListComponent.updateConfigs(TCPConfig.getInstance().getConfigs());
        configListComponent.revalidate();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            setMainLayout();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
