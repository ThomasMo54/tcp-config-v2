package com.motompro.tcp_config;

import com.motompro.tcp_config.window.MainWindow;
import com.profesorfalken.wmi4java.WMI4Java;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TCPConfig {

    private static TCPConfig instance;

    private final List<NetworkAdapter> networkAdapters;

    public TCPConfig() {
        this.networkAdapters = new ArrayList<>();
        new MainWindow();
    }

    public List<NetworkAdapter> loadNetworkAdapters() {
        return WMI4Java.get().getWMIObjectList("Win32_NetworkAdapterConfiguration").stream().map(wmiObj -> new NetworkAdapter(wmiObj.get("Description"))).collect(Collectors.toList());
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
