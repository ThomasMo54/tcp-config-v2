package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainWindow extends JFrame implements KeyListener {

    private static final String WINDOW_TITLE = "TCPConfig v" + TCPConfig.VERSION;
    private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(960, 600);
    public static final String SUCCESS_STRING = "success";

    private JPanel mainPanel;
    private ConfigListComponent configListComponent;
    private NewConfigComponent newConfigComponent;
    private EditConfigComponent editConfigComponent;

    private JButton newButton;
    private JButton importButton;
    private JButton listButton;
    private JButton openNetworkConnectionsButton;
    private JButton autoIpButton;
    private JButton autoDnsButton;

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
            showExceptionOptionPane(e);
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
        // Open network connections button
        openNetworkConnectionsButton = new JButton("Connex.");
        openNetworkConnectionsButton.setFocusable(false);
        openNetworkConnectionsButton.addActionListener(event -> openNetworkConnectionsMenu());
        // Auto IP button
        autoIpButton = new JButton("Auto-IP");
        autoIpButton.setFocusable(false);
        autoIpButton.addActionListener(event -> setIpConfig("autoip"));
        // Auto DNS
        autoDnsButton = new JButton("Auto-DNS");
        autoDnsButton.setFocusable(false);
        autoDnsButton.addActionListener(event -> setIpConfig("autodns"));
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
        mainPanel.add(createButtonsPanel(newButton, importButton, autoIpButton, autoDnsButton, openNetworkConnectionsButton), constraints);
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
        mainPanel.add(createButtonsPanel(listButton, importButton, autoIpButton, autoDnsButton, openNetworkConnectionsButton), constraints);
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
        mainPanel.add(createButtonsPanel(listButton, newButton, importButton, autoIpButton, autoDnsButton, openNetworkConnectionsButton), constraints);
        this.repaint();
        this.setVisible(true);
    }

    private void importConfig() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("S??lectionnez un fichier");
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

    private void openNetworkConnectionsMenu() {
        try {
            Runtime.getRuntime().exec("cmd /c ncpa.cpl");
        } catch (IOException e) {
            showExceptionOptionPane(e);
        }
    }

    private void setIpConfig(String option) {
        try {
            Process process = Runtime.getRuntime().exec(TCPConfig.getInstance().getJarLocation() + TCPConfig.INTERFACE_SET_IP_SCRIPT_FILE_PATH + " " + option);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = input.readLine();
            if(result == null || result.equals(SUCCESS_STRING)) {
                JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                        "??chec de l'op??ration",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                    "La configuration r??seau a bien ??t?? chang??e",
                    "Succ??s",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showExceptionOptionPane(e);
        }
    }

    public void showExceptionOptionPane(Exception e) {
        JOptionPane.showMessageDialog(TCPConfig.getInstance().getMainWindow(),
                e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
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
