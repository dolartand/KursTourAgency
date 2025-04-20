package com.client;

public class AppConfig {
    private static String host = "127.0.0.1";
    private static int port = 11000;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        AppConfig.host = host;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        AppConfig.port = port;
    }
}
