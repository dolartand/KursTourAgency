package com.client;

public class SessionHolder {
    private static String sessionId;

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        SessionHolder.sessionId = sessionId;
    }
}
