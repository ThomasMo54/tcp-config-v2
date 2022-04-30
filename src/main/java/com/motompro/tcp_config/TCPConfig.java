package com.motompro.tcp_config;

import com.motompro.tcp_config.net_adapter.NetworkAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TCPConfig {

    private static TCPConfig instance;
    private static final String INTERFACE_GETTER_SCRIPT_FILE_NAME = "NetInterfaceGetter.exe";

    private final List<NetworkAdapter> networkAdapters;

    public TCPConfig() {
        this.networkAdapters = loadNetworkAdapters();
    }

    public List<NetworkAdapter> loadNetworkAdapters() {
        List<NetworkAdapter> adapters = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec("D:\\temp\\" + INTERFACE_GETTER_SCRIPT_FILE_NAME);
            BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String name;
            while((name = processOutputReader.readLine()) != null) {
                String index = processOutputReader.readLine();
                adapters.add(new NetworkAdapter(name, Integer.parseInt(index)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return adapters;
    }

    public List<NetworkAdapter> getNetworkAdapters() {
        return networkAdapters;
    }

    public URL getJarLocation() {
        try {
            final URL codeSourceLocation = TCPConfig.class.getProtectionDomain().getCodeSource().getLocation();
            if(codeSourceLocation != null) return codeSourceLocation;
        } catch(SecurityException | NullPointerException e) {
            e.printStackTrace();
        }

        final URL classResource = TCPConfig.class.getResource(TCPConfig.class.getSimpleName() + ".class");
        if (classResource == null) return null;

        final String url = classResource.toString();
        final String suffix = TCPConfig.class.getCanonicalName().replace('.', '/') + ".class";
        if (!url.endsWith(suffix)) return null;

        final String base = url.substring(0, url.length() - suffix.length());

        String path = base;

        if (path.startsWith("jar:")) path = path.substring(4, path.length() - 2);

        try {
            return new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TCPConfig getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        instance = new TCPConfig();
    }
}
