package com.motompro.tcp_config.net_adapter;

import java.io.IOException;

public class NetworkAdapter {

    private static final String INTERFACE_SET_IP_SCRIPT_FILE_NAME = "NetInterfaceSetIp.exe";
    private static final String INTERFACE_SET_GATEWAY_SCRIPT_FILE_NAME = "NetInterfaceSetGateway.exe";

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

    public void setIPAddress(String ip, String mask) throws IOException {
        Runtime.getRuntime().exec("D:\\temp\\" + INTERFACE_SET_IP_SCRIPT_FILE_NAME + name + " " + ip + " " + mask);
    }

    public void setGateway(String gateway) throws IOException {
        Runtime.getRuntime().exec("D:\\temp\\" + INTERFACE_SET_GATEWAY_SCRIPT_FILE_NAME + name + " " + gateway);
    }
}
