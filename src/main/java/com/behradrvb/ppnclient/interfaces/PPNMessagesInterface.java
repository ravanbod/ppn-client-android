package com.behradrvb.ppnclient.interfaces;

import com.behradrvb.ppnclient.models.Message;

public interface PPNMessagesInterface {
    /**
     * called when a message is given by server.
     *
     * @param msg given message
     */
    void OnNewMessageReceived(Message msg);
}
