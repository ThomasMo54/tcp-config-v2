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

    public static final String VERSION = "2.0";

    private static final String SAVE_FILE_PATH = "D:\\temp\\save.txt";
    private static final String WMI_ADAPTER_CONFIGURATION_CLASS = "Win32_NetworkAdapterConfiguration";

    private static TCPConfig instance;

    private final List<NetworkAdapter> networkAdapters;
    private final MainWindow mainWindow;
    private final List<Config> configs;

    public TCPConfig() {
        instance = this;
        this.configs = loadConfigs();
        this.mainWindow = new MainWindow();
        this.networkAdapters = loadNetworkAdapters();
    }

    private List<NetworkAdapter> loadNetworkAdapters() {
        return WMI4Java.get().getWMIObjectList(WMI_ADAPTER_CONFIGURATION_CLASS).stream().map(wmiObj -> new NetworkAdapter(wmiObj.get("Description"))).collect(Collectors.toList());
    }

    private List<Config> loadConfigs() {
        List<Config> configList = new ArrayList<>();
        File saveFile = new File(SAVE_FILE_PATH);
        try {
            if(!saveFile.exists()) {
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
            }
            BufferedReader reader = new BufferedReader(new FileReader(saveFile));
            try {
                String configNumberStr = reader.readLine();
                if(configNumberStr == null)
                    return configList;
                int configNumber = Integer.parseInt(configNumberStr);
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

    private void saveConfigs() {
        File saveFile = new File(SAVE_FILE_PATH);
        try {
            if(!saveFile.exists()) {
                saveFile.getParentFile().mkdirs();
                saveFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
            writer.write(configs.size() + "\n");
            for(Config config : configs) {
                writer.write(config.getName() + "\n");
                writer.write(config.getAdapter() + "\n");
                writer.write(config.getIp() + "\n");
                writer.write(config.getMask() + "\n");
                writer.write((config.getGateway() != null ? config.getGateway() : "") + "\n");
                writer.write((config.getFavDNS() != null ? config.getFavDNS() : "") + "\n");
                writer.write((config.getAuxDNS() != null ? config.getAuxDNS() : "") + "\n");
            }
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newConfig(Config config) {
        configs.add(config);
        mainWindow.updateConfigs();
        saveConfigs();
    }

    public void deleteConfig(String name) {
        configs.removeIf(config -> config.getName().equals(name));
        mainWindow.updateConfigs();
        saveConfigs();
    }

    public List<NetworkAdapter> getNetworkAdapters() {
        return networkAdapters;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
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
