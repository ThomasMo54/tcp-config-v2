package com.motompro.tcp_config;

public class NetAdapter {

    private final String name;
    private final int index;

    public NetAdapter(String name, int index) {
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
