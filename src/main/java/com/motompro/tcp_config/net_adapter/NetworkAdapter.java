package com.motompro.tcp_config.net_adapter;

public class NetworkAdapter {

    private final String name;
    private final int index;

    public NetworkAdapter(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
