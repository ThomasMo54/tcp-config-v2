package com.motompro.tcp_config.net_adapter;

public class NetworkAdapter {

    private final String displayName;
    private final String name;

    public NetworkAdapter(String displayName, String name) {
        this.displayName = displayName;
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }
}
