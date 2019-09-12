package com.behradrvb.ppnclient;

import android.content.Context;

public class PPN {
    private Context context;
    private String host;
    private int port;
    private boolean interiorBroadcast;

    public PPN(Context context, String host, int port, boolean interiorBroadcast) {
        this.context = context;
        this.host = host;
        this.port = port;
        this.interiorBroadcast = interiorBroadcast;
    }

    public void init() {

    }
}
