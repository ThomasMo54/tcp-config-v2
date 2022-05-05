package com.motompro.tcp_config.window;

import com.motompro.tcp_config.TCPConfig;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class ConfigForm extends JPanel {

    private static final Dimension TEXT_FIELD_SIZE = new Dimension(250, 25);
    private static final int CATEGORY_OFFSET = 20;
    private static final int INPUT_OFFSET = 5;
    private static final int BUTTON_OFFSET = 15;
    protected static final Pattern IP_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
    protected static final String NAME_ALREADY_EXISTS_ERROR = "Une config avec ce nom existe déjà";
    protected static final String FIELD_MUST_BE_FILLED_ERROR = "Ce champ doit être rempli";
    protected static final String IP_NOT_VALID_ERROR = "Cette adresse IP n'est pas valide";

    private final String formName;
    protected final Set<JTextField> inputs = new HashSet<>();

    protected JTextField nameInput;
    protected JComboBox<String> adapterInput;
    protected JTextField ipInput;
    protected JTextField maskInput;
    protected JTextField gatewayInput;
    protected JTextField favDNSInput;
    protected JTextField auxDNSInput;

    public ConfigForm(String formName) {
        this.formName = formName;
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        // Title
        JLabel titleLabel = new JLabel(formName);
        this.add(titleLabel, constraints);
        // Config name
        constraints.gridy = 1;
        constraints.insets = new Insets(CATEGORY_OFFSET, 0, 0, 0);
        constraints.anchor = GridBagConstraints.LINE_START;
        JLabel nameLabel = new JLabel("Nom");
        this.add(nameLabel, constraints);
        constraints.gridy = 2;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        nameInput = new JTextField();
        nameInput.setPreferredSize(TEXT_FIELD_SIZE);
        inputs.add(nameInput);
        this.add(nameInput, constraints);
        // Config adapter
        constraints.gridy = 4;
        JLabel adapterLabel = new JLabel("Interface réseau");
        this.add(adapterLabel, constraints);
        constraints.gridy = 5;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        adapterInput = new JComboBox<>();
        adapterInput.setFocusable(false);
        adapterInput.setPreferredSize(TEXT_FIELD_SIZE);
        this.add(adapterInput, constraints);
        // Config ip
        constraints.gridy = 7;
        constraints.insets = new Insets(CATEGORY_OFFSET, 0, 0, 0);
        JLabel ipLabel = new JLabel("Adresse IP");
        this.add(ipLabel, constraints);
        constraints.gridy = 8;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        ipInput = new JTextField();
        ipInput.setPreferredSize(TEXT_FIELD_SIZE);
        inputs.add(ipInput);
        this.add(ipInput, constraints);
        // Config mask
        constraints.gridy = 10;
        JLabel maskLabel = new JLabel("Masque de sous-réseau");
        this.add(maskLabel, constraints);
        constraints.gridy = 11;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        maskInput = new JTextField();
        maskInput.setPreferredSize(TEXT_FIELD_SIZE);
        inputs.add(maskInput);
        this.add(maskInput, constraints);
        // Config gateway
        constraints.gridy = 13;
        JLabel gatewayLabel = new JLabel("Passerelle réseau");
        this.add(gatewayLabel, constraints);
        constraints.gridy = 14;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        gatewayInput = new JTextField();
        gatewayInput.setPreferredSize(TEXT_FIELD_SIZE);
        inputs.add(gatewayInput);
        this.add(gatewayInput, constraints);
        // Config fav dns
        constraints.gridy = 16;
        constraints.insets = new Insets(CATEGORY_OFFSET, 0, 0, 0);
        JLabel favDNSLabel = new JLabel("Serveur DNS préféré");
        this.add(favDNSLabel, constraints);
        constraints.gridy = 17;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        favDNSInput = new JTextField();
        favDNSInput.setPreferredSize(TEXT_FIELD_SIZE);
        inputs.add(favDNSInput);
        this.add(favDNSInput, constraints);
        // Config gateway
        constraints.gridy = 19;
        JLabel auxDNSLabel = new JLabel("Serveur DNS auxiliaire");
        this.add(auxDNSLabel, constraints);
        constraints.gridy = 20;
        constraints.insets = new Insets(INPUT_OFFSET, 0, 0, 0);
        auxDNSInput = new JTextField();
        auxDNSInput.setPreferredSize(TEXT_FIELD_SIZE);
        inputs.add(auxDNSInput);
        this.add(auxDNSInput, constraints);
        // Add button
        constraints.gridy = 22;
        constraints.insets = new Insets(BUTTON_OFFSET, 0, 0, 0);
        JButton confirmButton = new JButton("Confirmer");
        confirmButton.setFocusable(false);
        confirmButton.setPreferredSize(new Dimension(TEXT_FIELD_SIZE.width, 30));
        confirmButton.addActionListener(event ->  {
            if(!isFormValid())
                return;
            formConfirmed();
        });
        this.add(confirmButton, constraints);
    }

    public abstract void formConfirmed();

    protected boolean isFormValid() {
        clearErrors();
        boolean success = true;
        // Name
        String name = nameInput.getText();
        if(name == null || name.equals("")) {
            showError(FIELD_MUST_BE_FILLED_ERROR, 3);
            success = false;
        } else if(TCPConfig.getInstance().getConfigs().stream().anyMatch(config -> config.getName().equals(name))) {
            showError(NAME_ALREADY_EXISTS_ERROR, 3);
            success = false;
        }
        // Adapter
        String adapter = (String) adapterInput.getSelectedItem();
        if(adapter == null || adapter.equals("")) {
            showError(FIELD_MUST_BE_FILLED_ERROR, 6);
            success = false;
        }
        // IP
        String ip = ipInput.getText();
        if(ip == null || ip.equals("")) {
            showError(FIELD_MUST_BE_FILLED_ERROR, 9);
            success = false;
        } else if(!IP_PATTERN.matcher(ip).matches()) {
            showError(IP_NOT_VALID_ERROR, 9);
            success = false;
        }
        // Mask
        String mask = maskInput.getText();
        if(mask == null || mask.equals("")) {
            showError(FIELD_MUST_BE_FILLED_ERROR, 12);
            success = false;
        } else if(!IP_PATTERN.matcher(mask).matches()) {
            showError(IP_NOT_VALID_ERROR, 12);
            success = false;
        }
        // Gateway
        String gateway = gatewayInput.getText();
        if(gateway != null && !gateway.equals("") && !IP_PATTERN.matcher(gateway).matches()) {
            showError(IP_NOT_VALID_ERROR, 15);
            success = false;
        }
        // Fav DNS
        String favDNS = favDNSInput.getText();
        if(favDNS != null && !favDNS.equals("") && !IP_PATTERN.matcher(favDNS).matches()) {
            showError(IP_NOT_VALID_ERROR, 18);
            success = false;
        }
        // Aux DNS
        String auxDNS = auxDNSInput.getText();
        if(auxDNS != null && !auxDNS.equals("") && !IP_PATTERN.matcher(auxDNS).matches()) {
            showError(IP_NOT_VALID_ERROR, 21);
            success = false;
        }
        this.revalidate();
        return success;
    }

    protected void showError(String error, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.LINE_START;
        JLabel errorLabel = new JLabel(error);
        errorLabel.setForeground(Color.RED);
        this.add(errorLabel, constraints);
    }

    protected void clearErrors() {
        for(Component component : this.getComponents()) {
            if(component instanceof JLabel && component.getForeground().equals(Color.RED))
                this.remove(component);
        }
    }
}
