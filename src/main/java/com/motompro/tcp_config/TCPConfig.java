package com.motompro.tcp_config;

import com.motompro.tcp_config.window.MainWindow;
import com.profesorfalken.wmi4java.WMI4Java;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TCPConfig {

    private static final String SAVE_FILE_NAME = "save.txt";

    private static TCPConfig instance;

    private final List<NetworkAdapter> networkAdapters;
    private final List<Config> configs;

    public TCPConfig() {
        this.configs = loadConfigs();
        new MainWindow();
        this.networkAdapters = loadNetworkAdapters();
    }

    public List<NetworkAdapter> loadNetworkAdapters() {
        return WMI4Java.get().getWMIObjectList("Win32_NetworkAdapterConfiguration").stream().map(wmiObj -> new NetworkAdapter(wmiObj.get("Description"))).collect(Collectors.toList());
    }

    public List<Config> loadConfigs() {
        List<Config> configList = new ArrayList<>();
        File saveFile = new File("D:\\temp\\" + SAVE_FILE_NAME);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(saveFile));
            try {
                int configNumber = Integer.parseInt(reader.readLine());
                for(int i = 0; i < configNumber; i++) {
                    String name = reader.readLine();
                    String adapter = reader.readLine();
                    String ip = reader.readLine();
                    String mask = reader.readLine();
                    String gateway = reader.readLine();
                    String favDNS = reader.readLine();
                    String auxDNS = reader.readLine();
                    configList.add(new Config(name, adapter, ip, mask, gateway, favDNS, auxDNS));
                }
                reader.close();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configList;
    }

    public List<NetworkAdapter> getNetworkAdapters() {
        return networkAdapters;
    }

    public List<Config> getConfigs() {
        return configs;
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
