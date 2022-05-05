package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;
import com.motompro.tcp_config.TCPConfig;

public class NewConfigComponent extends ConfigForm {

    public NewConfigComponent(String formName) {
        super(formName);
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
        TCPConfig.getInstance().newConfig(new Config(name, adapter, ip, mask, "".equals(gateway) ? null : gateway, "".equals(favDNS) ? null : favDNS, "".equals(auxDNS) ? null : auxDNS));
        TCPConfig.getInstance().getMainWindow().setMainLayout();
    }

    public void clear() {
        // Clear inputs
        inputs.forEach(input -> input.setText(""));
        // Clear errors
        clearErrors();
        // Update adapters
        if(TCPConfig.getInstance().getNetworkAdapters() != null)
            TCPConfig.getInstance().getNetworkAdapters().forEach(adapter -> adapterInput.addItem(adapter.getName()));
    }
}
