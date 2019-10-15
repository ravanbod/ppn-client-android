package com.behradrvb.ppnclient.models;

public class Server {
    private String host;
    private int port;
    private String session_id;

    public Server(String host, int port, String session_id) {
        this.host = host;
        this.port = port;
        this.session_id = session_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
