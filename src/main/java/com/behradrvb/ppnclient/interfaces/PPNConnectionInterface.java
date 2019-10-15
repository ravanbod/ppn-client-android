package com.behradrvb.ppnclient.interfaces;

public interface PPNConnectionInterface {
    /**
     * called when service tries to connect.
     */
    void OnTry();

    /**
     * called when connection established.
     */
    void OnNewConnectionEstablished();

    /**
     * called when Connection closed.
     */
    void OnConnectionClosed();
}
