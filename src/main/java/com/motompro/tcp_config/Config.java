package com.motompro.tcp_config;

public class Config {

    private final String name;
    private final String adapter;
    private final String ip;
    private final String mask;
    private final String gateway;
    private final String favDNS;
    private final String auxDNS;

    public Config(String name, String adapter, String ip, String mask, String gateway, String favDNS, String auxDNS) {
        this.name = name;
        this.adapter = adapter;
        this.ip = ip;
        this.mask = mask;
        this.gateway = gateway;
        this.favDNS = favDNS;
        this.auxDNS = auxDNS;
    }

    public String getName() {
        return name;
    }

    public String getAdapter() {
        return adapter;
    }

    public String getIp() {
        return ip;
    }

    public String getMask() {
        return mask;
    }

    public String getGateway() {
        return gateway;
    }

    public String getFavDNS() {
        return favDNS;
    }

    public String getAuxDNS() {
        return auxDNS;
    }
}
