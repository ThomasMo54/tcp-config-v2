package com.motompro.tcp_config;

import com.motompro.tcp_config.window.MainWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NetworkAdapter {

    private final String name;

    public NetworkAdapter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean setIPAddress(String ip, String mask, String gateway, String favDNS, String auxDNS) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process;
        if(gateway == null && favDNS == null && auxDNS == null)
            process = runtime.exec(MainWindow.INTERFACE_SET_IP_SCRIPT_FILE_PATH + " setip \"" + name + "\" " + ip + " " + mask);
        else if(gateway == null)
            process = runtime.exec(MainWindow.INTERFACE_SET_IP_SCRIPT_FILE_PATH + " setip \"" + name + "\" " + ip + " " + mask + " " + favDNS + " " + auxDNS);
        else if(favDNS == null && auxDNS == null)
            process = runtime.exec(MainWindow.INTERFACE_SET_IP_SCRIPT_FILE_PATH + " setip \"" + name + "\" " + ip + " " + mask + " " + gateway);
        else
            process = runtime.exec(MainWindow.INTERFACE_SET_IP_SCRIPT_FILE_PATH + " setip \"" + name + "\" " + ip + " " + mask + " " + gateway + " " + favDNS + " " + auxDNS);
        BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = input.readLine();
        return result != null && result.equals(MainWindow.SUCCESS_STRING);
    }
}
