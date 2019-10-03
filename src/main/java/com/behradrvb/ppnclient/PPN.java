package com.behradrvb.ppnclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.behradrvb.ppnclient.services.ConnectionService;

public class PPN {
    private Context context;
    private String host;
    private int port;
    private boolean interiorBroadcast;

    /**
     * @param interiorBroadcast "true" if you want to use interior broadcast receiver: which run SocketService with data that was saved into sharedPreferences by app
     */
    public PPN(Context context, String host, int port, boolean interiorBroadcast) {
        this.context = context;
        this.host = host;
        this.port = port;
        this.interiorBroadcast = interiorBroadcast;
    }

    /**
     * This function runs service and saves data ...
     */
    public void init() {
        setPreferences();
    }

    /**
     * This function saves data in SharedPreferences
     */
    private void setPreferences() {
        SharedPreferences.Editor editor = context.getSharedPreferences("PPN", Context.MODE_PRIVATE).edit();
        editor.putString("host", host);
        editor.putInt("port", port);
        editor.putBoolean("interiorBroadcast", interiorBroadcast);
        editor.apply();
    }
}
