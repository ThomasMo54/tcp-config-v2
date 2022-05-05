package com.motompro.tcp_config.window;

import com.motompro.tcp_config.Config;

import javax.swing.*;
import java.util.List;

public class ConfigListComponent extends JScrollPane {

    private List<Config> configs;

    public ConfigListComponent() {
        init();
    }

    private void init() {
        this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }
}
