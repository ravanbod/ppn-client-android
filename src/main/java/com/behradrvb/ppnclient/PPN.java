package com.behradrvb.ppnclient;

import android.content.Context;
import android.content.SharedPreferences;

public class PPN {
    private Context context;
    private String host;
    private int port;
    private boolean interiorBroadcast;

    public PPN(Context context, String host, int port, boolean interiorBroadcast) {
        this.context = context;
        this.host = host;
        this.port = port;
        this.interiorBroadcast = interiorBroadcast; // "true" if you want to use interior broadcast receiver: which run SocketService with data that was saved into sharedPref by app
    }

    public void init() {
        setPreferences();
    }

    private void setPreferences() { //This function saves data in SharedPreferences
        SharedPreferences.Editor editor = context.getSharedPreferences("PPN", Context.MODE_PRIVATE).edit();
        editor.putString("host", host);
        editor.putInt("port", port);
        editor.putBoolean("interiorBroadcast", interiorBroadcast);
        editor.apply();
    }
}
