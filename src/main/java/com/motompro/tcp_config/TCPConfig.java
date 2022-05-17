package com.motompro.tcp_config;

import com.motompro.tcp_config.window.MainWindow;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class TCPConfig {

    public static final String VERSION = "2.21";
    public static final String TCPC_FILE_EXTENSION = "tcpc";

    private static final String SAVE_FILE_PATH = "D:\\temp\\save.txt";
    private static final String INTERFACE_GET_SCRIPT_FILE_PATH = "D:\\temp\\NetInterfaceGetter.exe";
    private static final String GET_VERSION_URL = "http://motompro.com/tcpconfig-version.php";
    private static final int GET_VERSION_REQUEST_TIMEOUT = 5000;

    private static TCPConfig instance;

    private URL jarURL;
    private final Images images;
    private final List<NetworkAdapter> networkAdapters;
    private final MainWindow mainWindow;
    private final List<Config> configs;

    public TCPConfig() {
        instance = this;
        this.images = new Images();
        this.configs = loadConfigs();
        this.mainWindow = new MainWindow();
        this.networkAdapters = loadNetworkAdapters();
        checkForNewVersion();
    }

    private List<NetworkAdapter> loadNetworkAdapters() {
        List<NetworkAdapter> adapters = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(INTERFACE_GET_SCRIPT_FILE_PATH);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String adapter;
            while((adapter = input.readLine()) != null)
                adapters.add(new NetworkAdapter(adapter));
            return adapters;
        } catch (IOException e) {
            mainWindow.showExceptionOptionPane(e);
        }
        return adapters;
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

    private void checkForNewVersion() {
        try {
            URL url = new URL(GET_VERSION_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(GET_VERSION_REQUEST_TIMEOUT);
            CompletableFuture<Optional<String>> newerVersionCompletableFuture = new CompletableFuture<>();
            new Thread(() -> {
                try {
                    int status = con.getResponseCode();
                    if(status != 200) {
                        newerVersionCompletableFuture.complete(Optional.empty());
                        return;
                    }
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String newerVersion = in.readLine();
                    in.close();
                    con.disconnect();
                    newerVersionCompletableFuture.complete(Optional.ofNullable(newerVersion));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            newerVersionCompletableFuture.thenAccept(newerVersionOpt -> newerVersionOpt.ifPresent(newerVersion -> {
                if(newerVersion.equals(VERSION))
                    return;
                int answer = JOptionPane.showConfirmDialog(TCPConfig.getInstance().getMainWindow(),
                        "Une nouvelle version du logiciel a été trouvée (v" + newerVersion + "), voulez-vous la télécharger ?",
                        "Mise à jour",
                        JOptionPane.YES_NO_OPTION);
                if(answer != JOptionPane.YES_OPTION)
                    return;
            }));
        } catch (IOException e) {
            mainWindow.showExceptionOptionPane(e);
        }
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
        } catch (IOException e) {
            mainWindow.showExceptionOptionPane(e);
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

    public void editConfig(String configName, Config newConfig) {
        configs.removeIf(config -> config.getName().equals(configName));
        configs.add(newConfig);
        mainWindow.updateConfigs();
        saveConfigs();
    }

    public void exportConfig(Config config, File exportFile) {
        if(!exportFile.exists()) {
            try {
                exportFile.getParentFile().mkdirs();
                exportFile.createNewFile();
            } catch (IOException e) {
                mainWindow.showExceptionOptionPane(e);
                return;
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile));
            writer.write(config.getName() + "\n");
            writer.write(config.getIp() + "\n");
            writer.write(config.getMask() + "\n");
            writer.write((config.getGateway() != null ? config.getGateway() : "") + "\n");
            writer.write((config.getFavDNS() != null ? config.getFavDNS() : "") + "\n");
            writer.write((config.getAuxDNS() != null ? config.getAuxDNS() : "") + "\n");
            writer.close();
        } catch (IOException e) {
            mainWindow.showExceptionOptionPane(e);
        }
    }

    public void importConfig(File importFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(importFile));
            try {
                String name = reader.readLine();
                String ip = reader.readLine();
                String mask = reader.readLine();
                String gateway = reader.readLine();
                String favDNS = reader.readLine();
                String auxDNS = reader.readLine();
                reader.close();
                StringBuilder finalName = new StringBuilder(name);
                int i = 2;
                while(configs.stream().anyMatch(config -> config.getName().equals(finalName.toString()))) {
                    finalName.setLength(0);
                    finalName.append(name).append(" (").append(i).append(")");
                    i++;
                }
                configs.add(new Config(finalName.toString(), "", ip, mask, gateway, favDNS, auxDNS));
                mainWindow.updateConfigs();
                saveConfigs();
            } catch (NumberFormatException | IOException e) {
                mainWindow.showExceptionOptionPane(e);
            }
        } catch (FileNotFoundException e) {
            mainWindow.showExceptionOptionPane(e);
        }
    }

    public Images getImages() {
        return images;
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
        if(jarURL != null)
            return jarURL;
        try {
            final URL codeSourceLocation = TCPConfig.class.getProtectionDomain().getCodeSource().getLocation();
            if(codeSourceLocation != null) return codeSourceLocation;
        } catch(SecurityException | NullPointerException e) {
            mainWindow.showExceptionOptionPane(e);
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
            jarURL = new URL(path);
            return jarURL;
        } catch (MalformedURLException e) {
            mainWindow.showExceptionOptionPane(e);
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
