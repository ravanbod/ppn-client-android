package com.behradrvb.ppnclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.behradrvb.ppnclient.services.RestarterBroadcast;

final public class PPN {
    private static String host;
    private static int port;
    private static boolean enable;

    /**
     * This function runs service and saves data ...
     */
    public static void init(Context context, String host, int port, boolean enable) {
        PPN.host = host;
        PPN.port = port;
        PPN.enable = enable;
        setPreferences(context);
        if (enable)
            context.sendBroadcast(new Intent(context, RestarterBroadcast.class)); // Sends broadcast to keeper service to start connectionService
    }

    /**
     * This function saves data in SharedPreferences
     */
    private static void setPreferences(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("PPN", Context.MODE_PRIVATE).edit();
        editor.putString("host", host);
        editor.putInt("port", port);
        editor.putBoolean("enable", enable);
        editor.apply();
    }
}
