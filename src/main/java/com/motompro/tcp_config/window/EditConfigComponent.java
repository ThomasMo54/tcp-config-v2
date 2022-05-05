package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.TCPConfig;

public class EditConfigComponent extends ConfigForm {

    private Config config;
    private String lastName;

    public EditConfigComponent(String formName) {
        super(formName);
    }

    public void setConfig(Config config) {
        this.config = config;
        this.lastName = config.getName();
        if(TCPConfig.getInstance().getNetworkAdapters() != null)
            TCPConfig.getInstance().getNetworkAdapters().forEach(adapter -> adapterInput.addItem(adapter.getName()));
        nameInput.setText(config.getName());
        adapterInput.setSelectedItem(config.getAdapter());
        ipInput.setText(config.getIp());
        maskInput.setText(config.getMask());
        gatewayInput.setText(config.getGateway() != null ? config.getGateway() : "");
        favDNSInput.setText(config.getFavDNS() != null ? config.getFavDNS() : "");
        auxDNSInput.setText(config.getAuxDNS() != null ? config.getAuxDNS() : "");
    }

    @Override
    protected boolean isFormValid() {
        clearErrors();
        boolean success = true;
        // Name
        String name = nameInput.getText();
        if(name == null || name.equals("")) {
            showError(FIELD_MUST_BE_FILLED_ERROR, 3);
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

    @Override
    public void formConfirmed() {
        String name = nameInput.getText();
        String adapter = (String) adapterInput.getSelectedItem();
        String ip = ipInput.getText();
        String mask = maskInput.getText();
        String gateway = gatewayInput.getText();
        String favDNS = favDNSInput.getText();
        String auxDNS = auxDNSInput.getText();
        TCPConfig.getInstance().editConfig(lastName, new Config(name, adapter, ip, mask, gateway, favDNS, auxDNS));
        TCPConfig.getInstance().getMainWindow().setMainLayout();
    }
}
