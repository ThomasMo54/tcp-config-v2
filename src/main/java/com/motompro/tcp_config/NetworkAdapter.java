package com.motompro.tcp_config;

import java.io.IOException;

public class NetworkAdapter {

    private static final String INTERFACE_SET_IP_SCRIPT_FILE_PATH = "D:\\temp\\NetInterfaceIPSetter.exe";

    private final String name;

    public NetworkAdapter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setIPAddress(String ip, String mask, String gateway, String favDNS, String auxDNS) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        if(gateway == null && favDNS == null && auxDNS == null)
            runtime.exec(INTERFACE_SET_IP_SCRIPT_FILE_PATH + "\"" + name + "\" " + ip + " " + mask);
        else if(gateway == null)
            runtime.exec(INTERFACE_SET_IP_SCRIPT_FILE_PATH + "\"" + name + "\" " + ip + " " + mask + " " + favDNS + " " + auxDNS);
        else if(favDNS == null && auxDNS == null)
            runtime.exec(INTERFACE_SET_IP_SCRIPT_FILE_PATH + "\"" + name + "\" " + ip + " " + mask + " " + gateway);
        else
            runtime.exec(INTERFACE_SET_IP_SCRIPT_FILE_PATH + "\"" + name + "\" " + ip + " " + mask + " " + gateway + " " + favDNS + " " + auxDNS);
    }
}
