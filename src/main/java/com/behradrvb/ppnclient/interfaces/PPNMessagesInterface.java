package com.behradrvb.ppnclient.interfaces;

public interface PPNMessagesInterface {
    /**
     * called when a message is given by server.
     *
     * @param msg given message
     */
    void OnNewMessageReceived(String msg);
}
