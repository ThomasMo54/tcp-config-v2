package com.motompro.tcp_config;

import java.io.IOException;

public class NetworkAdapter {

    private static final String INTERFACE_SET_IP_SCRIPT_FILE_NAME = "NetInterfaceIPSetter.exe";

    private final String name;

    public NetworkAdapter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIPAddress(String ip, String mask, String gateway, String favDNS, String auxDNS) throws IOException {
        Runtime.getRuntime().exec("D:\\temp\\" + INTERFACE_SET_IP_SCRIPT_FILE_NAME + name + " " + ip + " " + mask);
    }
}
